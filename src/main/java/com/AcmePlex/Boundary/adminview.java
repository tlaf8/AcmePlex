package com.AcmePlex.Boundary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class adminview extends JFrame {

    private JPanel contentPane;

    public adminview() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Title label
        JLabel lblTitle = new JLabel("Admin Dashboard");
        lblTitle.setBounds(200, 20, 200, 30);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle);

        // Movie management section
        JLabel lblMovieManagement = new JLabel("Manage Movies:");
        lblMovieManagement.setBounds(50, 70, 150, 20);
        contentPane.add(lblMovieManagement);

        JButton btnAddMovie = new JButton("Add Movie");
        btnAddMovie.setBounds(50, 100, 150, 30);
        contentPane.add(btnAddMovie);

        JButton btnDeleteMovie = new JButton("Delete Movie");
        btnDeleteMovie.setBounds(50, 180, 150, 30);
        contentPane.add(btnDeleteMovie);

        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(50, 300, 100, 30);
        contentPane.add(btnBack);

        // Add action listeners for buttons
        btnAddMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new addMovieView().setVisible(true); // Open Add Movie View
                dispose();
            }
        });

        btnDeleteMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new deleteMovieView().setVisible(true); // Open Delete Movie View
                dispose();
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Return to the main view or login screen
                new ttt().setVisible(true);
                dispose();
            }
        });
    }
}
