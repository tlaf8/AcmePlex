package com.AcmePlex.Control;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.AcmePlex.Database.DatabaseConnection;
import com.AcmePlex.Entity.Showtime;

public class ShowtimeController {
    public List<Showtime> getShowtimesByMovie(String movieTitle) {
        List<Showtime> showtimes = new ArrayList<>();
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int movieId = db_conn.getMovieIdFromName(movieTitle);

            String query_showtime = "SELECT ShowtimeID, SDate, StartTime FROM SHOWTIMES WHERE MovieID = ?";
            PreparedStatement stmt_showtime = db_conn.getConnection().prepareStatement(query_showtime);
            stmt_showtime.setString(1, String.valueOf(movieId));
            ResultSet rs_showtime = stmt_showtime.executeQuery();

            while (rs_showtime.next()) {
                showtimes.add(new Showtime(rs_showtime.getInt("ShowtimeID"), movieTitle, rs_showtime.getString("SDate"), rs_showtime.getString("StartTime")));
            }

            rs_showtime.close();
            stmt_showtime.close();
            db_conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showtimes;
    }
    
    public boolean addShowtime(String movieTitle, String theaterLocation, String date, String showtime) {
        try {
            boolean successful = true;
            DatabaseConnection db_conn = new DatabaseConnection();

            int movieId = db_conn.getMovieIdFromName(movieTitle);
            int theaterId = db_conn.getTheaterIdFromLocation(theaterLocation);

            String showtimeQuery = "INSERT INTO SHOWTIMES (MovieID, TheaterID, SDate, StartTime) VALUES (?, ?, ?, ?)";
            PreparedStatement showtimeStmt = db_conn.getConnection().prepareStatement(showtimeQuery);
            showtimeStmt.setInt(1, movieId);
            showtimeStmt.setInt(2, theaterId);
            showtimeStmt.setString(3, date);
            showtimeStmt.setString(4, showtime);
            if (showtimeStmt.executeUpdate() == 0) successful = false;
            showtimeStmt.close();

            int numRows = 5;
            int numCols = 5;
            int newShowtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, showtime);
            String seatQuery = "INSERT INTO SHOWTIMESEATS (ShowtimeID, SeatID, IsAvailable) VALUES (?, ?, ?)";
            PreparedStatement seatStmt = db_conn.getConnection().prepareStatement(seatQuery);
            db_conn.getConnection().setAutoCommit(false);
            for(int i = 0; i < numRows; i++) {
                for(int j = 0; j < numCols; j++) {
                    seatStmt.setInt(1, newShowtimeId);
                    seatStmt.setInt(2, (i * numRows) + j);
                    seatStmt.setBoolean(3, true);
                    seatStmt.addBatch();
                }
            }

            int[] seatsAdded = seatStmt.executeBatch();
            for(int i : seatsAdded) successful = i != 0;

            db_conn.getConnection().setAutoCommit(true);
            seatStmt.close();
            db_conn.close();

            return successful;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteShowtime(String theater, String movieName, String showtime) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int movieId = db_conn.getMovieIdFromName(movieName);
            int theaterId = db_conn.getTheaterIdFromLocation(theater);
            String query = "DELETE FROM SHOWTIMES WHERE MovieID = ? AND TheaterID = ? AND StartTime = ?";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            stmt.setInt(1, movieId);
            stmt.setInt(2, theaterId);
            stmt.setString(3, showtime);

            int rowsDeleted = stmt.executeUpdate();
            stmt.close();

            return rowsDeleted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
