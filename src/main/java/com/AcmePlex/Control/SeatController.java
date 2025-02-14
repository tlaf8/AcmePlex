package com.AcmePlex.Control;

import com.AcmePlex.Database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SeatController {

    /**
     * Fetch unavailable seats for a specific theater, movie, and showtime.
     */
    public List<Integer> getUnavailableSeats(String theater, String movieTitle, String showtime) {
        List<Integer> unavailableSeats = new ArrayList<>();

        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int theaterId = db_conn.getTheaterIdFromLocation(theater);
            int movieId = db_conn.getMovieIdFromName(movieTitle);
            int showtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, showtime);

            String query_seats = "SELECT SeatID FROM SHOWTIMESEATS WHERE ShowtimeID = ? AND IsAvailable = FALSE";
            PreparedStatement stmt_seats = db_conn.getConnection().prepareStatement(query_seats);
            stmt_seats.setInt(1, showtimeId);
            ResultSet rs = stmt_seats.executeQuery();
            while (rs.next()) {
                unavailableSeats.add(rs.getInt("SeatID"));
            }
            rs.close();
            stmt_seats.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return unavailableSeats;
    }

    /**
     * Book a seat for a specific theater, movie, and showtime.
     */
    public boolean bookSeat(String theater, String movie, String showtime, int seatNumber) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int theaterId = db_conn.getTheaterIdFromLocation(theater);
            int movieId = db_conn.getMovieIdFromName(movie);
            int showtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, showtime);
            String query = "UPDATE SHOWTIMESEATS SET IsAvailable = 0 WHERE ShowtimeID = ? AND SeatID = ?";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            stmt.setInt(1, showtimeId);
            stmt.setInt(2, seatNumber);
            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
