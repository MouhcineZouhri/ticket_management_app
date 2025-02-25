package org.example.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.ApiClient;
import org.example.NavigationManager;
import org.example.TicketTableManger;
import org.example.enums.Category;
import org.example.enums.Priority;
import org.example.models.TicketRequest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class CreateTicketView {
    private final JPanel panel;
    private final ApiClient apiClient;
    private final NavigationManager navigation;
    private final TicketTableManger tableManager;

    public CreateTicketView(ApiClient apiClient, NavigationManager navigation, TicketTableManger tableManager) {
        this.tableManager = tableManager;
        this.panel = new JPanel(new BorderLayout());
        this.apiClient = apiClient;
        this.navigation = navigation;

        initializeUI();
    }

    private void initializeUI() {
        // Back button panel
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> navigateToDashboard());
        northPanel.add(backButton);
        panel.add(northPanel, BorderLayout.NORTH);

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5); // Increased bottom inset for spacing
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form components
        JTextField titleField = new JTextField(25);
        JTextArea descriptionArea = new JTextArea(5, 25);
        JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
        JComboBox<Category> categoryCombo = new JComboBox<>(Category.values());
        JButton submitBtn = new JButton("Create Ticket");

        // Add components to form
        addFormRow(formPanel, gbc, 0, "Title:", titleField);
        addFormRow(formPanel, gbc, 1, "Description:", new JScrollPane(descriptionArea));
        addFormRow(formPanel, gbc, 2, "Priority:", priorityCombo);
        addFormRow(formPanel, gbc, 3, "Category:", categoryCombo);

        panel.add(formPanel, BorderLayout.CENTER);

        // Submit button panel
        JPanel southPanel = new JPanel();
        southPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        submitBtn.setPreferredSize(new Dimension(200, 30));
        submitBtn.addActionListener(e -> createTicket(titleField, descriptionArea, priorityCombo, categoryCombo));
        southPanel.add(submitBtn);
        panel.add(southPanel, BorderLayout.SOUTH);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(field, gbc);
    }

    private void createTicket(JTextField titleField, JTextArea descriptionArea,
                              JComboBox<Priority> priorityCombo, JComboBox<Category> categoryCombo) {
        TicketRequest request = TicketRequest.builder()
                .title(titleField.getText())
                .description(descriptionArea.getText())
                .priority((Priority) priorityCombo.getSelectedItem())
                .category((Category) categoryCombo.getSelectedItem())
                .build();

        try {
            apiClient.createTickets(request,
                    node -> navigateToDashboard(),
                    error -> JOptionPane.showMessageDialog(panel, "Error creating ticket: " + error)
            );
        } catch (JsonProcessingException ex) {
            JOptionPane.showMessageDialog(panel, "Error processing request");
        }
    }

    private void navigateToDashboard() {
        navigation.registerView("dashboard",
                new DashboardView(apiClient, navigation, new TicketTableManger(new ArrayList<>())).getPanel()
        );
        SwingUtilities.invokeLater(() -> navigation.showView("dashboard"));
    }

    public JPanel getPanel() {
        return panel;
    }
}