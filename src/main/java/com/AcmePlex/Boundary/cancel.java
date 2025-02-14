package com.AcmePlex.Boundary;

import com.AcmePlex.Control.TicketController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class cancel extends JFrame {

    private JPanel contentPane;
    private JTextField ticketIDField;

    public cancel(boolean is_registered, String email) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 250);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Cancel Booking");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(100, 10, 200, 30);
        contentPane.add(lblTitle);

        JLabel lblTicketID = new JLabel("Enter Ticket ID:");
        lblTicketID.setBounds(50, 70, 120, 20);
        contentPane.add(lblTicketID);

        ticketIDField = new JTextField();
        ticketIDField.setBounds(180, 70, 150, 20);
        contentPane.add(ticketIDField);
        ticketIDField.setColumns(10);

        JButton btnCancel = new JButton("Cancel Ticket");
        btnCancel.setBounds(50, 150, 120, 30);
        contentPane.add(btnCancel);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(210, 150, 120, 30);
        contentPane.add(btnBack);

        // Cancel button action listener
        btnCancel.addActionListener(e -> processCancellation(is_registered, email));

        // Back button action listener
        btnBack.addActionListener(e -> {
            if (is_registered) {
                new registeredview(email).setVisible(true);
                dispose();
            } else {
                new ttt().setVisible(true);
                dispose();
            }
        });
    }

    private void processCancellation(boolean is_registered, String email) {
        String ticketID = ticketIDField.getText().trim();

        if (ticketID.isEmpty()) {
            JOptionPane.showMessageDialog(contentPane, "Please enter a valid Ticket ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TicketController ticketController = new TicketController();
        try {
            // Check if the ticket is eligible for cancellation
            boolean isEligible = ticketController.isRefundEligible(ticketID);
            if (!isEligible) {
                JOptionPane.showMessageDialog(contentPane, "Cancellation is not allowed less than 72 hours before showtime.", "Refund Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Process the refund and in-store credit
            boolean success = ticketController.processRefund(is_registered, ticketID); // Assuming ticket price is $15.00
            if (success) {
                if (is_registered) {
                    JOptionPane.showMessageDialog(contentPane, "Ticket cancelled successfully. Full refund issued.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new registeredview(email).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Ticket cancelled successfully. 85% refunded and 15% stored as in-store credit.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new ttt().setVisible(true);
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(contentPane, "Failed to cancel the ticket. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(contentPane, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}