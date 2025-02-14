package com.AcmePlex.Boundary;

import com.AcmePlex.Control.EmailController;
import com.AcmePlex.Control.SeatController;
import com.AcmePlex.Database.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class registeredSeatView extends JFrame {
    private JPanel contentPane;
    private JButton[][] seatButtons;
    private String selectedTheater;
    private String selectedMovie;
    private String selectedShowtime;
    private String formattedTicket;

    public registeredSeatView(String theater, String movie, String showtime, String email) {
        this.selectedTheater = theater;
        this.selectedMovie = movie;
        this.selectedShowtime = showtime;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Select Your Seats");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(5, 5, 10, 10));
        contentPane.add(seatPanel, BorderLayout.CENTER);

        JButton btnConfirm = new JButton("Confirm Selection");
        contentPane.add(btnConfirm, BorderLayout.SOUTH);

        // Create the grid of seats
        seatButtons = new JButton[5][5];
        initializeSeats(seatPanel);

        // Load seat availability
        loadSeatAvailability();

        // Confirm button action
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    confirmSelection(email);
                } catch (SQLException | ClassNotFoundException err) {
                    throw new RuntimeException(err);
                }
            }
        });
    }

    private void initializeSeats(JPanel seatPanel) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                seatButtons[i][j] = new JButton("Seat " + (i * 5 + j + 1));
                seatButtons[i][j].setBackground(Color.GREEN);
                seatButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton) e.getSource();
                        if (clickedButton.getBackground() == Color.GREEN) {
                            clickedButton.setBackground(Color.YELLOW);
                        } else {
                            clickedButton.setBackground(Color.GREEN);
                        }
                    }
                });
                seatPanel.add(seatButtons[i][j]);
            }
        }
    }

    private void loadSeatAvailability() {
        SeatController seatController = new SeatController();
        List<Integer> unavailableSeats = seatController.getUnavailableSeats(selectedTheater, selectedMovie, selectedShowtime);
        for (int seat : unavailableSeats) {
            int row = (seat - 1) / 5;
            int col = (seat - 1) % 5;
            seatButtons[row][col].setBackground(Color.RED);
            seatButtons[row][col].setEnabled(false);
        }
    }

    private void confirmSelection(String email) throws RuntimeException, SQLException, ClassNotFoundException {
        SeatController seatController = new SeatController();
        ArrayList<String> seats = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (seatButtons[i][j].isEnabled() && seatButtons[i][j].getBackground() == Color.YELLOW) {
                    int seatNumber = i * 5 + j + 1;
                    boolean seatsBooked = seatController.bookSeat(selectedTheater, selectedMovie, selectedShowtime, seatNumber);
                    if (seatsBooked) {
                        seats.add("Row: " + (i + 1) + ", Seat: " + seatNumber);
                    }
                }
            }
        }
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            int movieId = db_conn.getMovieIdFromName(selectedMovie);
            int theaterId = db_conn.getTheaterIdFromLocation(selectedTheater);
            int showtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, selectedShowtime);
            String date = db_conn.getDateFromShowtimeId(showtimeId);
            formattedTicket = EmailController.formatTicket(selectedMovie, selectedTheater, date, selectedShowtime, seats);
        } catch (SQLException error) {
            error.printStackTrace();
        }
        registeredCheckoutView checkoutFrame = new registeredCheckoutView(formattedTicket, seats, selectedMovie, selectedTheater, selectedShowtime, email, ttt.pricePerSeat * seats.size());
        checkoutFrame.setVisible(true);
        dispose();
    }
}