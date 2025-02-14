package com.AcmePlex.Boundary;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.AcmePlex.Control.EmailController;
import com.AcmePlex.Control.TicketController;
import com.AcmePlex.Control.UserController;
import com.AcmePlex.Database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class checkout extends JFrame {

    private JPanel contentPane;
    private JTextField emailField;
    private JTextField cardField;
    private JTextField csvField;
    private JTextField expDateField;

    public checkout(String formattedTicket, ArrayList<String> seats, String movieName, String theaterLocation, String showtime, double total) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Checkout");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(100, 10, 200, 30);
        contentPane.add(lblTitle);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 60, 100, 20);
        contentPane.add(lblEmail);

        emailField = new JTextField();
        emailField.setBounds(150, 60, 200, 20);
        contentPane.add(emailField);
        emailField.setColumns(10);

        JLabel lblCard = new JLabel("Credit Card:");
        lblCard.setBounds(50, 90, 100, 20);
        contentPane.add(lblCard);

        cardField = new JTextField();
        cardField.setBounds(150, 90, 200, 20);
        contentPane.add(cardField);
        cardField.setColumns(16);

        JLabel lblCSV = new JLabel("CSV:");
        lblCSV.setBounds(50, 120, 100, 20);
        contentPane.add(lblCSV);

        csvField = new JTextField();
        csvField.setBounds(150, 120, 200, 20);
        contentPane.add(csvField);
        csvField.setColumns(3);

        JLabel lblExpDate = new JLabel("Exp Date (MM/YY):");
        lblExpDate.setBounds(50, 150, 120, 20);
        contentPane.add(lblExpDate);

        expDateField = new JTextField();
        expDateField.setBounds(150, 150, 200, 20);
        contentPane.add(expDateField);
        expDateField.setColumns(5);

        JLabel lblTotalPrice = new JLabel("Total Price: $" + Math.floor(total * 100) / 100);
        lblTotalPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTotalPrice.setBounds(50, 180, 300, 20);
        contentPane.add(lblTotalPrice);

        JButton btnConfirm = new JButton("Confirm Payment");
        btnConfirm.setBounds(50, 220, 150, 30);
        contentPane.add(btnConfirm);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(220, 220, 100, 30);
        contentPane.add(btnBack);

        // Confirm payment button action
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Capture user input
                String email = emailField.getText();
                String cardNumber = cardField.getText();
                String csv = csvField.getText();
                String expiryDate = expDateField.getText();

                UserController userController = new UserController();
                if (userController.isSignupValid(email, cardNumber, csv, expiryDate)) {
                    try {
                        DatabaseConnection db_conn = new DatabaseConnection();
                        EmailController emailController = new EmailController();
                        TicketController ticketController = new TicketController();
                        Random rand = new Random();
                        int newId = rand.nextInt(2147483647);

                        if (!db_conn.ccExists(cardNumber)) {
                            userController.signupUser(-newId, email, "", "", "", cardNumber, csv, expiryDate);
                        }

                        double currentCredit = db_conn.getAmountRemaining(cardNumber);
                        double currentInStoreCredit = db_conn.getRemainingInStoreCredit(cardNumber);
                        if (currentCredit + currentInStoreCredit < total) {
                            JOptionPane.showMessageDialog(null, "Insufficient funds", "Failed", JOptionPane.INFORMATION_MESSAGE);
                            throw new RuntimeException("Insufficient funds");
                        }

                        double currentInStore = db_conn.getRemainingInStoreCredit(cardNumber);
                        // TODO: change current
                        db_conn.setAmountRemaining(cardNumber, currentCredit + currentInStoreCredit - total);
                        String ticketId = formattedTicket.substring(formattedTicket.indexOf('[') + 1, formattedTicket.indexOf(']'));
                        int theaterId = db_conn.getTheaterIdFromLocation(theaterLocation);
                        int movieId = db_conn.getMovieIdFromName(movieName);
                        int showtimeId = db_conn.getShowtimeIdFromTime(theaterId, movieId, showtime);
                        ticketController.storeTicket(Integer.parseInt(ticketId), showtimeId, seats, -newId, 0, cardNumber, total);
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = now.format(formatter);
                        String formattedReceipt = EmailController.formatReceipt(formattedTicket, cardNumber, String.valueOf(Math.floor(total * 100) / 100), formattedDateTime);
                        emailController.send_email(email, "AcmePlex Ticket", formattedReceipt);
                    } catch (MessagingException | SQLException | ClassNotFoundException error) {
                        error.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new ttt().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back button action
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ttt().setVisible(true); // Redirect back to main menu
                dispose();
            }
        });
    }
}