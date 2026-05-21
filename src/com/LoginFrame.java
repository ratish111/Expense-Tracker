package com;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");

        userLabel.setFont(font);
        passLabel.setFont(font);
        userField.setFont(font);
        passField.setFont(font);

        // USER
        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        add(userField, gbc);

        // PASS
        gbc.gridx = 0; gbc.gridy = 1;
        add(passLabel, gbc);

        gbc.gridx = 1;
        add(passField, gbc);

        // BUTTON
        gbc.gridx = 1; gbc.gridy = 2;
        add(loginBtn, gbc);

        // LOGIN LOGIC
        loginBtn.addActionListener(e -> {

            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if(user.equals("admin") && pass.equals("1234")) {
                new ExpenseTrackerGUI().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login!");
            }
        });
    }
}

