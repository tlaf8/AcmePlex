package com.AcmePlex.Database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DatabaseConnection {

    // Database connection details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/ACMEPLEX";
    public static final String USER = "root";
    public static final String PASSWORD = "tinguspingus";

    private final Connection connection;

    public DatabaseConnection() throws SQLException, ClassNotFoundException {
        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }

    public int getMovieIdFromName(String movieName) throws SQLException {
        String query = "SELECT MovieID FROM MOVIES WHERE Title = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, movieName);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        int movieId = rs.getInt("MovieID");
        stmt.close();
        rs.close();
        return movieId;
    }

    public String getMovieNameFromId(int movieId) throws SQLException {
        String query = "SELECT Title FROM MOVIES WHERE MovieID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, movieId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return null;
        }
        rs.next();
        String movieName = rs.getString("Title");
        stmt.close();
        rs.close();
        return movieName;
    }

    public int getTheaterIdFromLocation(String theaterLocation) throws SQLException {
        String query = "SELECT TheaterID FROM THEATERS WHERE Location = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, theaterLocation);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        int theaterId = rs.getInt("TheaterID");
        stmt.close();
        rs.close();
        return theaterId;
    }

    public String getTheaterLocationFromId(int theaterId) throws SQLException {
        String query = "SELECT Location FROM THEATERS WHERE TheaterID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, theaterId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return null;
        }
        rs.next();
        String theaterName = rs.getString("Title");
        stmt.close();
        rs.close();
        return theaterName;
    }

    public int getShowtimeIdFromTime(int TheaterID, int MovieID, String startTime) throws SQLException {
        String query = "SELECT ShowtimeID FROM SHOWTIMES WHERE TheaterID = ? AND MovieID = ? AND StartTime = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, TheaterID);
        stmt.setInt(2, MovieID);
        stmt.setString(3, startTime);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        int showtimeId = rs.getInt("ShowtimeID");
        stmt.close();
        rs.close();
        return showtimeId;
    }

    public String getShowtimeFromId(int showtimeId) throws SQLException {
        String query = "SELECT StartTime FROM SHOWTIMES WHERE ShowtimeID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, showtimeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return null;
        }
        rs.next();
        String startTime = rs.getString("StartTime");
        stmt.close();
        rs.close();
        return startTime;
    }

    public int getShowtimeIdFromTicket(String ticketId) throws SQLException {
        String query = "SELECT ShowtimeID FROM TICKETS WHERE TicketID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, ticketId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        int showtimeId = rs.getInt("ShowtimeID");
        stmt.close();
        rs.close();

        return showtimeId;
    }

    public int getMovieIdFromShowtimeId(int showtimeId) throws SQLException {
        String query = "SELECT MovieID FROM SHOWTIMES WHERE ShowtimeID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, showtimeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        int movieId = rs.getInt("MovieID");
        stmt.close();
        rs.close();
        return movieId;
    }

    public int getTheaterIdFromShowtimeId(int showtimeId) throws SQLException {
        String query = "SELECT TheaterID FROM SHOWTIMES WHERE ShowtimeID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, showtimeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        int theaterId = rs.getInt("TheaterID");
        stmt.close();
        rs.close();
        return theaterId;
    }

    public String getDateFromShowtimeId(int showtimeId) throws SQLException {
        String query = "SELECT SDate, StartTime FROM SHOWTIMES WHERE ShowtimeID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, showtimeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return "Unknown";
        }
        rs.next();
        String date = rs.getString("SDate");
        String time = rs.getString("StartTime");
        stmt.close();
        rs.close();
        return date + " " + time;
    }

    public String getUserIDFromEmail(String email) throws SQLException {
        String query = "SELECT RegisteredUserID FROM REGISTEREDUSERS WHERE Email = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return "Unknown";
        }
        rs.next();
        String userId = rs.getString("RegisteredUserID");
        stmt.close();
        rs.close();
        return userId;
    }

    public String getCCFromTicketID(String ticketId) throws SQLException {
        String query = "SELECT CardNumber FROM TICKETS WHERE TicketID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, ticketId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return null;
        }
        rs.next();
        String cardNumber = rs.getString("CardNumber");
        stmt.close();
        rs.close();
        return cardNumber;
    }

    public double getPriceFromTicketID(String ticketId) throws SQLException {
        String query = "SELECT Price FROM TICKETS WHERE TicketID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, ticketId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        double price = rs.getDouble("Price");
        stmt.close();
        rs.close();
        return price;
    }

    public String getCCFromID(String registeredUserId) throws SQLException {
        String query = "SELECT CardNumber FROM CREDITCARDS WHERE RegisteredUserID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, registeredUserId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return null;
        }
        rs.next();
        String cardNumber = rs.getString("CardNumber");
        stmt.close();
        rs.close();
        return cardNumber;
    }

    public boolean ccExists(String cc) throws SQLException {
        String query = "SELECT * FROM CREDITCARDS WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, cc);
        ResultSet rs = stmt.executeQuery();
        return rs.isBeforeFirst();
    }

    public double getAmountRemaining(String cc) throws SQLException {
        String query = "SELECT CreditAmount FROM CREDITCARDS WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, cc);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        double credit = rs.getDouble("CreditAmount");
        stmt.close();
        rs.close();
        return credit;
    }

    public double getRemainingInStoreCredit(String cc) throws SQLException {
        String query = "SELECT InStoreCredit FROM CREDITCARDS WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, cc);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return -1;
        }
        rs.next();
        double credit = rs.getDouble("InStoreCredit");
        stmt.close();
        rs.close();
        return credit;
    }

    public boolean setAmountRemaining(String cc, double amount) throws SQLException {
        String query = "UPDATE CREDITCARDS SET CreditAmount = ? WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setDouble(1, amount);
        stmt.setString(2, cc);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean setInStoreCreditRemaining(String cc, double amount) throws SQLException {
        String query = "UPDATE CREDITCARDS SET InStoreCredit = ? WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setDouble(1, amount);
        stmt.setString(2, cc);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean invalidateTicket(String cardNumber) throws SQLException {
        String query = "UPDATE TICKETS SET IsCancelled = ? WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, 1);
        stmt.setString(2, cardNumber);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean updateExpiry(String cardNumber) throws SQLException {
        String query = "UPDATE CREDITCARDS SET Expiry = ? WHERE CardNumber = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = futureDate.format(formatter);
        stmt.setString(1, formattedDate);
        stmt.setString(2, cardNumber);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    }

    public boolean checkTicketCancelled(String ticketId) throws SQLException {
        String query = "SELECT IsCancelled FROM TICKETS WHERE TicketID = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, ticketId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.isBeforeFirst()) {
            return true;
        }
        rs.next();
        boolean cancelled = rs.getBoolean("IsCancelled");
        stmt.close();
        rs.close();
        return cancelled;
    }

    public boolean releaseSeatsFromTicketId(String ticketId) throws SQLException {
        String query = "UPDATE SHOWTIMESEATS SET IsCancelled = ? WHERE TicketID = ?";
        return false;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public static void main(String[] args) {
        try {
            // Connect to MySQL server (not to a specific database)
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // Step 1: Create the database
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS ACMEPLEX";
            stmt.executeUpdate(createDatabaseSQL);
            System.out.println("Database ACMEPLEX created successfully!");

            // Step 2: Use the ACMEPLEX database
            String useDatabaseSQL = "USE ACMEPLEX";
            stmt.executeUpdate(useDatabaseSQL);
            System.out.println("Switched to database ACMEPLEX!");

            // Step 3: Create the users table
            String createUsersTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    full_name VARCHAR(255),
                    address VARCHAR(255),
                    card_number VARCHAR(16),
                    csv VARCHAR(3),
                    expiry_date VARCHAR(5)
                )
            """;
            stmt.executeUpdate(createUsersTableSQL);
            System.out.println("Table 'users' created successfully!");

            // Step 4: Create other tables if needed (e.g., movies, theaters, reservations)
            String createMoviesTableSQL = """
                CREATE TABLE IF NOT EXISTS movies (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    genre VARCHAR(50),
                    duration INT,
                    rating FLOAT
                )
            """;
            stmt.executeUpdate(createMoviesTableSQL);
            System.out.println("Table 'movies' created successfully!");

            // Step 5: Insert some sample data (optional)
            String insertSampleUsersSQL = """
                INSERT INTO users (email, password, full_name, address, card_number, csv, expiry_date)
                VALUES ('john.doe@example.com', 'password123', 'John Doe', '123 Main St', '4111111111111111', '123', '12/25')
                ON DUPLICATE KEY UPDATE email=email
            """;
            stmt.executeUpdate(insertSampleUsersSQL);
            System.out.println("Sample user data inserted successfully!");

            // Close resources
            stmt.close();
            conn.close();
            System.out.println("Database setup complete!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
