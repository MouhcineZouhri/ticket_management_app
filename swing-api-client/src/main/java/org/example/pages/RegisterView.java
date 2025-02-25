package org.example.pages;

import org.apache.hc.core5.http.ProtocolException;
import org.example.ApiClient;
import org.example.NavigationManager;
import org.example.models.UserCreateRequest;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class RegisterView {
    private final JPanel panel;
    private final ApiClient apiClient;
    private final NavigationManager navigation;

    public RegisterView(ApiClient apiClient, NavigationManager navigation) {
        this.apiClient = apiClient;
        this.navigation = navigation;
        panel = new JPanel(new GridBagLayout());
        initializeUI();
    }

    private void initializeUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        // Form layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        buttonPanel.add(backBtn);
        buttonPanel.add(registerBtn);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        panel.add(formPanel);

        // Register action
        registerBtn.addActionListener(e -> {
            UserCreateRequest request = UserCreateRequest.builder()
                    .name(nameField.getText())
                    .email(emailField.getText())
                    .password(passwordField.getText())
                    .confirmPassword(confirmPasswordField.getText())
                    .build();

            performRegistration(request);
        });

        // Back action
        backBtn.addActionListener(e ->
                SwingUtilities.invokeLater(() -> navigation.showView("login"))
        );
    }

    private void performRegistration(UserCreateRequest userCreateRequest) {
        if (!userCreateRequest.getPassword().equals(userCreateRequest.getConfirmPassword())) {
            JOptionPane.showMessageDialog(panel, "Passwords do not match!");
            return;
        }

        if (userCreateRequest.getName().isEmpty() || userCreateRequest.getEmail().isEmpty() || userCreateRequest.getPassword().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Please fill all fields!");
            return;
        }

        try {
            apiClient.register(userCreateRequest,
                    response -> {
                        JOptionPane.showMessageDialog(panel, "Registration successful!");
                        SwingUtilities.invokeLater(() -> navigation.showView("login"));
                    },
                    error -> JOptionPane.showMessageDialog(panel, error)
            );
        } catch (ProtocolException | IOException ex) {
            JOptionPane.showMessageDialog(panel, "Connection error: " + ex.getMessage());
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}