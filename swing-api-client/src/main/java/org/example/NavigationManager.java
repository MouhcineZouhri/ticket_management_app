package org.example;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class NavigationManager {
    private final JFrame mainFrame;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    public NavigationManager(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.add(mainPanel);
    }

    public void registerView(String name, JComponent view) {
        mainPanel.add(view, name);
    }

    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
        mainFrame.revalidate();
        mainFrame.repaint();
    }


}