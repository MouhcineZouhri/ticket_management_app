package org.example.pages;

import org.apache.hc.core5.http.ProtocolException;
import org.example.ApiClient;
import org.example.NavigationManager;
import org.example.TicketTableManger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class LoginView {
    private final JPanel panel;
    private final ApiClient apiClient;
    private final NavigationManager navigation;

    public LoginView(ApiClient apiClient, NavigationManager navigation) {
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

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Create Account");

        // Add components to form panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        panel.add(formPanel);

        // Login action
        loginBtn.addActionListener(e -> performLogin(emailField, passwordField));

        // Register action
        registerBtn.addActionListener(e -> {
            navigation.registerView("register", new RegisterView(apiClient, navigation).getPanel());
            SwingUtilities.invokeLater(() -> navigation.showView("register"));
        });
    }

    private void performLogin(JTextField emailField, JPasswordField passwordField) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            apiClient.login(email, password,
                    response -> {
                        navigation.registerView("dashboard",
                                new DashboardView(apiClient, navigation, new TicketTableManger(new ArrayList<>())).getPanel());
                        SwingUtilities.invokeLater(() -> navigation.showView("dashboard"));
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