package com.AcmePlex.Boundary;

import com.AcmePlex.Control.MovieController;
import com.AcmePlex.Control.ShowtimeController;
import com.AcmePlex.Control.TheaterController;
import com.AcmePlex.Entity.Movie;
import com.AcmePlex.Entity.Showtime;
import com.AcmePlex.Entity.Theater;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class deleteMovieView extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> theaterComboBox;
    private JComboBox<String> movieComboBox;
    private JComboBox<String> showtimeComboBox;

    public deleteMovieView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTheater = new JLabel("Select a Theater:");
        lblTheater.setBounds(50, 30, 150, 20);
        contentPane.add(lblTheater);

        theaterComboBox = new JComboBox<>();
        theaterComboBox.setBounds(200, 30, 250, 20);
        contentPane.add(theaterComboBox);

        JLabel lblMovie = new JLabel("Select Movie to Delete:");
        lblMovie.setBounds(50, 80, 150, 20);
        contentPane.add(lblMovie);

        movieComboBox = new JComboBox<>();
        movieComboBox.setBounds(200, 80, 250, 20);
        contentPane.add(movieComboBox);

        JLabel lblShowtime = new JLabel("Select Showtime to Delete:");
        lblShowtime.setBounds(50, 130, 150, 20);
        contentPane.add(lblShowtime);

        showtimeComboBox = new JComboBox<>();
        showtimeComboBox.setBounds(200, 130, 250, 20);
        contentPane.add(showtimeComboBox);

        JButton btnDeleteMovie = new JButton("Delete Movie");
        btnDeleteMovie.setBounds(50, 200, 150, 30);
        contentPane.add(btnDeleteMovie);

        JButton btnDeleteShowtime = new JButton("Delete Showtime");
        btnDeleteShowtime.setBounds(250, 200, 150, 30);
        contentPane.add(btnDeleteShowtime);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(200, 280, 100, 30);
        contentPane.add(btnBack);

        loadTheaters();

        // Add listeners
        theaterComboBox.addActionListener(e -> loadMovies());
        movieComboBox.addActionListener(e -> loadShowtimes());

        // Action listener for deleting a movie
        btnDeleteMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedShowtime = (String) showtimeComboBox.getSelectedItem();
                String selectedTheater = (String) theaterComboBox.getSelectedItem();
                String selectedMovie = (String) movieComboBox.getSelectedItem();
                if (selectedMovie == null || selectedMovie.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No movie selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirm deletion
                int confirmation = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete the movie \"" + selectedMovie + "\"?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    MovieController movieController = new MovieController();
                    boolean isDeleted = movieController.deleteMovie(selectedMovie);

                    if (isDeleted) {
                        JOptionPane.showMessageDialog(null, "Movie deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadMovies(); // Refresh the dropdowns after deletion
                        showtimeComboBox.removeAllItems(); // Clear showtimes since the movie is gone
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete movie. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action listener for deleting a showtime
        btnDeleteShowtime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedShowtime = (String) showtimeComboBox.getSelectedItem();
                String selectedTheater = (String) theaterComboBox.getSelectedItem();
                String selectedMovie = (String) movieComboBox.getSelectedItem();
                if (selectedShowtime == null || selectedShowtime.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No showtime selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirm deletion
                int confirmation = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete the showtime \"" + selectedShowtime + "\"?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    ShowtimeController showtimeController = new ShowtimeController();
                    boolean isDeleted = showtimeController.deleteShowtime(selectedTheater, selectedMovie, selectedShowtime);

                    if (isDeleted) {
                        JOptionPane.showMessageDialog(null, "Showtime deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadShowtimes(); // Refresh showtimes after deletion
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete showtime. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Back button action listener
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new adminview().setVisible(true);
                dispose();
            }
        });
    }

    private void loadTheaters() {
        TheaterController theaterController = new TheaterController();
        List<Theater> theaters = theaterController.getAllTheaters();
        theaterComboBox.removeAllItems();
        for (Theater theater : theaters) {
            theaterComboBox.addItem(theater.getLocation());
        }
    }

    private void loadMovies() {
        String selectedTheater = (String) theaterComboBox.getSelectedItem();
        if (selectedTheater != null) {
            MovieController movieController = new MovieController();
            List<Movie> movies = movieController.getMoviesByTheater(selectedTheater);
            movieComboBox.removeAllItems();
            for (Movie movie : movies) {
                movieComboBox.addItem(movie.getTitle());
            }
        }
    }

    private void loadShowtimes() {
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        if (selectedMovie != null) {
            ShowtimeController showtimeController = new ShowtimeController();
            List<Showtime> showtimes = showtimeController.getShowtimesByMovie(selectedMovie);
            showtimeComboBox.removeAllItems();
            for (Showtime showtime : showtimes) {
                showtimeComboBox.addItem(showtime.getTime());
            }
        }
    }
}
