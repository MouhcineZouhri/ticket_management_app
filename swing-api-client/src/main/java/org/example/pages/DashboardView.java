package org.example.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.*;
import org.example.enums.Role;
import org.example.enums.TicketStatus;
import org.example.models.Ticket;
import org.example.models.TicketResponseDetails;
import org.example.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardView {
    private static final Logger log = LoggerFactory.getLogger(DashboardView.class);
    private final JPanel panel;
    private final ApiClient apiClient;
    private final NavigationManager navigation;
    private final TicketTableManger tableManager;

    public DashboardView(ApiClient apiClient, NavigationManager navigation, TicketTableManger tableManager) {
        this.apiClient = apiClient;
        this.navigation = navigation;
        this.tableManager = tableManager;
        panel = new JPanel(new BorderLayout());

        initializeUI();
    }

    private void initializeUI() {
        JTable ticketTable = tableManager.getTable();
        JScrollPane scrollPane = new JScrollPane(ticketTable);

        User currentUser = UserHolder.getInstance().getUser();

        JLabel jLabel = new JLabel(currentUser.getRole().toString() + " : " + currentUser.getName().replace("\"", ""));
        JButton newTicketBtn = new JButton("New Ticket");
        JButton refreshBtn = new JButton("Refresh");
        JButton viewBtn = new JButton("View");
        JButton changeStatusBtn = new JButton("ChangeStatus");
        JTextField searchField = new JTextField(20);
        JComboBox<TicketStatus> statusFilter = new JComboBox<>(TicketStatus.values());
        JButton logoutBtn = new JButton("Logout");

        // changeStatusBtn only display if user has ITSUPPORT Role
        changeStatusBtn.setVisible(currentUser.getRole().equals(Role.ITSUPPORT));

        // new tickets button
        newTicketBtn.addActionListener(e -> {
            navigation.registerView("create_ticket_view", new CreateTicketView(apiClient, navigation, tableManager).getPanel());
            SwingUtilities.invokeLater(() ->
                    navigation.showView("create_ticket_view"));
        });

        // refresh button
        refreshBtn.addActionListener(e -> refreshTickets());

        // change status
        changeStatusBtn.addActionListener(e -> {
            showChangeStatusDialog();
        });

        // search
        searchField.addActionListener(e -> {
            String value = searchField.getText();
            if (value.matches("\\d+")) {
                try {
                    apiClient.getTicketById(Long.parseLong(value),
                            node -> {
                                Ticket ticket = Ticket.parseTicket(node);
                                List<Ticket> tickets = new ArrayList<>();
                                tickets.add(ticket);

                                tableManager.updateTickets(tickets);
                            },
                            error -> JOptionPane.showMessageDialog(panel, error)
                    );
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
            }
        });

        // status filter
        statusFilter.addActionListener(e -> {
            TicketStatus selectedItem = (TicketStatus) statusFilter.getSelectedItem();
            if (selectedItem == TicketStatus.ALL) {
                refreshTickets();
                return;
            }
            apiClient.getTickets(selectedItem,
                    response -> {
                        List<Ticket> tickets = parseTickets(response);
                        tableManager.updateTickets(tickets);
                    },
                    error -> JOptionPane.showMessageDialog(panel, error)
            );
        });

        viewBtn.addActionListener(e -> {
            int selectedRow = ticketTable.getSelectedRow();

            Long ticketId = Long.parseLong(ticketTable.getValueAt(selectedRow, 0).toString());

            try {
                apiClient.getTicketById(ticketId, node -> {
                    TicketResponseDetails ticketResponseDetails = TicketResponseDetails.parse(node);

                    navigation.registerView("view_ticket", new TicketView(apiClient, navigation, ticketResponseDetails).getPanel());

                    SwingUtilities.invokeLater(() ->
                            navigation.showView("view_ticket"));
                }, s -> {
                });
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        });

        logoutBtn.addActionListener(e -> {
            TokenHolder.getInstance().setToken(null);
            UserHolder.getInstance().setUser((User) null);
            navigation.registerView("login" , new LoginView(apiClient, navigation).getPanel());
            SwingUtilities.invokeLater(() -> navigation.showView("login"));
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(jLabel);
        controlPanel.add(newTicketBtn);
        controlPanel.add(refreshBtn);
        controlPanel.add(viewBtn);
        controlPanel.add(changeStatusBtn);
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(new JLabel("Status:"));
        controlPanel.add(statusFilter);
        controlPanel.add(logoutBtn);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshTickets();
    }

    private void refreshTickets() {
        apiClient.getTickets(TicketStatus.ALL,
                response -> {
                    List<Ticket> tickets = parseTickets(response);
                    tableManager.updateTickets(tickets);
                },
                error -> JOptionPane.showMessageDialog(panel, error)
        );
    }

    private List<Ticket> parseTickets(JsonNode response) {
        List<Ticket> tickets = new ArrayList<>();
        for (JsonNode node : response) {
            Ticket ticket = Ticket.parseTicket(node);
            tickets.add(ticket);
        }
        return tickets;
    }

    private void showChangeStatusDialog() {
        JTable ticketTable = tableManager.getTable();
        int selectedRow = ticketTable.getSelectedRow();

        JDialog dialog = new JDialog(navigation.getMainFrame(), "Update Ticket Status", true);
        dialog.setLayout(new BorderLayout());
        dialog.setPreferredSize(new Dimension(300, 150)); // Larger dialog size

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        Long ticketId = Long.parseLong(ticketTable.getValueAt(selectedRow, 0).toString());
        TicketStatus currentTicketStatus = TicketStatus.valueOf(ticketTable.getValueAt(selectedRow, 5).toString());

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("New Status:"));
        JComboBox<Object> ticketStatusJCombo = new JComboBox<>(
                Arrays.stream(TicketStatus.values()).filter(ticketStatus -> ticketStatus != TicketStatus.ALL)
                        .toArray()
        );
        ticketStatusJCombo.setPreferredSize(new Dimension(150, 30));
        ticketStatusJCombo.setSelectedItem(currentTicketStatus);
        statusPanel.add(ticketStatusJCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Update Status");
        submitBtn.setPreferredSize(new Dimension(130, 30));

        submitBtn.addActionListener(e1 -> {
            TicketStatus selectedStatus = (TicketStatus) ticketStatusJCombo.getSelectedItem();
            changeStatusDialog(ticketId, selectedStatus, currentTicketStatus);
            dialog.dispose();
        });

        dialogPanel.add(statusPanel, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(submitBtn);

        dialog.add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(navigation.getMainFrame()); // Center dialog
        dialog.setVisible(true);
    }

    private void changeStatusDialog(Long ticketId, TicketStatus selectedStatus, TicketStatus currentTicketStatus) {
        if (selectedStatus == currentTicketStatus) {
            return;
        }
        try {
            apiClient.changeStatus(ticketId, selectedStatus,
                    response -> {
                        navigation.registerView("dashboard", new DashboardView(apiClient, navigation, new TicketTableManger(new ArrayList<>())).getPanel());
                        SwingUtilities.invokeLater(() ->
                                navigation.showView("dashboard"));
                    },
                    error -> JOptionPane.showMessageDialog(panel, error)
            );
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}