package com.AcmePlex.Boundary;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.AcmePlex.Control.MovieController;
import com.AcmePlex.Control.ShowtimeController;
import com.AcmePlex.Control.TheaterController;
import com.AcmePlex.Entity.Movie;
import com.AcmePlex.Entity.Showtime;
import com.AcmePlex.Entity.Theater;

import java.awt.EventQueue;
import java.util.List;

public class ttt extends JFrame {
    private JPanel contentPane;
    private JComboBox<String> theaterComboBox;
    private JComboBox<String> movieComboBox;
    private JComboBox<String> showtimeComboBox;
    public static final double pricePerSeat = 15.00;
    public static final double annualFee = 20.00;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ttt frame = new ttt();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ttt() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTheater = new JLabel("Select a Theater:");
        lblTheater.setBounds(50, 30, 150, 20);
        contentPane.add(lblTheater);

        theaterComboBox = new JComboBox<>();
        theaterComboBox.setBounds(200, 30, 300, 20);
        contentPane.add(theaterComboBox);

        JLabel lblMovie = new JLabel("Select a Movie:");
        lblMovie.setBounds(50, 80, 150, 20);
        contentPane.add(lblMovie);

        movieComboBox = new JComboBox<>();
        movieComboBox.setBounds(200, 80, 300, 20);
        contentPane.add(movieComboBox);

        JLabel lblShowtime = new JLabel("Select a Showtime:");
        lblShowtime.setBounds(50, 130, 150, 20);
        contentPane.add(lblShowtime);

        showtimeComboBox = new JComboBox<>();
        showtimeComboBox.setBounds(200, 130, 300, 20);
        contentPane.add(showtimeComboBox);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(400, 300, 100, 30);
        contentPane.add(btnLogin);
        
        JButton btnAdmin = new JButton("Admin");
        btnAdmin.setBounds(300, 200, 100, 30);
        contentPane.add(btnAdmin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(200, 200, 100, 30);
        contentPane.add(btnCancel);

        JButton btnSelectSeat = new JButton("Select Seat");
        btnSelectSeat.setBounds(400, 200, 100, 30);
        contentPane.add(btnSelectSeat);

        // Load theaters into the theaterComboBox
        loadTheaters();

        // Add listener to theaterComboBox to load movies
        theaterComboBox.addActionListener(e -> loadMovies());

        // Add listener to movieComboBox to load showtimes
        movieComboBox.addActionListener(e -> loadShowtimes());

        btnCancel.addActionListener(e -> {
            cancel cl = new cancel(false, "");
            cl.setVisible(true);
            dispose();
        });
        
        btnAdmin.addActionListener(e -> {
        	adminKeyLoginView adminValidation = new adminKeyLoginView();
        	adminValidation.setVisible(true);
        	dispose();
        });
        
        // Add listener to the login button
        btnLogin.addActionListener(e -> {
        	signup signupFrame = new signup();
            signupFrame.setVisible(true);
            dispose();
        });

        btnSelectSeat.addActionListener(e -> {
            String selectedTheater = (String) theaterComboBox.getSelectedItem();
            String selectedMovie = (String) movieComboBox.getSelectedItem();
            String selectedShowtime = (String) showtimeComboBox.getSelectedItem();

            if (selectedTheater != null && selectedMovie != null && selectedShowtime != null) {
                seatView seatFrame = new seatView(selectedTheater, selectedMovie, selectedShowtime);
                seatFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a theater, movie, and showtime.", "Error", JOptionPane.ERROR_MESSAGE);
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


