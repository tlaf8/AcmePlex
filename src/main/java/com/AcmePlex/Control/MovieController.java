package com.AcmePlex.Control;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.AcmePlex.Entity.Movie;
import com.AcmePlex.Database.DatabaseConnection;

public class MovieController {

    /**
     * Fetch movies by theater location.
     *
     * @param theaterLocation The location of the theater.
     * @return List of movies available at the given theater.
     */
    public List<Movie> getMoviesByTheater(String theaterLocation) {
        List<Movie> movies = new ArrayList<>();
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int theaterId = db_conn.getTheaterIdFromLocation(theaterLocation);

            String query = "SELECT MovieID, Title FROM MOVIES WHERE TheaterID = ?";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            stmt.setInt(1, theaterId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(new Movie(rs.getInt("MovieID"), rs.getString("Title"), theaterLocation));
            }

            rs.close();
            stmt.close();
            db_conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Add a new movie to the database.
     *
     * @param title    The title of the movie.
     * @param genre    The genre of the movie.
     * @param duration The duration of the movie in minutes.
     * @param rating   The rating of the movie.
     * @return true if the movie was added successfully, false otherwise.
     */
    public boolean addMovie(String title, String theaterLocation) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();

            int theaterId = db_conn.getTheaterIdFromLocation(theaterLocation);
            if (db_conn.getMovieIdFromName(title) > 0) {
                return true;
            }
            String query = "INSERT INTO MOVIES (Title, TheaterID) VALUES (?, ?)";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            stmt.setString(1, title);
            stmt.setInt(2, theaterId);

            int rowsInserted = stmt.executeUpdate();

            stmt.close();
            db_conn.close();

            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Delete a movie from the database by title.
     *
     * @param title The title of the movie to delete.
     * @return true if the movie was deleted successfully, false otherwise.
     */
    public boolean deleteMovie(String title) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();

            String movieQuery = "DELETE FROM MOVIES WHERE Title = ?";
            PreparedStatement movieStmt = db_conn.getConnection().prepareStatement(movieQuery);
            movieStmt.setString(1, title);
            int moviesDeleted = movieStmt.executeUpdate();
            movieStmt.close();

            movieStmt.close();
            db_conn.close();

            return moviesDeleted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

