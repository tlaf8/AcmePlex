package com.AcmePlex.Boundary;

import com.AcmePlex.Control.UserController;
import com.AcmePlex.Database.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Random;

public class signupinfo extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldEmail;
    private JTextField textFieldPassword;
    private JTextField textFieldFullName;
    private JTextField textFieldAddress;
    private JTextField textFieldCardNumber;
    private JTextField textFieldCSV;
    private JTextField textFieldExpiryDate;

    public signupinfo() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Acmeplex Signup");
        lblNewLabel.setBounds(6, 5, 353, 17);
        contentPane.add(lblNewLabel);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(371, 6, 79, 17);
        contentPane.add(btnBack);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(195, 35, 200, 26);
        contentPane.add(textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(66, 40, 61, 16);
        contentPane.add(lblEmail);

        textFieldPassword = new JTextField();
        textFieldPassword.setBounds(195, 70, 200, 26);
        contentPane.add(textFieldPassword);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(66, 75, 61, 16);
        contentPane.add(lblPassword);

        textFieldFullName = new JTextField();
        textFieldFullName.setBounds(195, 105, 200, 26);
        contentPane.add(textFieldFullName);

        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setBounds(66, 110, 100, 16);
        contentPane.add(lblFullName);

        textFieldAddress = new JTextField();
        textFieldAddress.setBounds(195, 140, 200, 26);
        contentPane.add(textFieldAddress);

        JLabel lblAddress = new JLabel("Address");
        lblAddress.setBounds(66, 145, 100, 16);
        contentPane.add(lblAddress);

        textFieldCardNumber = new JTextField();
        textFieldCardNumber.setBounds(195, 175, 200, 26);
        contentPane.add(textFieldCardNumber);

        JLabel lblCardNumber = new JLabel("Card Number");
        lblCardNumber.setBounds(66, 180, 100, 16);
        contentPane.add(lblCardNumber);

        textFieldCSV = new JTextField();
        textFieldCSV.setBounds(195, 210, 200, 26);
        contentPane.add(textFieldCSV);

        JLabel lblCSV = new JLabel("CSV");
        lblCSV.setBounds(66, 215, 100, 16);
        contentPane.add(lblCSV);

        textFieldExpiryDate = new JTextField();
        textFieldExpiryDate.setBounds(195, 245, 200, 26);
        contentPane.add(textFieldExpiryDate);

        JLabel lblExpiryDate = new JLabel("Expiry Date");
        lblExpiryDate.setBounds(66, 250, 100, 16);
        contentPane.add(lblExpiryDate);

        JButton btnSignup = new JButton("Signup");
        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Capture user input
                String email = textFieldEmail.getText();
                String password = textFieldPassword.getText();
                String fullName = textFieldFullName.getText();
                String address = textFieldAddress.getText();
                String cardNumber = textFieldCardNumber.getText();
                String csv = textFieldCSV.getText();
                String expiryDate = textFieldExpiryDate.getText();

                UserController userController = new UserController();
                Random rand = new Random();
                if (userController.signupUser(rand.nextInt(2147483647), email, password, fullName, address, cardNumber, csv, expiryDate)) {
                    JOptionPane.showMessageDialog(null, "Signup successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new signup().setVisible(true); // Redirect to login screen
                } else {
                    JOptionPane.showMessageDialog(null, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSignup.setBounds(195, 300, 100, 26);
        contentPane.add(btnSignup);

        btnBack.addActionListener(e -> {
            new signup().setVisible(true);
            dispose();
        });
    }
}

	
