package com.AcmePlex.Boundary;

import com.AcmePlex.Control.EmailController;
import com.AcmePlex.Control.TicketController;
import com.AcmePlex.Database.DatabaseConnection;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class registeredCheckoutView extends JFrame {

    private JPanel contentPane;

    public registeredCheckoutView(String formattedTicket, ArrayList<String> seats, String movieName, String theaterLocation, String showtime, String email, double total) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Checkout");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(50, 10, 300, 30);
        contentPane.add(lblTitle);

        JLabel lblPrice = new JLabel("Total Price: $" + Math.floor(total * 100) / 100);
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPrice.setBounds(50, 60, 300, 20);
        contentPane.add(lblPrice);

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.setBounds(50, 120, 100, 30);
        contentPane.add(btnConfirm);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(220, 120, 100, 30);
        contentPane.add(btnBack);

        // Confirm button action
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseConnection db_conn = new DatabaseConnection();
                    EmailController emailController = new EmailController();
                    TicketController ticketController = new TicketController();

                    String userId = db_conn.getUserIDFromEmail(email);
                    String cardNumber = db_conn.getCCFromID(userId);

                    double currentCredit = db_conn.getAmountRemaining(cardNumber);
                    if (currentCredit < total) {
                        JOptionPane.showMessageDialog(null, "Insufficient funds", "Failed", JOptionPane.INFORMATION_MESSAGE);
                        throw new RuntimeException("Insufficient funds");
                    }

                    db_conn.setAmountRemaining(cardNumber, currentCredit - total);
                    String ticketId = formattedTicket.substring(formattedTicket.indexOf('[') + 1, formattedTicket.indexOf(']'));
                    int theaterId = db_conn.getTheaterIdFromLocation(theaterLocation);
                    int movieId = db_conn.getMovieIdFromName(movieName);
                    int showtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, showtime);
                    ticketController.storeTicket(Integer.parseInt(ticketId), showtimeId, seats, 0, Integer.parseInt(userId), cardNumber, total);
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);
                    String formattedReceipt = EmailController.formatReceipt(formattedTicket, cardNumber, String.valueOf(Math.floor(total * 100) / 100), formattedDateTime);
                    emailController.send_email(email, "AcmePlex Ticket", formattedReceipt);
                } catch (MessagingException | SQLException | ClassNotFoundException error) {
                    error.printStackTrace();
                }
                JOptionPane.showMessageDialog(contentPane, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new registeredview(email).setVisible(true); // Redirect to main menu
                dispose();
            }
        });

        // Back button action
        btnBack.addActionListener(e -> {
            new registeredview(email).setVisible(true); // Redirect to main menu
            dispose();
        });
    }

    /**
     * Handles the payment confirmation.
     */
    private void confirmPayment(ActionEvent e) {

    }
}