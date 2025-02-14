package com.AcmePlex.Boundary;

import com.AcmePlex.Control.AdminController;
import com.AcmePlex.Boundary.ttt;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class adminKeyLoginView extends JFrame {
    private JPanel contentPane;
    private JTextField adminKeyField;

    public adminKeyLoginView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Admin Key Login");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(50, 10, 300, 30);
        contentPane.add(lblTitle);

        JLabel lblAdminKey = new JLabel("Enter Admin Key:");
        lblAdminKey.setBounds(50, 70, 120, 20);
        contentPane.add(lblAdminKey);

        adminKeyField = new JTextField();
        adminKeyField.setBounds(180, 70, 150, 20);
        contentPane.add(adminKeyField);
        adminKeyField.setColumns(10);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(50, 120, 100, 30);
        contentPane.add(btnLogin);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(220, 120, 100, 30);
        contentPane.add(btnBack);

        // Login button action
        btnLogin.addActionListener(e -> processAdminLogin());

        // Back button action
        btnBack.addActionListener(e -> {
            new ttt().setVisible(true); // Redirect to main menu
            dispose();
        });
    }

    private void processAdminLogin() {
        String adminKey = adminKeyField.getText().trim();

        if (adminKey.isEmpty()) {
            JOptionPane.showMessageDialog(contentPane, "Please enter an admin key.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AdminController adminController = new AdminController();
        if (adminController.validateAdminKey(adminKey)) {
            JOptionPane.showMessageDialog(contentPane, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new adminview().setVisible(true); // Redirect to admin view
            dispose();
        } else {
            JOptionPane.showMessageDialog(contentPane, "Invalid admin key. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}