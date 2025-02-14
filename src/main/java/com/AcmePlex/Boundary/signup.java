package com.AcmePlex.Boundary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.AcmePlex.Control.UserController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class signup extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_2;

    public signup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Acmeplex");
        lblNewLabel.setBounds(6, 5, 353, 17);
        contentPane.add(lblNewLabel);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(371, 6, 79, 17);
        contentPane.add(btnBack);

        textField = new JTextField();
        textField.setBounds(121, 94, 238, 26);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Email");
        lblNewLabel_1.setBounds(121, 66, 122, 16);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Password");
        lblNewLabel_1_1.setBounds(121, 132, 61, 16);
        contentPane.add(lblNewLabel_1_1);

        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(121, 151, 238, 26);
        contentPane.add(textField_2);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textField.getText();
                String password = textField_2.getText();

                UserController userController = new UserController();
                if (userController.validateLogin(username, password)) {
                    // Open registeredview if login is successful
                    registeredview registeredView = new registeredview(username);
                    registeredView.setVisible(true);
                    dispose();
                } else {
                    // Show error message
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnLogin.setBounds(195, 189, 79, 17);
        contentPane.add(btnLogin);

        JLabel lblNewLabel_1_2 = new JLabel("Don't have an account?");
        lblNewLabel_1_2.setBounds(162, 218, 166, 16);
        contentPane.add(lblNewLabel_1_2);

        JButton btnSignup = new JButton("Signup");
        btnSignup.setBounds(195, 246, 79, 17);
        contentPane.add(btnSignup);

        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signupinfo signupInfoFrame = new signupinfo();
                signupInfoFrame.setVisible(true);
                dispose();
            }
        });

        btnBack.addActionListener(e -> {
            ttt mainFrame = new ttt();
            mainFrame.setVisible(true);
            dispose();
        });
    }
}
