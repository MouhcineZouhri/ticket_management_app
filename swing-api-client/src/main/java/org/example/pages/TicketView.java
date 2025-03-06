package org.example.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.ApiClient;
import org.example.NavigationManager;
import org.example.TicketTableManger;
import org.example.UserHolder;
import org.example.enums.Role;
import org.example.enums.TicketStatus;
import org.example.models.TicketResponseDetails;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

public class TicketView {
    private final JPanel panel;
    private final ApiClient apiClient;
    private final NavigationManager navigation;
    private TicketResponseDetails ticketResponseDetails;

    public TicketView(ApiClient apiClient, NavigationManager navigation, TicketResponseDetails ticketResponseDetails) {
        this.panel = new JPanel(new GridLayout(0, 2));
        this.apiClient = apiClient;
        this.navigation = navigation;
        this.ticketResponseDetails = ticketResponseDetails;
        initializeUI();
    }

    public void initializeUI() {
        // Main content panel with vertical layout
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> navigateToDashboard());
        northPanel.add(backButton);
        panel.add(northPanel, BorderLayout.NORTH);

        // Section 1: Basic Information
        JPanel infoPanel = createSectionPanel("Basic Information");
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addFormRow(infoPanel, gbc, 0, "ID:", new JLabel(String.valueOf(ticketResponseDetails.getId())));
        addFormRow(infoPanel, gbc, 1, "Title:", createNonEditableTextField(ticketResponseDetails.getTitle()));
        addFormRow(infoPanel, gbc, 2, "Description:", createScrollableTextArea(ticketResponseDetails.getDescription()));
        addFormRow(infoPanel, gbc, 3, "Priority:", new JLabel(ticketResponseDetails.getPriority().toString()));
        addFormRow(infoPanel, gbc, 4, "Category:", new JLabel(ticketResponseDetails.getCategory().toString()));
        addFormRow(infoPanel, gbc, 5, "Status:", new JLabel(ticketResponseDetails.getTicketStatus().toString()));
        addFormRow(infoPanel, gbc, 6, "Created At:",
                new JLabel(ticketResponseDetails.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));

        // Section 2: Status History
        JPanel statusPanel = createSectionPanel("Status History");
        JList<TicketStatus> statusList = new JList<>(new Vector<>(ticketResponseDetails.getTicketStatusHistory()));
        statusPanel.add(new JScrollPane(statusList));

        // Section 3: Comments
        JButton addCommentButton = new JButton("Add Comment");
        User user = UserHolder.getInstance().getUser();
        addCommentButton.setVisible(user.getRole().equals(Role.ITSUPPORT));
        addCommentButton.addActionListener(e -> {
            // Create comment input dialog
            showCommentDialog();
        });

        JPanel commentPanel = createSectionPanel("Comments");
        JList<String> commentList = new JList<>(new Vector<>(ticketResponseDetails.getComments()));
        commentPanel.add(new JScrollPane(commentList));

        // Add sections to main panel
        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(statusPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(addCommentButton);
        panel.add(Box.createVerticalStrut(15));
        panel.add(commentPanel);


    }

    // Helper methods
    private static JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), title));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private static void addFormRow(JPanel panel, GridBagConstraints gbc, int yPos, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
    }

    private static JComponent createNonEditableTextField(String text) {
        JTextField tf = new JTextField(text);
        tf.setEditable(false);
        tf.setBorder(BorderFactory.createEmptyBorder());
        tf.setBackground(UIManager.getColor("Panel.background"));
        return tf;
    }

    private static JComponent createScrollableTextArea(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        return new JScrollPane(ta);
    }

    // Custom list renderers
    private static class StatusListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            return label;
        }
    }

    private static class CommentListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            label.setBackground(index % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
            label.setOpaque(true);
            return label;
        }
    }

    public void showCommentDialog() {
        JDialog commentDialog = new JDialog(navigation.getMainFrame(), "Add Comment", true);
        commentDialog.setLayout(new BorderLayout());
        commentDialog.setSize(400, 200);
        commentDialog.setLocationRelativeTo(navigation.getMainFrame());

        // Create text area with scroll pane
        JTextArea commentInput = new JTextArea(5, 20);
        commentInput.setLineWrap(true);
        commentInput.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(commentInput);

        // Create submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(submitEvent -> {
            String newComment = commentInput.getText().trim();
            try {
                apiClient.addComments(ticketResponseDetails.getId(), newComment, node -> {
                    try {
                        apiClient.getTicketById(ticketResponseDetails.getId(), ticketResponse -> {
                            TicketResponseDetails ticketResponseDetails = TicketResponseDetails
                                    .parse(ticketResponse);

                            navigation.registerView("view_ticket", new TicketView(apiClient, navigation, ticketResponseDetails).getPanel());

                            SwingUtilities.invokeLater(() ->
                                    navigation.showView("view_ticket"));
                        }, s -> {
                        });

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    commentDialog.setVisible(false);
                }, s -> {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        // Create panel layout
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Enter your comment:"), BorderLayout.NORTH);
        inputPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        commentDialog.add(inputPanel, BorderLayout.CENTER);
        commentDialog.add(buttonPanel, BorderLayout.SOUTH);
        commentDialog.setVisible(true);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void navigateToDashboard() {
        navigation.registerView("dashboard",
                new DashboardView(apiClient, navigation, new TicketTableManger(new ArrayList<>())).getPanel()
        );
        SwingUtilities.invokeLater(() -> navigation.showView("dashboard"));
    }
}
