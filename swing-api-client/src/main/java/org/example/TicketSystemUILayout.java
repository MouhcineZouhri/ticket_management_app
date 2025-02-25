package org.example;

import org.example.pages.LoginView;

import javax.swing.*;

public class TicketSystemUILayout {
    private JFrame mainFrame;
    private NavigationManager navigation;
    private ApiClient apiClient;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketSystemUILayout().initialize());
    }

    private void initialize() {
        mainFrame = new JFrame("IT Support Ticket System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);

        apiClient = new ApiClient();
        navigation = new NavigationManager(mainFrame);
        setupViews();
        navigation.showView("login");
        mainFrame.setVisible(true);
    }

    private void setupViews() {
        navigation.registerView("login", new LoginView(apiClient, navigation).getPanel());
    }
}