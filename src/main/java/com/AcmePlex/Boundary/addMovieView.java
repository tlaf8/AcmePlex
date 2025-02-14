package com.AcmePlex.Boundary;

import com.AcmePlex.Control.MovieController;
import com.AcmePlex.Control.TheaterController;
import com.AcmePlex.Control.ShowtimeController;
import com.AcmePlex.Database.DatabaseConnection;
import com.AcmePlex.Entity.Theater;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class addMovieView extends JFrame {
    private JPanel contentPane;
    private JTextField titleField,showtimeField,dateField,lengthField;
    private JComboBox<String> theaterDropdown;

    public addMovieView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setBounds(50, 30, 100, 20);
        contentPane.add(lblTitle);

        titleField = new JTextField();
        titleField.setBounds(200, 30, 200, 20);
        contentPane.add(titleField);

        JLabel lblTheater = new JLabel("Theater Location:");
        lblTheater.setBounds(50, 60, 120, 20);
        contentPane.add(lblTheater);

        theaterDropdown = new JComboBox<>();
        theaterDropdown.setBounds(200, 60, 200, 20);
        contentPane.add(theaterDropdown);

        JLabel lblShowtime = new JLabel("Showtime:");
        lblShowtime.setBounds(50, 90, 100, 20);
        contentPane.add(lblShowtime);

        showtimeField = new JTextField("HH:MM:SS (24-hour format)");
        showtimeField.setBounds(200, 90, 200, 20);
        contentPane.add(showtimeField);

        JLabel lblDate = new JLabel("Date:");
        lblDate.setBounds(50, 120, 100, 20);
        contentPane.add(lblDate);

        dateField = new JTextField("YYYY/MM/DD");
        dateField.setBounds(200, 120, 200, 20);
        contentPane.add(dateField);

        JLabel lblLength = new JLabel("Length:");
        lblLength.setBounds(50, 150, 100, 20);
        contentPane.add(lblLength);

        lengthField = new JTextField("");
        lengthField.setBounds(200, 150, 200, 20);
        contentPane.add(lengthField);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(100, 300, 100, 30);
        contentPane.add(btnSave);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(300, 300, 100, 30);
        contentPane.add(btnBack);
        
        //load theater
        loadTheaters();
        
        // Save button action
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseConnection db_conn = new DatabaseConnection();
                    String title = titleField.getText().trim();
                    String theaterLocation = (String) theaterDropdown.getSelectedItem();
                    String showtime = showtimeField.getText().trim();
                    String date = dateField.getText().trim();

                    if (theaterLocation == null || theaterLocation.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please select a theater location.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int theaterId = db_conn.getTheaterIdFromLocation(theaterLocation);
                    int movieId = db_conn.getMovieIdFromName(title);

                    int showtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, showtime);
                    if (showtimeId > 0) {
                        System.err.println("A showtime for this movie at this location and time already exists.");
                        return;
                    }

                    MovieController movieController = new MovieController();
                    boolean movieAdded = movieController.addMovie(title, theaterLocation);

                    if (movieAdded) {
                        // Add showtime
                        ShowtimeController showtimeController = new ShowtimeController();
                        boolean showtimeAdded = showtimeController.addShowtime(title, theaterLocation, date, showtime);

                        if (showtimeAdded) {
                            JOptionPane.showMessageDialog(null, "Movie and showtime added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            new adminview().setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to add showtime. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add movie. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numeric values for duration and rating.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Back button action
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
        theaterDropdown.removeAllItems();
        for (Theater theater : theaters) {
            theaterDropdown.addItem(theater.getLocation());
        }
    }
}
