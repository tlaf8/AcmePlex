package com.AcmePlex.Control;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import com.AcmePlex.Database.DatabaseConnection;

public class TicketController {

    /**
     * Check if the ticket is eligible for cancellation.
     * @param ticketID The ticket ID.
     * @return true if eligible for cancellation, false otherwise.
     */
    public boolean isRefundEligible(String ticketId) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int showtimeId = db_conn.getShowtimeIdFromTicket(ticketId);
            String showtime = db_conn.getDateFromShowtimeId(showtimeId);
            LocalDateTime showtimeDateTime = LocalDateTime.parse(showtime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            db_conn.close();

            // Check if cancellation is 72+ hours before showtime
            LocalDateTime now = LocalDateTime.now();
            if (now.plusHours(72).isBefore(showtimeDateTime)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Process refund and in-store credit for a ticket.
     * @param ticketID The ticket ID.
     * @param ticketPrice The total price of the ticket.
     * @return true if successful, false otherwise.
     */
    public boolean processRefund(boolean is_registered, String ticketID) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            if (db_conn.checkTicketCancelled(ticketID)) {
                throw new RuntimeException("Ticket is cancelled or is not valid");
            }

            String cardNumber = db_conn.getCCFromTicketID(ticketID);
            double ticketPrice = db_conn.getPriceFromTicketID(ticketID);
            double remaining = db_conn.getAmountRemaining(cardNumber);
            db_conn.invalidateTicket(cardNumber);
            db_conn.updateExpiry(cardNumber);
            if (is_registered) {
                db_conn.setAmountRemaining(cardNumber, remaining + ticketPrice);
            } else {
                // Refund 85% of the ticket price
                double refundAmount = ticketPrice * 0.85;

                // Add 15% as in-store credit with a 1-year expiration
                double creditAmount = ticketPrice * 0.15;
                db_conn.setAmountRemaining(cardNumber, remaining + refundAmount);
                db_conn.setInStoreCreditRemaining(cardNumber, creditAmount);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean storeTicket(int ticketId, int showtimeId, ArrayList<String> seats, int ou, int ru, String cardNumber, double total) throws SQLException, ClassNotFoundException {
        DatabaseConnection db_conn = new DatabaseConnection();
        String query = "INSERT INTO TICKETS (TicketID, ShowtimeID, Seats, OrdinaryUserID, RegisteredUserID, Price, CardNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ticketStmt = db_conn.getConnection().prepareStatement(query);
        ticketStmt.setInt(1, ticketId);
        ticketStmt.setInt(2, showtimeId);
        ticketStmt.setString(3, seats.toString());
        ticketStmt.setInt(4, ou);
        ticketStmt.setInt(5, ru);
        ticketStmt.setDouble(6, total);
        ticketStmt.setString(7, cardNumber);
        return ticketStmt.executeUpdate() > 0;
    }
}