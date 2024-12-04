package com.sortingapp;

import javax.swing.*;

/**
 * Main class to launch the Sorting Algorithm Performance Evaluator application.
 */
public class Main {
    public static void main(String[] args) {
        // Set the look and feel to the system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If failed, continue with default look and feel
        }

        // Launch the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SortingGUI gui = new SortingGUI();
            gui.setVisible(true);
        });
    }
}
