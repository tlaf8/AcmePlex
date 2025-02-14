package com.AcmePlex.Control;

import com.AcmePlex.Boundary.ttt;
import com.AcmePlex.Database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class UserController {

    /**
     * Validate login credentials.
     */
    public boolean validateLogin(String email, String password) {
        boolean isValid = false;
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            String query = "SELECT Email, RPassword FROM REGISTEREDUSERS WHERE Email = ? AND RPassword = ?";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            isValid = stmt.executeQuery().next();
            stmt.close();
            db_conn.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    /**
     * Validate and register a new user in the database.
     */
    public boolean signupUser(int newId, String email, String password, String fullName, String address, String cardNumber, String csv, String expiryDate) {
        if (!isSignupValid(email, cardNumber, csv, expiryDate)) {
            return false;
        }

        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int rowsInserted;

            if (newId > 0) {
                String redQuery = "INSERT INTO REGISTEREDUSERS (RegisteredUserID, RName, Email, RPassword, Address) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = db_conn.getConnection().prepareStatement(redQuery);
                pst.setInt(1, newId);
                pst.setString(2, fullName);
                pst.setString(3, email);
                pst.setString(4, password);
                pst.setString(5, address);
                pst.executeUpdate();
                pst.close();
                Timer timer = new Timer(true); // Daemon thread
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            DatabaseConnection db_conn = new DatabaseConnection();
                            double currentAmount = db_conn.getAmountRemaining(cardNumber);
                            db_conn.setAmountRemaining(cardNumber, currentAmount - ttt.annualFee);
                            System.out.println("Deducted " + ttt.annualFee + " from " + cardNumber);
                        } catch (SQLException | ClassNotFoundException error) {
                            error.printStackTrace();
                            run();
                        }
                    }
                }, 0, 365L * 24 * 60 * 60 * 1000);
            }

            String ccQuery = "INSERT INTO CREDITCARDS (RegisteredUserID, CardHolderName, CardNumber, CSV, ExpirationDate, CreditAmount) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst2 = db_conn.getConnection().prepareStatement(ccQuery);
            pst2.setInt(1, newId);
            pst2.setString(2, fullName);
            pst2.setString(3, cardNumber);
            pst2.setString(4, csv);
            pst2.setString(5, expiryDate.replace("/", ""));
            pst2.setInt(6, 500);
            rowsInserted = pst2.executeUpdate();
            pst2.close();

            db_conn.close();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate all input fields for signup.
     */
    public boolean isSignupValid(String email, String cardNumber, String csv, String expiryDate) {
        return isValidEmail(email) &&
               isValidCardNumber(cardNumber) &&
               isValidCSV(csv) &&
               isValidExpiryDate(expiryDate);
    }

    /**
     * Validate email format.
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Validate credit card number using the Luhn algorithm.
     */
    private boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false; // Card number must be between 13-19 digits
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

    /**
     * Validate CSV (Card Security Value).
     */
    private boolean isValidCSV(String csv) {
        return csv != null && csv.matches("\\d{3}");
    }

    /**
     * Validate credit card expiry date (format: MM/YY).
     */
    private boolean isValidExpiryDate(String expiryDate) {
        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000; // Convert YY to YYYY

            if (month < 1 || month > 12) {
                return false; // Invalid month
            }

            // Check if the expiration date is in the future
            LocalDate expiry = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
            return !expiry.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false; // Invalid format or parsing error
        }
    }

    private Date getNextYearDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1); // Add one year
        return calendar.getTime();
    }
}

