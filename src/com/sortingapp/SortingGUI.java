//package com.sortingapp;
//
//import com.sortingapp.sortingalgorithms.*;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import java.util.List;
//
///**
// * GUI class for the Sorting Algorithm Performance Evaluator application.
// */
//public class SortingGUI extends JFrame {
//
//    private JButton uploadButton;
//    private JComboBox<String> columnSelector;
//    private JButton sortButton;
//    private JTextArea resultArea;
//    private JTable dataTable;
//    private JScrollPane tableScrollPane;
//    private JPanel chartContainer;
//
//    private CSVReaderUtil csvReader;
//    private List<Double> columnData;
//    private List<Integer> numericColumnIndices;
//    private String[] headers;
//    private String currentFilePath;
//
//    public SortingGUI() {
//        setTitle("Sorting Algorithm Performance Evaluator");
//        setSize(1000, 700);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        csvReader = new CSVReaderUtil();
//
//        initUI();
//    }
//
//    private void initUI() {
//        JPanel mainPanel = new JPanel(new BorderLayout());
//
//        // Top Panel for Upload and Column Selection
//        JPanel topPanel = new JPanel();
//        uploadButton = new JButton("Upload CSV");
//        uploadButton.addActionListener(e -> uploadCSV());
//
//        columnSelector = new JComboBox<>();
//        columnSelector.setEnabled(false);
//
//        sortButton = new JButton("Sort");
//        sortButton.setEnabled(false);
//        sortButton.addActionListener(e -> performSorting());
//
//        topPanel.add(uploadButton);
//        topPanel.add(new JLabel("Select Numeric Column:"));
//        topPanel.add(columnSelector);
//        topPanel.add(sortButton);
//
//        // Center Panel for Data Table and Chart
//        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        centerSplitPane.setResizeWeight(0.5);
//
//        // Data Table
//        dataTable = new JTable();
//        tableScrollPane = new JScrollPane(dataTable);
//        centerSplitPane.setLeftComponent(tableScrollPane);
//
//        // Chart Container
//        chartContainer = new JPanel(new BorderLayout());
//        centerSplitPane.setRightComponent(chartContainer);
//
//        // Bottom Panel for Results
//        resultArea = new JTextArea(10, 30);
//        resultArea.setEditable(false);
//        JScrollPane resultScrollPane = new JScrollPane(resultArea);
//
//        // Add components to main panel
//        mainPanel.add(topPanel, BorderLayout.NORTH);
//        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
//        mainPanel.add(resultScrollPane, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    private void uploadCSV() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
//        int option = fileChooser.showOpenDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
//            currentFilePath = filepath;
//            boolean success = csvReader.readCSV(filepath);
//            if (success) {
//                headers = csvReader.getHeaders();
//                numericColumnIndices = csvReader.getNumericColumnIndices();
//                populateColumns();
//                displayDataTable();
//                clearChart();
//                resultArea.setText("");
//            }
//        }
//    }
//
//    private void populateColumns() {
//        columnSelector.removeAllItems();
//        for (Integer index : numericColumnIndices) {
//            columnSelector.addItem(headers[index]);
//        }
//        if (!numericColumnIndices.isEmpty()) {
//            columnSelector.setEnabled(true);
//            sortButton.setEnabled(true);
//        } else {
//            columnSelector.setEnabled(false);
//            sortButton.setEnabled(false);
//            JOptionPane.showMessageDialog(this, "No numeric columns found in the CSV file.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    private void displayDataTable() {
//        List<String[]> data = csvReader.getData();
//        String[] tableHeaders = headers;
//        DefaultTableModel tableModel = new DefaultTableModel(tableHeaders, 0);
//
//        for (String[] row : data) {
//            // Ensure the row has the same number of columns as headers
//            String[] completeRow = new String[headers.length];
//            for (int i = 0; i < headers.length; i++) {
//                if (i < row.length) {
//                    completeRow[i] = row[i];
//                } else {
//                    completeRow[i] = "";
//                }
//            }
//            tableModel.addRow(completeRow);
//        }
//
//        dataTable.setModel(tableModel);
//    }
//
//    private void performSorting() {
//        String selectedColumn = (String) columnSelector.getSelectedItem();
//        if (selectedColumn == null) {
//            JOptionPane.showMessageDialog(this, "Please select a column to sort.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        int columnIndex = Arrays.asList(headers).indexOf(selectedColumn);
//        columnData = new ArrayList<>();
//
//        // Extract numeric data from the selected column
//        for (String[] row : csvReader.getData()) {
//            if (columnIndex < row.length) {
//                String value = row[columnIndex];
//                try {
//                    double num = Double.parseDouble(value);
//                    columnData.add(num);
//                } catch (NumberFormatException e) {
//                    // Skip non-numeric or invalid data
//                }
//            }
//        }
//
//        if (columnData.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Selected column contains no numeric data.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Convert List<Double> to double[]
//        double[] dataArray = columnData.stream().mapToDouble(Double::doubleValue).toArray();
//
//        // Perform sorting and measure execution time
//        performAllSorts(dataArray.clone(), selectedColumn);
//    }
//
//    private void performAllSorts(double[] data, String selectedColumn) {
//        StringBuilder result = new StringBuilder();
//        Map<String, Long> executionTimes = new LinkedHashMap<>();
//
//        // Insertion Sort
//        double[] insertionData = data.clone();
//        long startTime = System.nanoTime();
//        InsertionSort.sort(insertionData);
//        long endTime = System.nanoTime();
//        executionTimes.put("Insertion Sort", endTime - startTime);
//
//        // Shell Sort
//        double[] shellData = data.clone();
//        startTime = System.nanoTime();
//        ShellSort.sort(shellData);
//        endTime = System.nanoTime();
//        executionTimes.put("Shell Sort", endTime - startTime);
//
//        // Merge Sort
//        double[] mergeData = data.clone();
//        startTime = System.nanoTime();
//        MergeSort.sort(mergeData, 0, mergeData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Merge Sort", endTime - startTime);
//
//        // Quick Sort
//        double[] quickData = data.clone();
//        startTime = System.nanoTime();
//        QuickSort.sort(quickData, 0, quickData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Quick Sort", endTime - startTime);
//
//        // Heap Sort
//        double[] heapData = data.clone();
//        startTime = System.nanoTime();
//        HeapSort.sort(heapData);
//        endTime = System.nanoTime();
//        executionTimes.put("Heap Sort", endTime - startTime);
//
//        // Display execution times
//        result.append("Sorting Algorithm Performance (in nanoseconds):\n");
//        String bestAlgorithm = null;
//        long bestTime = Long.MAX_VALUE;
//
//        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
//            result.append(String.format("%s: %d ns\n", entry.getKey(), entry.getValue()));
//            if (entry.getValue() < bestTime) {
//                bestTime = entry.getValue();
//                bestAlgorithm = entry.getKey();
//            }
//        }
//
//        result.append(String.format("\nBest Performing Algorithm: %s (%d ns)", bestAlgorithm, bestTime));
//        resultArea.setText(result.toString());
//
//        // Visualize Original and Sorted Data
//        visualizeData(data, bestAlgorithm, executionTimes.get(bestAlgorithm));
//    }
//
//    private void visualizeData(double[] originalData, String bestAlgorithm, long bestTime) {
//        // Create datasets
//        DefaultCategoryDataset originalDataset = new DefaultCategoryDataset();
//        DefaultCategoryDataset sortedDataset = new DefaultCategoryDataset();
//
//        // To avoid cluttering the chart, we'll plot only the first 100 data points
//        int limit = Math.min(originalData.length, 100);
//
//        for (int i = 0; i < limit; i++) {
//            originalDataset.addValue(originalData[i], "Original", Integer.toString(i + 1));
//        }
//
//        // Sort the data using the best algorithm for visualization
//        double[] sortedData = originalData.clone();
//        switch (bestAlgorithm) {
//            case "Insertion Sort":
//                InsertionSort.sort(sortedData);
//                break;
//            case "Shell Sort":
//                ShellSort.sort(sortedData);
//                break;
//            case "Merge Sort":
//                MergeSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Quick Sort":
//                QuickSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Heap Sort":
//                HeapSort.sort(sortedData);
//                break;
//            default:
//                // Default to original data if algorithm not recognized
//                break;
//        }
//
//        for (int i = 0; i < limit; i++) {
//            sortedDataset.addValue(sortedData[i], "Sorted", Integer.toString(i + 1));
//        }
//
//        // Create charts
//        JFreeChart originalChart = ChartFactory.createLineChart(
//                "Original Data",
//                "Index",
//                "Value",
//                originalDataset
//        );
//
//        JFreeChart sortedChart = ChartFactory.createLineChart(
//                "Sorted Data",
//                "Index",
//                "Value",
//                sortedDataset
//        );
//
//        // Clear previous charts
//        chartContainer.removeAll();
//
//        // Add charts to the chart container
//        JPanel chartsPanel = new JPanel(new GridLayout(2, 1));
//        chartsPanel.add(new ChartPanel(originalChart));
//        chartsPanel.add(new ChartPanel(sortedChart));
//
//        chartContainer.add(chartsPanel, BorderLayout.CENTER);
//        chartContainer.validate();
//    }
//
//    private void clearChart() {
//        chartContainer.removeAll();
//        chartContainer.revalidate();
//        chartContainer.repaint();
//    }
//}
//
//
//


//package com.sortingapp;
//
//import com.sortingapp.sortingalgorithms.*;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
///**
// * GUI class for the Sorting Algorithm Performance Evaluator application.
// */
//public class SortingGUI extends JFrame {
//
//    private JButton uploadButton;
//    private JComboBox<String> columnSelector;
//    private JButton sortButton;
//    private JTextArea resultArea;
//    private JTable dataTable;
//    private JScrollPane tableScrollPane;
//    private JPanel chartContainer;
//    private JProgressBar progressBar;
//
//    private CSVReaderUtil csvReader;
//    private List<Double> columnData;
//    private List<Integer> numericColumnIndices;
//    private String[] headers;
//    private String currentFilePath;
//
//    public SortingGUI() {
//        // Set the Nimbus look and feel
//        setNimbusLookAndFeel();
//
//        setTitle("Sorting Algorithm Performance Evaluator");
//        setSize(1000, 700);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Set a custom font for the entire application
//        setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
//
//        // Set background color
//        getContentPane().setBackground(new Color(240, 240, 240));
//
//        csvReader = new CSVReaderUtil();
//
//        initUI();
//    }
//
//    /**
//     * Initializes the user interface components.
//     */
//    private void initUI() {
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(new Color(240, 240, 240));
//
//        // Top Panel for Upload and Column Selection
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        topPanel.setBackground(new Color(220, 220, 220)); // Light Gray
//
//        // Upload Button
//        uploadButton = new JButton("Upload CSV");
//        uploadButton.setBackground(new Color(70, 130, 180)); // Steel Blue
//        uploadButton.setForeground(Color.WHITE);
//        uploadButton.setFocusPainted(false);
//        uploadButton.setFont(new Font("Arial", Font.BOLD, 14));
//        uploadButton.setBorder(new RoundedBorder(10));
//        uploadButton.setMargin(new Insets(5, 15, 5, 15));
//        uploadButton.setToolTipText("Upload a CSV file");
//        uploadButton.addActionListener(e -> uploadCSV());
//
//        // Column Selector Label
//        JLabel selectColumnLabel = new JLabel("Select Numeric Column:");
//        selectColumnLabel.setFont(new Font("Arial", Font.BOLD, 14));
//
//        // Column Selector ComboBox
//        columnSelector = new JComboBox<>();
//        columnSelector.setEnabled(false);
//        columnSelector.setPreferredSize(new Dimension(200, 30));
//        columnSelector.setFont(new Font("Arial", Font.PLAIN, 14));
//        columnSelector.setBackground(Color.WHITE);
//        columnSelector.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//
//        // Sort Button
//        sortButton = new JButton("Sort");
//        sortButton.setBackground(new Color(70, 130, 180)); // Steel Blue
//        sortButton.setForeground(Color.WHITE);
//        sortButton.setFocusPainted(false);
//        sortButton.setFont(new Font("Arial", Font.BOLD, 14));
//        sortButton.setBorder(new RoundedBorder(10));
//        sortButton.setMargin(new Insets(5, 15, 5, 15));
//        sortButton.setToolTipText("Perform sorting on selected column");
//        sortButton.setEnabled(false);
//        sortButton.addActionListener(e -> performSorting());
//
//        // Progress Bar
//        progressBar = new JProgressBar();
//        progressBar.setVisible(false);
//        progressBar.setPreferredSize(new Dimension(200, 25));
//        progressBar.setIndeterminate(true); // Show an indeterminate progress
//
//        // Add components to the top panel
//        topPanel.add(uploadButton);
//        topPanel.add(selectColumnLabel);
//        topPanel.add(columnSelector);
//        topPanel.add(sortButton);
//        topPanel.add(progressBar);
//
//        // Center Panel for Data Table and Chart
//        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        centerSplitPane.setResizeWeight(0.5);
//        centerSplitPane.setDividerSize(5);
//        centerSplitPane.setDividerLocation(500);
//
//        // Data Table
//        dataTable = new JTable();
//        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
//        dataTable.setGridColor(Color.GRAY);
//        dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
//        dataTable.setRowHeight(25);
//        dataTable.setSelectionBackground(new Color(184, 207, 229));
//        dataTable.setSelectionForeground(Color.BLACK);
//        tableScrollPane = new JScrollPane(dataTable);
//        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
//        centerSplitPane.setLeftComponent(tableScrollPane);
//
//        // Chart Container
//        chartContainer = new JPanel(new BorderLayout());
//        chartContainer.setBackground(Color.WHITE);
//        centerSplitPane.setRightComponent(chartContainer);
//
//        // Bottom Panel for Results
//        resultArea = new JTextArea(10, 30);
//        resultArea.setEditable(false);
//        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        resultArea.setBackground(new Color(245, 245, 245));
//        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        JScrollPane resultScrollPane = new JScrollPane(resultArea);
//        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//        // Add components to main panel
//        mainPanel.add(topPanel, BorderLayout.NORTH);
//        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
//        mainPanel.add(resultScrollPane, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    /**
//     * Sets the Nimbus look and feel for the application.
//     */
//    private void setNimbusLookAndFeel() {
//        try {
//            boolean nimbusFound = false;
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    nimbusFound = true;
//                    break;
//                }
//            }
//            if (!nimbusFound) {
//                // If Nimbus is not available, set the default look and feel.
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            }
//        } catch (Exception e) {
//            // If Nimbus is not available, you can set the GUI to another look and feel.
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Handles the CSV upload functionality.
//     */
//    private void uploadCSV() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
//        int option = fileChooser.showOpenDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
//            currentFilePath = filepath;
//            boolean success = csvReader.readCSV(filepath);
//            if (success) {
//                headers = csvReader.getHeaders();
//                numericColumnIndices = csvReader.getNumericColumnIndices();
//                populateColumns();
//                displayDataTable();
//                clearChart();
//                resultArea.setText("");
//            } else {
//                JOptionPane.showMessageDialog(this, "Failed to read the CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//    }
//
//    /**
//     * Populates the column selector ComboBox with numeric columns.
//     */
//    private void populateColumns() {
//        columnSelector.removeAllItems();
//        for (Integer index : numericColumnIndices) {
//            columnSelector.addItem(headers[index]);
//        }
//        if (!numericColumnIndices.isEmpty()) {
//            columnSelector.setEnabled(true);
//            sortButton.setEnabled(true);
//        } else {
//            columnSelector.setEnabled(false);
//            sortButton.setEnabled(false);
//            JOptionPane.showMessageDialog(this, "No numeric columns found in the CSV file.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Displays the CSV data in the JTable.
//     */
//    private void displayDataTable() {
//        List<String[]> data = csvReader.getData();
//        String[] tableHeaders = headers;
//        DefaultTableModel tableModel = new DefaultTableModel(tableHeaders, 0);
//
//        for (String[] row : data) {
//            // Ensure the row has the same number of columns as headers
//            String[] completeRow = new String[headers.length];
//            for (int i = 0; i < headers.length; i++) {
//                if (i < row.length) {
//                    completeRow[i] = row[i];
//                } else {
//                    completeRow[i] = "";
//                }
//            }
//            tableModel.addRow(completeRow);
//        }
//
//        dataTable.setModel(tableModel);
//    }
//
//    /**
//     * Initiates the sorting process using SwingWorker to keep UI responsive.
//     */
//    private void performSorting() {
//        String selectedColumn = (String) columnSelector.getSelectedItem();
//        if (selectedColumn == null) {
//            JOptionPane.showMessageDialog(this, "Please select a column to sort.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        int columnIndex = Arrays.asList(headers).indexOf(selectedColumn);
//        columnData = new ArrayList<>();
//
//        // Extract numeric data from the selected column
//        for (String[] row : csvReader.getData()) {
//            if (columnIndex < row.length) {
//                String value = row[columnIndex];
//                try {
//                    double num = Double.parseDouble(value);
//                    columnData.add(num);
//                } catch (NumberFormatException e) {
//                    // Skip non-numeric or invalid data
//                }
//            }
//        }
//
//        if (columnData.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Selected column contains no numeric data.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Convert List<Double> to double[]
//        double[] dataArray = columnData.stream().mapToDouble(Double::doubleValue).toArray();
//
//        // Perform sorting and measure execution time in a background thread
//        progressBar.setVisible(true);
//        sortButton.setEnabled(false);
//        columnSelector.setEnabled(false);
//        uploadButton.setEnabled(false);
//
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            @Override
//            protected Void doInBackground() {
//                performAllSorts(dataArray.clone(), selectedColumn);
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                progressBar.setVisible(false);
//                sortButton.setEnabled(true);
//                columnSelector.setEnabled(true);
//                uploadButton.setEnabled(true);
//            }
//        };
//        worker.execute();
//    }
//
//    /**
//     * Performs all sorting algorithms and records their execution times.
//     *
//     * @param data            The data array to sort.
//     * @param selectedColumn  The name of the selected column.
//     */
//    private void performAllSorts(double[] data, String selectedColumn) {
//        StringBuilder result = new StringBuilder();
//        Map<String, Long> executionTimes = new LinkedHashMap<>();
//
//        // Insertion Sort
//        double[] insertionData = data.clone();
//        long startTime = System.nanoTime();
//        InsertionSort.sort(insertionData);
//        long endTime = System.nanoTime();
//        executionTimes.put("Insertion Sort", endTime - startTime);
//
//        // Shell Sort
//        double[] shellData = data.clone();
//        startTime = System.nanoTime();
//        ShellSort.sort(shellData);
//        endTime = System.nanoTime();
//        executionTimes.put("Shell Sort", endTime - startTime);
//
//        // Merge Sort
//        double[] mergeData = data.clone();
//        startTime = System.nanoTime();
//        MergeSort.sort(mergeData, 0, mergeData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Merge Sort", endTime - startTime);
//
//        // Quick Sort
//        double[] quickData = data.clone();
//        startTime = System.nanoTime();
//        QuickSort.sort(quickData, 0, quickData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Quick Sort", endTime - startTime);
//
//        // Heap Sort
//        double[] heapData = data.clone();
//        startTime = System.nanoTime();
//        HeapSort.sort(heapData);
//        endTime = System.nanoTime();
//        executionTimes.put("Heap Sort", endTime - startTime);
//
//        // Display execution times
//        result.append("Sorting Algorithm Performance (in nanoseconds):\n");
//        String bestAlgorithm = null;
//        long bestTime = Long.MAX_VALUE;
//
//        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
//            result.append(String.format("%s: %d ns\n", entry.getKey(), entry.getValue()));
//            if (entry.getValue() < bestTime) {
//                bestTime = entry.getValue();
//                bestAlgorithm = entry.getKey();
//            }
//        }
//
//        result.append(String.format("\nBest Performing Algorithm: %s (%d ns)", bestAlgorithm, bestTime));
//
//        // Update the result area on the EDT
//        SwingUtilities.invokeLater(() -> resultArea.setText(result.toString()));
//
//        // Visualize Original and Sorted Data
//        visualizeData(data, bestAlgorithm, executionTimes.get(bestAlgorithm));
//    }
//
//    /**
//     * Visualizes the original and sorted data using JFreeChart.
//     *
//     * @param originalData   The original data array.
//     * @param bestAlgorithm  The name of the best performing algorithm.
//     * @param bestTime       The execution time of the best performing algorithm.
//     */
//    private void visualizeData(double[] originalData, String bestAlgorithm, long bestTime) {
//        // Create datasets
//        DefaultCategoryDataset originalDataset = new DefaultCategoryDataset();
//        DefaultCategoryDataset sortedDataset = new DefaultCategoryDataset();
//
//        // To avoid cluttering the chart, we'll plot only the first 100 data points
//        int limit = Math.min(originalData.length, 100);
//
//        for (int i = 0; i < limit; i++) {
//            originalDataset.addValue(originalData[i], "Original", Integer.toString(i + 1));
//        }
//
//        // Sort the data using the best algorithm for visualization
//        double[] sortedData = originalData.clone();
//        switch (bestAlgorithm) {
//            case "Insertion Sort":
//                InsertionSort.sort(sortedData);
//                break;
//            case "Shell Sort":
//                ShellSort.sort(sortedData);
//                break;
//            case "Merge Sort":
//                MergeSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Quick Sort":
//                QuickSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Heap Sort":
//                HeapSort.sort(sortedData);
//                break;
//            default:
//                // Default to original data if algorithm not recognized
//                break;
//        }
//
//        for (int i = 0; i < limit; i++) {
//            sortedDataset.addValue(sortedData[i], "Sorted", Integer.toString(i + 1));
//        }
//
//        // Create charts
//        JFreeChart originalChart = ChartFactory.createLineChart(
//                "Original Data",
//                "Index",
//                "Value",
//                originalDataset
//        );
//
//        JFreeChart sortedChart = ChartFactory.createLineChart(
//                "Sorted Data",
//                "Index",
//                "Value",
//                sortedDataset
//        );
//
//        // Clear previous charts and update on the EDT
//        SwingUtilities.invokeLater(() -> {
//            chartContainer.removeAll();
//
//            // Add charts to the chart container
//            JPanel chartsPanel = new JPanel(new GridLayout(2, 1));
//            chartsPanel.add(new ChartPanel(originalChart));
//            chartsPanel.add(new ChartPanel(sortedChart));
//
//            chartContainer.add(chartsPanel, BorderLayout.CENTER);
//            chartContainer.validate();
//        });
//    }
//
//    /**
//     * Clears the chart container.
//     */
//    private void clearChart() {
//        chartContainer.removeAll();
//        chartContainer.revalidate();
//        chartContainer.repaint();
//    }
//
//    /**
//     * Sets a custom font for all UI components.
//     *
//     * @param f The FontUIResource to apply.
//     */
//    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
//        Enumeration<Object> keys = UIManager.getDefaults().keys();
//        while (keys.hasMoreElements()) {
//            Object key = keys.nextElement();
//            Object value = UIManager.get(key);
//            if (value instanceof javax.swing.plaf.FontUIResource)
//                UIManager.put(key, f);
//        }
//    }
//
//    /**
//     * Custom Rounded Border class to create rounded edges for buttons.
//     */
//    public class RoundedBorder implements Border {
//
//        private int radius;
//
//        RoundedBorder(int radius) {
//            this.radius = radius;
//        }
//
//        public Insets getBorderInsets(Component c) {
//            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
//        }
//
//        public boolean isBorderOpaque() {
//            return false;
//        }
//
//        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            g.setColor(c.getBackground());
//            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
//        }
//    }
//
//    /**
//     * Main method to run the SortingGUI application.
//     */
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SortingGUI gui = new SortingGUI();
//            gui.setVisible(true);
//        });
//    }
//}



//package com.sortingapp;
//
//import com.sortingapp.sortingalgorithms.*;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
///**
// * GUI class for the Sorting Algorithm Performance Evaluator application.
// */
//public class SortingGUI extends JFrame {
//
//    private JButton uploadButton;
//    private JComboBox<String> columnSelector;
//    private JButton sortButton;
//    private JTextArea resultArea;
//    private JTable dataTable;
//    private JScrollPane tableScrollPane;
//    private JPanel chartContainer;
//    private JProgressBar progressBar;
//
//    private CSVReaderUtil csvReader;
//    private List<Double> columnData;
//    private List<Integer> numericColumnIndices;
//    private String[] headers;
//    private String currentFilePath;
//
//    public SortingGUI() {
//        // Set the Nimbus look and feel
//        setNimbusLookAndFeel();
//
//        setTitle("Sorting Algorithm Performance Evaluator");
//        setSize(1200, 800);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Set a custom font for the entire application
//        setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));
//
//        // Set background color
//        getContentPane().setBackground(new Color(240, 240, 240));
//
//        csvReader = new CSVReaderUtil();
//
//        initUI();
//    }
//
//    /**
//     * Initializes the user interface components.
//     */
//    private void initUI() {
//        JPanel mainPanel = new JPanel(new BorderLayout());
//        mainPanel.setBackground(new Color(240, 240, 240));
//
//        // Top Panel for Upload and Column Selection
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        topPanel.setBackground(new Color(220, 220, 220)); // Light Gray
//
//        // Upload Button
//        uploadButton = new JButton("Upload CSV");
//        uploadButton.setBackground(new Color(70, 130, 180)); // Steel Blue
//        uploadButton.setForeground(Color.WHITE);
//        uploadButton.setFocusPainted(false);
//        uploadButton.setFont(new Font("Arial", Font.BOLD, 14));
//        uploadButton.setBorder(new RoundedBorder(10));
//        uploadButton.setMargin(new Insets(5, 15, 5, 15));
//        uploadButton.setToolTipText("Upload a CSV file");
//        uploadButton.addActionListener(e -> uploadCSV());
//
//        // Column Selector Label
//        JLabel selectColumnLabel = new JLabel("Select Numeric Column:");
//        selectColumnLabel.setFont(new Font("Arial", Font.BOLD, 14));
//
//        // Column Selector ComboBox
//        columnSelector = new JComboBox<>();
//        columnSelector.setEnabled(false);
//        columnSelector.setPreferredSize(new Dimension(250, 30));
//        columnSelector.setFont(new Font("Arial", Font.PLAIN, 14));
//        columnSelector.setBackground(Color.WHITE);
//        columnSelector.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        columnSelector.setToolTipText("Choose a column containing numeric data for sorting");
//
//        // Sort Button
//        sortButton = new JButton("Sort");
//        sortButton.setBackground(new Color(70, 130, 180)); // Steel Blue
//        sortButton.setForeground(Color.WHITE);
//        sortButton.setFocusPainted(false);
//        sortButton.setFont(new Font("Arial", Font.BOLD, 14));
//        sortButton.setBorder(new RoundedBorder(10));
//        sortButton.setMargin(new Insets(5, 15, 5, 15));
//        sortButton.setToolTipText("Perform sorting on selected column");
//        sortButton.setEnabled(false);
//        sortButton.addActionListener(e -> performSorting());
//
//        // Progress Bar
//        progressBar = new JProgressBar();
//        progressBar.setVisible(false);
//        progressBar.setPreferredSize(new Dimension(200, 25));
//        progressBar.setIndeterminate(true); // Show an indeterminate progress
//        progressBar.setToolTipText("Sorting in progress... Please wait.");
//
//        // Add components to the top panel
//        topPanel.add(uploadButton);
//        topPanel.add(selectColumnLabel);
//        topPanel.add(columnSelector);
//        topPanel.add(sortButton);
//        topPanel.add(progressBar);
//
//        // Center Panel for Data Table and Chart
//        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        centerSplitPane.setResizeWeight(0.5);
//        centerSplitPane.setDividerSize(5);
//        centerSplitPane.setDividerLocation(600);
//
//        // Data Table
//        dataTable = new JTable();
//        dataTable.setFont(new Font("Arial", Font.PLAIN, 12));
//        dataTable.setGridColor(Color.GRAY);
//        dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
//        dataTable.setRowHeight(25);
//        dataTable.setSelectionBackground(new Color(184, 207, 229));
//        dataTable.setSelectionForeground(Color.BLACK);
//        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Allow horizontal scrolling
//        tableScrollPane = new JScrollPane(dataTable);
//        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
//        centerSplitPane.setLeftComponent(tableScrollPane);
//
//        // Chart Container
//        chartContainer = new JPanel(new BorderLayout());
//        chartContainer.setBackground(Color.WHITE);
//        centerSplitPane.setRightComponent(chartContainer);
//
//        // Bottom Panel for Results
//        resultArea = new JTextArea(15, 30);
//        resultArea.setEditable(false);
//        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        resultArea.setBackground(new Color(245, 245, 245));
//        resultArea.setBorder(BorderFactory.createTitledBorder("Sorting Results"));
//        resultArea.setToolTipText("Displays the performance of each sorting algorithm");
//
//        JScrollPane resultScrollPane = new JScrollPane(resultArea);
//        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//        // Add components to main panel
//        mainPanel.add(topPanel, BorderLayout.NORTH);
//        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
//        mainPanel.add(resultScrollPane, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    /**
//     * Sets the Nimbus look and feel for the application.
//     */
//    private void setNimbusLookAndFeel() {
//        try {
//            boolean nimbusFound = false;
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    nimbusFound = true;
//                    break;
//                }
//            }
//            if (!nimbusFound) {
//                // If Nimbus is not available, set the default look and feel.
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            }
//        } catch (Exception e) {
//            // If Nimbus is not available, you can set the GUI to another look and feel.
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Handles the CSV upload functionality with proper edge case handling.
//     */
//    private void uploadCSV() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
//        int option = fileChooser.showOpenDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
//            currentFilePath = filepath;
//            boolean success = csvReader.readCSV(filepath);
//            if (success) {
//                headers = csvReader.getHeaders();
//                numericColumnIndices = csvReader.getNumericColumnIndices();
//                if (headers == null || headers.length == 0) {
//                    JOptionPane.showMessageDialog(this, "The CSV file has no headers.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                if (csvReader.getData().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "The CSV file is empty.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                populateColumns();
//                displayDataTable();
//                clearChart();
//                resultArea.setText("");
//            } else {
//                JOptionPane.showMessageDialog(this, "Failed to read the CSV file. Please ensure it's properly formatted.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } else {
//            // User canceled the file chooser
//            JOptionPane.showMessageDialog(this, "File upload canceled.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Populates the column selector ComboBox with numeric columns.
//     */
//    private void populateColumns() {
//        columnSelector.removeAllItems();
//        for (Integer index : numericColumnIndices) {
//            columnSelector.addItem(headers[index]);
//        }
//        if (!numericColumnIndices.isEmpty()) {
//            columnSelector.setEnabled(true);
//            sortButton.setEnabled(true);
//        } else {
//            columnSelector.setEnabled(false);
//            sortButton.setEnabled(false);
//            JOptionPane.showMessageDialog(this, "No numeric columns found in the CSV file.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Displays the CSV data in the JTable with proper handling of inconsistent rows.
//     */
//    private void displayDataTable() {
//        List<String[]> data = csvReader.getData();
//        String[] tableHeaders = headers;
//        DefaultTableModel tableModel = new DefaultTableModel(tableHeaders, 0);
//
//        for (String[] row : data) {
//            // Ensure the row has the same number of columns as headers
//            String[] completeRow = new String[headers.length];
//            for (int i = 0; i < headers.length; i++) {
//                if (i < row.length) {
//                    completeRow[i] = row[i].trim();
//                } else {
//                    completeRow[i] = ""; // Fill missing columns with empty strings
//                }
//            }
//            tableModel.addRow(completeRow);
//        }
//
//        dataTable.setModel(tableModel);
//        // Adjust column widths
//        for (int i = 0; i < dataTable.getColumnCount(); i++) {
//            dataTable.getColumnModel().getColumn(i).setPreferredWidth(150);
//        }
//    }
//
//    /**
//     * Initiates the sorting process using SwingWorker to keep UI responsive.
//     */
//    private void performSorting() {
//        String selectedColumn = (String) columnSelector.getSelectedItem();
//        if (selectedColumn == null) {
//            JOptionPane.showMessageDialog(this, "Please select a column to sort.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        int columnIndex = Arrays.asList(headers).indexOf(selectedColumn);
//        columnData = new ArrayList<>();
//
//        // Extract numeric data from the selected column
//        for (String[] row : csvReader.getData()) {
//            if (columnIndex < row.length) {
//                String value = row[columnIndex].trim();
//                if (value.isEmpty()) continue; // Skip empty values
//                try {
//                    double num = Double.parseDouble(value);
//                    columnData.add(num);
//                } catch (NumberFormatException e) {
//                    // Skip non-numeric or invalid data
//                }
//            }
//        }
//
//        if (columnData.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Selected column contains no valid numeric data.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Convert List<Double> to double[]
//        double[] dataArray = columnData.stream().mapToDouble(Double::doubleValue).toArray();
//
//        // Perform sorting and measure execution time in a background thread
//        progressBar.setVisible(true);
//        sortButton.setEnabled(false);
//        columnSelector.setEnabled(false);
//        uploadButton.setEnabled(false);
//
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            private Exception sortException = null;
//
//            @Override
//            protected Void doInBackground() {
//                try {
//                    performAllSorts(dataArray.clone(), selectedColumn);
//                } catch (Exception e) {
//                    sortException = e;
//                }
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                progressBar.setVisible(false);
//                sortButton.setEnabled(true);
//                columnSelector.setEnabled(true);
//                uploadButton.setEnabled(true);
//
//                if (sortException != null) {
//                    JOptionPane.showMessageDialog(SortingGUI.this,
//                            "An error occurred during sorting: " + sortException.getMessage(),
//                            "Sorting Error",
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        };
//        worker.execute();
//    }
//
//    /**
//     * Performs all sorting algorithms and records their execution times.
//     *
//     * @param data           The data array to sort.
//     * @param selectedColumn The name of the selected column.
//     */
//    private void performAllSorts(double[] data, String selectedColumn) {
//        StringBuilder result = new StringBuilder();
//        Map<String, Long> executionTimes = new LinkedHashMap<>();
//
//        // Insertion Sort
//        double[] insertionData = data.clone();
//        long startTime = System.nanoTime();
//        InsertionSort.sort(insertionData);
//        long endTime = System.nanoTime();
//        executionTimes.put("Insertion Sort", endTime - startTime);
//
//        // Shell Sort
//        double[] shellData = data.clone();
//        startTime = System.nanoTime();
//        ShellSort.sort(shellData);
//        endTime = System.nanoTime();
//        executionTimes.put("Shell Sort", endTime - startTime);
//
//        // Merge Sort
//        double[] mergeData = data.clone();
//        startTime = System.nanoTime();
//        MergeSort.sort(mergeData, 0, mergeData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Merge Sort", endTime - startTime);
//
//        // Quick Sort
//        double[] quickData = data.clone();
//        startTime = System.nanoTime();
//        QuickSort.sort(quickData, 0, quickData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Quick Sort", endTime - startTime);
//
//        // Heap Sort
//        double[] heapData = data.clone();
//        startTime = System.nanoTime();
//        HeapSort.sort(heapData);
//        endTime = System.nanoTime();
//        executionTimes.put("Heap Sort", endTime - startTime);
//
//        // Display execution times
//        result.append("Sorting Algorithm Performance (in nanoseconds):\n");
//        String bestAlgorithm = null;
//        long bestTime = Long.MAX_VALUE;
//
//        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
//            result.append(String.format("%-15s : %d ns\n", entry.getKey(), entry.getValue()));
//            if (entry.getValue() < bestTime) {
//                bestTime = entry.getValue();
//                bestAlgorithm = entry.getKey();
//            }
//        }
//
//        result.append(String.format("\nBest Performing Algorithm: %s (%d ns)", bestAlgorithm, bestTime));
//        // Update the result area on the EDT
//        SwingUtilities.invokeLater(() -> resultArea.setText(result.toString()));
//
//        // Visualize Original and Sorted Data
//        visualizeData(data, bestAlgorithm, executionTimes.get(bestAlgorithm));
//    }
//
//    /**
//     * Visualizes the original and sorted data using JFreeChart.
//     *
//     * @param originalData  The original data array.
//     * @param bestAlgorithm The name of the best performing algorithm.
//     * @param bestTime      The execution time of the best performing algorithm.
//     */
//    private void visualizeData(double[] originalData, String bestAlgorithm, long bestTime) {
//        // Create datasets
//        DefaultCategoryDataset originalDataset = new DefaultCategoryDataset();
//        DefaultCategoryDataset sortedDataset = new DefaultCategoryDataset();
//
//        // To avoid cluttering the chart, we'll plot only the first 100 data points
//        int limit = Math.min(originalData.length, 100);
//
//        for (int i = 0; i < limit; i++) {
//            originalDataset.addValue(originalData[i], "Original", Integer.toString(i + 1));
//        }
//
//        // Sort the data using the best algorithm for visualization
//        double[] sortedData = originalData.clone();
//        switch (bestAlgorithm) {
//            case "Insertion Sort":
//                InsertionSort.sort(sortedData);
//                break;
//            case "Shell Sort":
//                ShellSort.sort(sortedData);
//                break;
//            case "Merge Sort":
//                MergeSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Quick Sort":
//                QuickSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Heap Sort":
//                HeapSort.sort(sortedData);
//                break;
//            default:
//                // Default to original data if algorithm not recognized
//                break;
//        }
//
//        for (int i = 0; i < limit; i++) {
//            sortedDataset.addValue(sortedData[i], "Sorted", Integer.toString(i + 1));
//        }
//
//        // Create charts
//        JFreeChart originalChart = ChartFactory.createLineChart(
//                "Original Data",
//                "Index",
//                "Value",
//                originalDataset
//        );
//
//        JFreeChart sortedChart = ChartFactory.createLineChart(
//                "Sorted Data",
//                "Index",
//                "Value",
//                sortedDataset
//        );
//
//        // Clear previous charts and update on the EDT
//        SwingUtilities.invokeLater(() -> {
//            chartContainer.removeAll();
//
//            // Add charts to the chart container
//            JPanel chartsPanel = new JPanel(new GridLayout(2, 1));
//            chartsPanel.add(new ChartPanel(originalChart));
//            chartsPanel.add(new ChartPanel(sortedChart));
//
//            chartContainer.add(chartsPanel, BorderLayout.CENTER);
//            chartContainer.validate();
//            chartContainer.repaint();
//        });
//    }
//
//    /**
//     * Clears the chart container.
//     */
//    private void clearChart() {
//        chartContainer.removeAll();
//        chartContainer.revalidate();
//        chartContainer.repaint();
//    }
//
//    /**
//     * Sets a custom font for all UI components.
//     *
//     * @param f The FontUIResource to apply.
//     */
//    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
//        Enumeration<Object> keys = UIManager.getDefaults().keys();
//        while (keys.hasMoreElements()) {
//            Object key = keys.nextElement();
//            Object value = UIManager.get(key);
//            if (value instanceof javax.swing.plaf.FontUIResource)
//                UIManager.put(key, f);
//        }
//    }
//
//    /**
//     * Custom Rounded Border class to create rounded edges for buttons.
//     */
//    public class RoundedBorder implements Border {
//
//        private int radius;
//
//        RoundedBorder(int radius) {
//            this.radius = radius;
//        }
//
//        public Insets getBorderInsets(Component c) {
//            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
//        }
//
//        public boolean isBorderOpaque() {
//            return false;
//        }
//
//        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            g.setColor(c.getBackground());
//            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
//        }
//    }
//
//    /**
//     * Main method to run the SortingGUI application.
//     */
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SortingGUI gui = new SortingGUI();
//            gui.setVisible(true);
//        });
//    }
//}



//package com.sortingapp;
//
//import com.sortingapp.sortingalgorithms.*;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.border.TitledBorder;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.net.URL;
//import java.util.*;
//import java.util.List;
//
///**
// * GUI class for the Sorting Algorithm Performance Evaluator application.
// */
//public class SortingGUI extends JFrame {
//
//    private JButton uploadButton;
//    private JComboBox<String> columnSelector;
//    private JButton sortButton;
//    private JTextArea resultArea;
//    private JTable dataTable;
//    private JScrollPane tableScrollPane;
//    private JPanel chartContainer;
//    private JProgressBar progressBar;
//
//    private CSVReaderUtil csvReader;
//    private List<Double> columnData;
//    private List<Integer> numericColumnIndices;
//    private String[] headers;
//    private String currentFilePath;
//
//    public SortingGUI() {
//        // Set the Nimbus look and feel
//        setNimbusLookAndFeel();
//
//        setTitle("Sorting Algorithm Performance Evaluator");
//        setSize(1300, 900);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Set a custom font for the entire application
//        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14));
//
//        // Set background color
//        getContentPane().setBackground(new Color(245, 245, 245));
//
//        csvReader = new CSVReaderUtil();
//
//        initUI();
//    }
//
//    /**
//     * Initializes the user interface components with enhanced design.
//     */
//    private void initUI() {
//        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
//        mainPanel.setBackground(new Color(245, 245, 245));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Top Panel for Upload and Column Selection
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
//        topPanel.setBackground(new Color(220, 220, 220));
//        topPanel.setBorder(BorderFactory.createTitledBorder(createTitleBorder("File and Column Selection")));
//
//        // Upload Button with Optional Icon
//        uploadButton = new JButton("Upload CSV");
//        uploadButton.setBackground(new Color(70, 130, 180));
//        uploadButton.setForeground(Color.WHITE);
//        uploadButton.setFocusPainted(false);
//        uploadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        uploadButton.setBorder(new RoundedBorder(10));
//        uploadButton.setMargin(new Insets(5, 15, 5, 15));
//        uploadButton.setToolTipText("Upload a CSV file");
//        // Attempt to load icon; if not found, proceed without it
//        setButtonIcon(uploadButton, "/icons/upload.png");
//        uploadButton.addActionListener(e -> uploadCSV());
//
//        // Column Selector Label
//        JLabel selectColumnLabel = new JLabel("Select Numeric Column:");
//        selectColumnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
//
//        // Column Selector ComboBox
//        columnSelector = new JComboBox<>();
//        columnSelector.setEnabled(false);
//        columnSelector.setPreferredSize(new Dimension(250, 35));
//        columnSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        columnSelector.setBackground(Color.WHITE);
//        columnSelector.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        columnSelector.setToolTipText("Choose a column containing numeric data for sorting");
//
//        // Sort Button with Optional Icon
//        sortButton = new JButton("Sort");
//        sortButton.setBackground(new Color(34, 139, 34));
//        sortButton.setForeground(Color.WHITE);
//        sortButton.setFocusPainted(false);
//        sortButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        sortButton.setBorder(new RoundedBorder(10));
//        sortButton.setMargin(new Insets(5, 15, 5, 15));
//        sortButton.setToolTipText("Perform sorting on selected column");
//        // Attempt to load icon; if not found, proceed without it
//        setButtonIcon(sortButton, "/icons/sort.png");
//        sortButton.setEnabled(false);
//        sortButton.addActionListener(e -> performSorting());
//
//        // Progress Bar
//        progressBar = new JProgressBar();
//        progressBar.setVisible(false);
//        progressBar.setPreferredSize(new Dimension(200, 25));
//        progressBar.setIndeterminate(true);
//        progressBar.setForeground(new Color(70, 130, 180));
//        progressBar.setToolTipText("Sorting in progress... Please wait.");
//
//        // Add components to the top panel
//        topPanel.add(uploadButton);
//        topPanel.add(selectColumnLabel);
//        topPanel.add(columnSelector);
//        topPanel.add(sortButton);
//        topPanel.add(progressBar);
//
//        // Center Panel for Data Table and Chart
//        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        centerSplitPane.setResizeWeight(0.5);
//        centerSplitPane.setDividerSize(10);
//        centerSplitPane.setDividerLocation(650);
//
//        // Data Table with Enhanced Design
//        dataTable = new JTable();
//        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        dataTable.setGridColor(new Color(200, 200, 200));
//        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
//        dataTable.setRowHeight(25);
//        dataTable.setSelectionBackground(new Color(184, 207, 229));
//        dataTable.setSelectionForeground(Color.BLACK);
//        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        dataTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
//
//        tableScrollPane = new JScrollPane(dataTable);
//        tableScrollPane.setBorder(BorderFactory.createTitledBorder(createTitleBorder("CSV Data Preview")));
//
//        centerSplitPane.setLeftComponent(tableScrollPane);
//
//        // Chart Container with Enhanced Design
//        chartContainer = new JPanel(new BorderLayout());
//        chartContainer.setBackground(Color.WHITE);
//        chartContainer.setBorder(BorderFactory.createTitledBorder(createTitleBorder("Data Visualization")));
//        centerSplitPane.setRightComponent(chartContainer);
//
//        // Bottom Panel for Results
//        resultArea = new JTextArea(15, 30);
//        resultArea.setEditable(false);
//        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
//        resultArea.setBackground(new Color(245, 245, 245));
//        resultArea.setBorder(BorderFactory.createTitledBorder(createTitleBorder("Sorting Results")));
//        resultArea.setToolTipText("Displays the performance of each sorting algorithm");
//
//        JScrollPane resultScrollPane = new JScrollPane(resultArea);
//        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//        // Add components to main panel
//        mainPanel.add(topPanel, BorderLayout.NORTH);
//        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
//        mainPanel.add(resultScrollPane, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    /**
//     * Attempts to set an icon for a button. If the icon is not found, it skips setting the icon.
//     *
//     * @param button   The JButton to set the icon on.
//     * @param iconPath The path to the icon resource.
//     */
//    private void setButtonIcon(JButton button, String iconPath) {
//        URL iconURL = getClass().getResource(iconPath);
//        if (iconURL != null) {
//            ImageIcon icon = new ImageIcon(iconURL);
//            // Optionally, scale the icon to fit the button
//            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
//            button.setIcon(new ImageIcon(img));
//        } else {
//            // Icon not found; proceed without setting it
//            System.err.println("Icon not found: " + iconPath);
//        }
//    }
//
//    /**
//     * Sets the Nimbus look and feel for the application.
//     */
//    private void setNimbusLookAndFeel() {
//        try {
//            boolean nimbusFound = false;
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    nimbusFound = true;
//                    break;
//                }
//            }
//            if (!nimbusFound) {
//                // If Nimbus is not available, set the default look and feel.
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            }
//        } catch (Exception e) {
//            // If Nimbus is not available, you can set the GUI to another look and feel.
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Handles the CSV upload functionality with proper edge case handling.
//     */
//    private void uploadCSV() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
//        int option = fileChooser.showOpenDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
//            currentFilePath = filepath;
//            boolean success = csvReader.readCSV(filepath);
//            if (success) {
//                headers = csvReader.getHeaders();
//                numericColumnIndices = csvReader.getNumericColumnIndices();
//                if (headers == null || headers.length == 0) {
//                    JOptionPane.showMessageDialog(this, "The CSV file has no headers.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                if (csvReader.getData().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "The CSV file is empty.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                populateColumns();
//                displayDataTable();
//                clearChart();
//                resultArea.setText("");
//            } else {
//                JOptionPane.showMessageDialog(this, "Failed to read the CSV file. Please ensure it's properly formatted.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } else if (option == JFileChooser.CANCEL_OPTION) {
//            // User canceled the file chooser
//            JOptionPane.showMessageDialog(this, "File upload canceled.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Populates the column selector ComboBox with numeric columns.
//     */
//    private void populateColumns() {
//        columnSelector.removeAllItems();
//        for (Integer index : numericColumnIndices) {
//            columnSelector.addItem(headers[index]);
//        }
//        if (!numericColumnIndices.isEmpty()) {
//            columnSelector.setEnabled(true);
//            sortButton.setEnabled(true);
//        } else {
//            columnSelector.setEnabled(false);
//            sortButton.setEnabled(false);
//            JOptionPane.showMessageDialog(this, "No numeric columns found in the CSV file.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Displays the CSV data in the JTable with proper handling of inconsistent rows.
//     */
//    private void displayDataTable() {
//        List<String[]> data = csvReader.getData();
//        String[] tableHeaders = headers;
//        DefaultTableModel tableModel = new DefaultTableModel(tableHeaders, 0) {
//            // Make cells non-editable
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//
//        for (String[] row : data) {
//            // Ensure the row has the same number of columns as headers
//            String[] completeRow = new String[headers.length];
//            for (int i = 0; i < headers.length; i++) {
//                if (i < row.length) {
//                    completeRow[i] = row[i].trim();
//                } else {
//                    completeRow[i] = ""; // Fill missing columns with empty strings
//                }
//            }
//            tableModel.addRow(completeRow);
//        }
//
//        dataTable.setModel(tableModel);
//        // Adjust column widths
//        for (int i = 0; i < dataTable.getColumnCount(); i++) {
//            dataTable.getColumnModel().getColumn(i).setPreferredWidth(150);
//        }
//    }
//
//    /**
//     * Initiates the sorting process using SwingWorker to keep UI responsive.
//     */
//    private void performSorting() {
//        String selectedColumn = (String) columnSelector.getSelectedItem();
//        if (selectedColumn == null) {
//            JOptionPane.showMessageDialog(this, "Please select a column to sort.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        int columnIndex = Arrays.asList(headers).indexOf(selectedColumn);
//        columnData = new ArrayList<>();
//
//        // Extract numeric data from the selected column
//        for (String[] row : csvReader.getData()) {
//            if (columnIndex < row.length) {
//                String value = row[columnIndex].trim();
//                if (value.isEmpty()) continue; // Skip empty values
//                try {
//                    double num = Double.parseDouble(value);
//                    columnData.add(num);
//                } catch (NumberFormatException e) {
//                    // Skip non-numeric or invalid data
//                }
//            }
//        }
//
//        if (columnData.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Selected column contains no valid numeric data.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Convert List<Double> to double[]
//        double[] dataArray = columnData.stream().mapToDouble(Double::doubleValue).toArray();
//
//        // Perform sorting and measure execution time in a background thread
//        progressBar.setVisible(true);
//        sortButton.setEnabled(false);
//        columnSelector.setEnabled(false);
//        uploadButton.setEnabled(false);
//
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            private Exception sortException = null;
//
//            @Override
//            protected Void doInBackground() {
//                try {
//                    performAllSorts(dataArray.clone(), selectedColumn);
//                } catch (Exception e) {
//                    sortException = e;
//                }
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                progressBar.setVisible(false);
//                sortButton.setEnabled(true);
//                columnSelector.setEnabled(true);
//                uploadButton.setEnabled(true);
//
//                if (sortException != null) {
//                    JOptionPane.showMessageDialog(SortingGUI.this,
//                            "An error occurred during sorting: " + sortException.getMessage(),
//                            "Sorting Error",
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        };
//        worker.execute();
//    }
//
//    /**
//     * Performs all sorting algorithms and records their execution times.
//     *
//     * @param data           The data array to sort.
//     * @param selectedColumn The name of the selected column.
//     */
//    private void performAllSorts(double[] data, String selectedColumn) {
//        StringBuilder result = new StringBuilder();
//        Map<String, Long> executionTimes = new LinkedHashMap<>();
//
//        // Insertion Sort
//        double[] insertionData = data.clone();
//        long startTime = System.nanoTime();
//        InsertionSort.sort(insertionData);
//        long endTime = System.nanoTime();
//        executionTimes.put("Insertion Sort", endTime - startTime);
//
//        // Shell Sort
//        double[] shellData = data.clone();
//        startTime = System.nanoTime();
//        ShellSort.sort(shellData);
//        endTime = System.nanoTime();
//        executionTimes.put("Shell Sort", endTime - startTime);
//
//        // Merge Sort
//        double[] mergeData = data.clone();
//        startTime = System.nanoTime();
//        MergeSort.sort(mergeData, 0, mergeData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Merge Sort", endTime - startTime);
//
//        // Quick Sort
//        double[] quickData = data.clone();
//        startTime = System.nanoTime();
//        QuickSort.sort(quickData, 0, quickData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Quick Sort", endTime - startTime);
//
//        // Heap Sort
//        double[] heapData = data.clone();
//        startTime = System.nanoTime();
//        HeapSort.sort(heapData);
//        endTime = System.nanoTime();
//        executionTimes.put("Heap Sort", endTime - startTime);
//
//        // Display execution times
//        result.append("Sorting Algorithm Performance (in nanoseconds):\n\n");
//        String bestAlgorithm = null;
//        long bestTime = Long.MAX_VALUE;
//
//        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
//            result.append(String.format("%-15s : %d ns\n", entry.getKey(), entry.getValue()));
//            if (entry.getValue() < bestTime) {
//                bestTime = entry.getValue();
//                bestAlgorithm = entry.getKey();
//            }
//        }
//
//        result.append(String.format("\nBest Performing Algorithm: %s (%d ns)", bestAlgorithm, bestTime));
//        // Update the result area on the EDT
//        SwingUtilities.invokeLater(() -> resultArea.setText(result.toString()));
//
//        // Visualize Original and Sorted Data
//        visualizeData(data, bestAlgorithm, executionTimes.get(bestAlgorithm));
//    }
//
//    /**
//     * Visualizes the original and sorted data using JFreeChart with enhanced visuals.
//     *
//     * @param originalData  The original data array.
//     * @param bestAlgorithm The name of the best performing algorithm.
//     * @param bestTime      The execution time of the best performing algorithm.
//     */
//    private void visualizeData(double[] originalData, String bestAlgorithm, long bestTime) {
//        // Create datasets
//        DefaultCategoryDataset originalDataset = new DefaultCategoryDataset();
//        DefaultCategoryDataset sortedDataset = new DefaultCategoryDataset();
//
//        // To avoid cluttering the chart, we'll plot only the first 100 data points
//        int limit = Math.min(originalData.length, 100);
//
//        for (int i = 0; i < limit; i++) {
//            originalDataset.addValue(originalData[i], "Original", Integer.toString(i + 1));
//        }
//
//        // Sort the data using the best algorithm for visualization
//        double[] sortedData = originalData.clone();
//        switch (bestAlgorithm) {
//            case "Insertion Sort":
//                InsertionSort.sort(sortedData);
//                break;
//            case "Shell Sort":
//                ShellSort.sort(sortedData);
//                break;
//            case "Merge Sort":
//                MergeSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Quick Sort":
//                QuickSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Heap Sort":
//                HeapSort.sort(sortedData);
//                break;
//            default:
//                // Default to original data if algorithm not recognized
//                break;
//        }
//
//        for (int i = 0; i < limit; i++) {
//            sortedDataset.addValue(sortedData[i], "Sorted", Integer.toString(i + 1));
//        }
//
//        // Create charts with enhanced aesthetics
//        JFreeChart originalChart = ChartFactory.createLineChart(
//                "Original Data",
//                "Index",
//                "Value",
//                originalDataset
//        );
//
//        JFreeChart sortedChart = ChartFactory.createLineChart(
//                "Sorted Data",
//                "Index",
//                "Value",
//                sortedDataset
//        );
//
//        // Customize chart appearance
//        customizeChart(originalChart, Color.BLUE);
//        customizeChart(sortedChart, Color.RED);
//
//        // Clear previous charts and update on the EDT
//        SwingUtilities.invokeLater(() -> {
//            chartContainer.removeAll();
//
//            // Add charts to the chart container
//            JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
//            chartsPanel.setBackground(Color.WHITE);
//            chartsPanel.add(new ChartPanel(originalChart));
//            chartsPanel.add(new ChartPanel(sortedChart));
//
//            chartContainer.add(chartsPanel, BorderLayout.CENTER);
//            chartContainer.validate();
//            chartContainer.repaint();
//        });
//    }
//
//    /**
//     * Customizes the appearance of a JFreeChart.
//     *
//     * @param chart The chart to customize.
//     * @param color The color to apply to the chart lines.
//     */
//    private void customizeChart(JFreeChart chart, Color color) {
//        chart.setBackgroundPaint(Color.WHITE);
//        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
//        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, color);
//        chart.getCategoryPlot().getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
//        chart.getCategoryPlot().getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
//        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 13));
//    }
//
//    /**
//     * Clears the chart container.
//     */
//    private void clearChart() {
//        chartContainer.removeAll();
//        chartContainer.revalidate();
//        chartContainer.repaint();
//    }
//
//    /**
//     * Sets a custom font for all UI components.
//     *
//     * @param f The FontUIResource to apply.
//     */
//    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
//        Enumeration<Object> keys = UIManager.getDefaults().keys();
//        while (keys.hasMoreElements()) {
//            Object key = keys.nextElement();
//            Object value = UIManager.get(key);
//            if (value instanceof javax.swing.plaf.FontUIResource)
//                UIManager.put(key, f);
//        }
//    }
//
//    /**
//     * Creates a titled border with custom styling.
//     *
//     * @param title The title of the border.
//     * @return A TitledBorder with custom fonts and colors.
//     */
//    private TitledBorder createTitleBorder(String title) {
//        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1), title);
//        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
//        border.setTitleColor(new Color(70, 130, 180));
//        return border;
//    }
//
//    /**
//     * Custom Rounded Border class to create rounded edges for buttons and panels.
//     */
//    public class RoundedBorder implements Border {
//
//        private int radius;
//
//        RoundedBorder(int radius) {
//            this.radius = radius;
//        }
//
//        public Insets getBorderInsets(Component c) {
//            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
//        }
//
//        public boolean isBorderOpaque() {
//            return false;
//        }
//
//        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            Graphics2D g2 = (Graphics2D) g;
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(new Color(200, 200, 200));
//            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
//        }
//    }
//
//    /**
//     * Main method to run the SortingGUI application.
//     */
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SortingGUI gui = new SortingGUI();
//            gui.setVisible(true);
//        });
//    }
//}


//package com.sortingapp;
//
//import com.sortingapp.sortingalgorithms.*;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.border.TitledBorder;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.net.URL;
//import java.util.*;
//import java.util.List;
//
///**
// * GUI class for the Sorting Algorithm Performance Evaluator application.
// */
//public class SortingGUI extends JFrame {
//
//    private JButton uploadButton;
//    private JComboBox<String> columnSelector;
//    private JButton sortButton;
//    private JTextArea resultArea;
//    private JTable dataTable;
//    private JScrollPane tableScrollPane;
//    private JPanel chartContainer;
//    private JProgressBar progressBar;
//
//    private CSVReaderUtil csvReader;
//    private List<Double> columnData;
//    private List<Integer> numericColumnIndices;
//    private String[] headers;
//    private String currentFilePath;
//
//    public SortingGUI() {
//        // Set the Nimbus look and feel
//        setNimbusLookAndFeel();
//
//        setTitle("Sorting Algorithm Performance Evaluator");
//        setSize(1300, 900);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Set a custom font for the entire application
//        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14));
//
//        // Set background color
//        getContentPane().setBackground(new Color(245, 245, 245));
//
//        csvReader = new CSVReaderUtil();
//
//        initUI();
//    }
//
//    /**
//     * Initializes the user interface components with enhanced design and adds the menu bar.
//     */
//    private void initUI() {
//        // Initialize Menu Bar
//        createMenuBar();
//
//        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
//        mainPanel.setBackground(new Color(245, 245, 245));
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // Top Panel for Upload and Column Selection
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
//        topPanel.setBackground(new Color(220, 220, 220));
//        topPanel.setBorder(BorderFactory.createTitledBorder(createTitleBorder("File and Column Selection")));
//
//        // Upload Button with Optional Icon
//        uploadButton = new JButton("Upload CSV");
//        uploadButton.setBackground(new Color(70, 130, 180));
//        uploadButton.setForeground(Color.WHITE);
//        uploadButton.setFocusPainted(false);
//        uploadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        uploadButton.setBorder(new RoundedBorder(10));
//        uploadButton.setMargin(new Insets(5, 15, 5, 15));
//        uploadButton.setToolTipText("Upload a CSV file");
//        // Attempt to load icon; if not found, proceed without it
//        setButtonIcon(uploadButton, "/icons/upload.png");
//        uploadButton.addActionListener(e -> uploadCSV());
//
//        // Column Selector Label
//        JLabel selectColumnLabel = new JLabel("Select Numeric Column:");
//        selectColumnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
//
//        // Column Selector ComboBox
//        columnSelector = new JComboBox<>();
//        columnSelector.setEnabled(false);
//        columnSelector.setPreferredSize(new Dimension(250, 35));
//        columnSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        columnSelector.setBackground(Color.WHITE);
//        columnSelector.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        columnSelector.setToolTipText("Choose a column containing numeric data for sorting");
//
//        // Sort Button with Optional Icon
//        sortButton = new JButton("Sort");
//        sortButton.setBackground(new Color(34, 139, 34));
//        sortButton.setForeground(Color.WHITE);
//        sortButton.setFocusPainted(false);
//        sortButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        sortButton.setBorder(new RoundedBorder(10));
//        sortButton.setMargin(new Insets(5, 15, 5, 15));
//        sortButton.setToolTipText("Perform sorting on selected column");
//        // Attempt to load icon; if not found, proceed without it
//        setButtonIcon(sortButton, "/icons/sort.png");
//        sortButton.setEnabled(false);
//        sortButton.addActionListener(e -> performSorting());
//
//        // Progress Bar
//        progressBar = new JProgressBar();
//        progressBar.setVisible(false);
//        progressBar.setPreferredSize(new Dimension(200, 25));
//        progressBar.setIndeterminate(true);
//        progressBar.setForeground(new Color(70, 130, 180));
//        progressBar.setToolTipText("Sorting in progress... Please wait.");
//
//        // Add components to the top panel
//        topPanel.add(uploadButton);
//        topPanel.add(selectColumnLabel);
//        topPanel.add(columnSelector);
//        topPanel.add(sortButton);
//        topPanel.add(progressBar);
//
//        // Center Panel for Data Table and Chart
//        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        centerSplitPane.setResizeWeight(0.5);
//        centerSplitPane.setDividerSize(10);
//        centerSplitPane.setDividerLocation(650);
//
//        // Data Table with Enhanced Design
//        dataTable = new JTable();
//        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
//        dataTable.setGridColor(new Color(200, 200, 200));
//        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
//        dataTable.setRowHeight(25);
//        dataTable.setSelectionBackground(new Color(184, 207, 229));
//        dataTable.setSelectionForeground(Color.BLACK);
//        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        dataTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering
//
//        tableScrollPane = new JScrollPane(dataTable);
//        tableScrollPane.setBorder(BorderFactory.createTitledBorder(createTitleBorder("CSV Data Preview")));
//
//        centerSplitPane.setLeftComponent(tableScrollPane);
//
//        // Chart Container with Enhanced Design
//        chartContainer = new JPanel(new BorderLayout());
//        chartContainer.setBackground(Color.WHITE);
//        chartContainer.setBorder(BorderFactory.createTitledBorder(createTitleBorder("Data Visualization")));
//        centerSplitPane.setRightComponent(chartContainer);
//
//        // Bottom Panel for Results
//        resultArea = new JTextArea(15, 30);
//        resultArea.setEditable(false);
//        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
//        resultArea.setBackground(new Color(245, 245, 245));
//        resultArea.setBorder(BorderFactory.createTitledBorder(createTitleBorder("Sorting Results")));
//        resultArea.setToolTipText("Displays the performance of each sorting algorithm");
//
//        JScrollPane resultScrollPane = new JScrollPane(resultArea);
//        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//        // Add components to main panel
//        mainPanel.add(topPanel, BorderLayout.NORTH);
//        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
//        mainPanel.add(resultScrollPane, BorderLayout.SOUTH);
//
//        add(mainPanel);
//    }
//
//    /**
//     * Creates and sets the menu bar with "File" and "Help" menus.
//     */
//    private void createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        // "File" Menu
//        JMenu fileMenu = new JMenu("File");
//        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//
//        // "Exit" Menu Item
//        JMenuItem exitMenuItem = new JMenuItem("Exit");
//        exitMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        exitMenuItem.setToolTipText("Exit the application");
//        exitMenuItem.addActionListener(e -> exitApplication());
//        fileMenu.add(exitMenuItem);
//
//        // "Help" Menu
//        JMenu helpMenu = new JMenu("Help");
//        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//
//        // "About" Menu Item
//        JMenuItem aboutMenuItem = new JMenuItem("About");
//        aboutMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        aboutMenuItem.setToolTipText("View information about the application");
//        aboutMenuItem.addActionListener(e -> showAboutDialog());
//        helpMenu.add(aboutMenuItem);
//
//        // Add menus to the menu bar
//        menuBar.add(fileMenu);
//        menuBar.add(helpMenu);
//
//        setJMenuBar(menuBar);
//    }
//
//    /**
//     * Displays the "About" dialog with application information.
//     */
//    private void showAboutDialog() {
//        String message = "<html><body style='width: 300px;'>" +
//                "<h2>Sorting Algorithm Performance Evaluator</h2>" +
//                "<p>Version: 1.0</p>" +
//                "<p>Author: Gayan Jayasanka & Chamudhi Kaveesha</p>" +
//                "<p>This application allows users to upload CSV files, select numeric columns, " +
//                "perform sorting using various algorithms, and visualize the performance results.</p>" +
//                "</body></html>";
//
//        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    /**
//     * Exits the application gracefully after confirmation.
//     */
//    private void exitApplication() {
//        int confirm = JOptionPane.showConfirmDialog(this,
//                "Are you sure you want to exit?",
//                "Exit Confirmation",
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE);
//        if (confirm == JOptionPane.YES_OPTION) {
//            System.exit(0);
//        }
//    }
//
//    /**
//     * Attempts to set an icon for a button. If the icon is not found, it skips setting the icon.
//     *
//     * @param button   The JButton to set the icon on.
//     * @param iconPath The path to the icon resource.
//     */
//    private void setButtonIcon(JButton button, String iconPath) {
//        URL iconURL = getClass().getResource(iconPath);
//        if (iconURL != null) {
//            ImageIcon icon = new ImageIcon(iconURL);
//            // Optionally, scale the icon to fit the button
//            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
//            button.setIcon(new ImageIcon(img));
//        } else {
//            // Icon not found; proceed without setting it
//            System.err.println("Icon not found: " + iconPath);
//        }
//    }
//
//    /**
//     * Sets the Nimbus look and feel for the application.
//     */
//    private void setNimbusLookAndFeel() {
//        try {
//            boolean nimbusFound = false;
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    nimbusFound = true;
//                    break;
//                }
//            }
//            if (!nimbusFound) {
//                // If Nimbus is not available, set the default look and feel.
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            }
//        } catch (Exception e) {
//            // If Nimbus is not available, you can set the GUI to another look and feel.
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Handles the CSV upload functionality with proper edge case handling.
//     */
//    private void uploadCSV() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
//        int option = fileChooser.showOpenDialog(this);
//        if (option == JFileChooser.APPROVE_OPTION) {
//            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
//            currentFilePath = filepath;
//            boolean success = csvReader.readCSV(filepath);
//            if (success) {
//                headers = csvReader.getHeaders();
//                numericColumnIndices = csvReader.getNumericColumnIndices();
//                if (headers == null || headers.length == 0) {
//                    JOptionPane.showMessageDialog(this, "The CSV file has no headers.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                if (csvReader.getData().isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "The CSV file is empty.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                populateColumns();
//                displayDataTable();
//                clearChart();
//                resultArea.setText("");
//            } else {
//                JOptionPane.showMessageDialog(this, "Failed to read the CSV file. Please ensure it's properly formatted.", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        } else if (option == JFileChooser.CANCEL_OPTION) {
//            // User canceled the file chooser
//            JOptionPane.showMessageDialog(this, "File upload canceled.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Populates the column selector ComboBox with numeric columns.
//     */
//    private void populateColumns() {
//        columnSelector.removeAllItems();
//        for (Integer index : numericColumnIndices) {
//            columnSelector.addItem(headers[index]);
//        }
//        if (!numericColumnIndices.isEmpty()) {
//            columnSelector.setEnabled(true);
//            sortButton.setEnabled(true);
//        } else {
//            columnSelector.setEnabled(false);
//            sortButton.setEnabled(false);
//            JOptionPane.showMessageDialog(this, "No numeric columns found in the CSV file.", "Information", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//
//    /**
//     * Displays the CSV data in the JTable with proper handling of inconsistent rows.
//     */
//    private void displayDataTable() {
//        List<String[]> data = csvReader.getData();
//        String[] tableHeaders = headers;
//        DefaultTableModel tableModel = new DefaultTableModel(tableHeaders, 0) {
//            // Make cells non-editable
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//
//        for (String[] row : data) {
//            // Ensure the row has the same number of columns as headers
//            String[] completeRow = new String[headers.length];
//            for (int i = 0; i < headers.length; i++) {
//                if (i < row.length) {
//                    completeRow[i] = row[i].trim();
//                } else {
//                    completeRow[i] = ""; // Fill missing columns with empty strings
//                }
//            }
//            tableModel.addRow(completeRow);
//        }
//
//        dataTable.setModel(tableModel);
//        // Adjust column widths
//        for (int i = 0; i < dataTable.getColumnCount(); i++) {
//            dataTable.getColumnModel().getColumn(i).setPreferredWidth(150);
//        }
//    }
//
//    /**
//     * Initiates the sorting process using SwingWorker to keep UI responsive.
//     */
//    private void performSorting() {
//        String selectedColumn = (String) columnSelector.getSelectedItem();
//        if (selectedColumn == null) {
//            JOptionPane.showMessageDialog(this, "Please select a column to sort.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        int columnIndex = Arrays.asList(headers).indexOf(selectedColumn);
//        columnData = new ArrayList<>();
//
//        // Extract numeric data from the selected column
//        for (String[] row : csvReader.getData()) {
//            if (columnIndex < row.length) {
//                String value = row[columnIndex].trim();
//                if (value.isEmpty()) continue; // Skip empty values
//                try {
//                    double num = Double.parseDouble(value);
//                    columnData.add(num);
//                } catch (NumberFormatException e) {
//                    // Skip non-numeric or invalid data
//                }
//            }
//        }
//
//        if (columnData.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Selected column contains no valid numeric data.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        // Convert List<Double> to double[]
//        double[] dataArray = columnData.stream().mapToDouble(Double::doubleValue).toArray();
//
//        // Perform sorting and measure execution time in a background thread
//        progressBar.setVisible(true);
//        sortButton.setEnabled(false);
//        columnSelector.setEnabled(false);
//        uploadButton.setEnabled(false);
//
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//            private Exception sortException = null;
//
//            @Override
//            protected Void doInBackground() {
//                try {
//                    performAllSorts(dataArray.clone(), selectedColumn);
//                } catch (Exception e) {
//                    sortException = e;
//                }
//                return null;
//            }
//
//            @Override
//            protected void done() {
//                progressBar.setVisible(false);
//                sortButton.setEnabled(true);
//                columnSelector.setEnabled(true);
//                uploadButton.setEnabled(true);
//
//                if (sortException != null) {
//                    JOptionPane.showMessageDialog(SortingGUI.this,
//                            "An error occurred during sorting: " + sortException.getMessage(),
//                            "Sorting Error",
//                            JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        };
//        worker.execute();
//    }
//
//    /**
//     * Performs all sorting algorithms and records their execution times.
//     *
//     * @param data           The data array to sort.
//     * @param selectedColumn The name of the selected column.
//     */
//    private void performAllSorts(double[] data, String selectedColumn) {
//        StringBuilder result = new StringBuilder();
//        Map<String, Long> executionTimes = new LinkedHashMap<>();
//
//        // Insertion Sort
//        double[] insertionData = data.clone();
//        long startTime = System.nanoTime();
//        InsertionSort.sort(insertionData);
//        long endTime = System.nanoTime();
//        executionTimes.put("Insertion Sort", endTime - startTime);
//
//        // Shell Sort
//        double[] shellData = data.clone();
//        startTime = System.nanoTime();
//        ShellSort.sort(shellData);
//        endTime = System.nanoTime();
//        executionTimes.put("Shell Sort", endTime - startTime);
//
//        // Merge Sort
//        double[] mergeData = data.clone();
//        startTime = System.nanoTime();
//        MergeSort.sort(mergeData, 0, mergeData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Merge Sort", endTime - startTime);
//
//        // Quick Sort
//        double[] quickData = data.clone();
//        startTime = System.nanoTime();
//        QuickSort.sort(quickData, 0, quickData.length - 1);
//        endTime = System.nanoTime();
//        executionTimes.put("Quick Sort", endTime - startTime);
//
//        // Heap Sort
//        double[] heapData = data.clone();
//        startTime = System.nanoTime();
//        HeapSort.sort(heapData);
//        endTime = System.nanoTime();
//        executionTimes.put("Heap Sort", endTime - startTime);
//
//        // Display execution times
//        result.append("Sorting Algorithm Performance (in nanoseconds):\n\n");
//        String bestAlgorithm = null;
//        long bestTime = Long.MAX_VALUE;
//
//        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
//            result.append(String.format("%-15s : %d ns\n", entry.getKey(), entry.getValue()));
//            if (entry.getValue() < bestTime) {
//                bestTime = entry.getValue();
//                bestAlgorithm = entry.getKey();
//            }
//        }
//
//        result.append(String.format("\nBest Performing Algorithm: %s (%d ns)", bestAlgorithm, bestTime));
//        // Update the result area on the EDT
//        SwingUtilities.invokeLater(() -> resultArea.setText(result.toString()));
//
//        // Visualize Original and Sorted Data
//        visualizeData(data, bestAlgorithm, executionTimes.get(bestAlgorithm));
//    }
//
//    /**
//     * Visualizes the original and sorted data using JFreeChart with enhanced visuals.
//     *
//     * @param originalData  The original data array.
//     * @param bestAlgorithm The name of the best performing algorithm.
//     * @param bestTime      The execution time of the best performing algorithm.
//     */
//    private void visualizeData(double[] originalData, String bestAlgorithm, long bestTime) {
//        // Create datasets
//        DefaultCategoryDataset originalDataset = new DefaultCategoryDataset();
//        DefaultCategoryDataset sortedDataset = new DefaultCategoryDataset();
//
//        // To avoid cluttering the chart, we'll plot only the first 100 data points
//        int limit = Math.min(originalData.length, 100);
//
//        for (int i = 0; i < limit; i++) {
//            originalDataset.addValue(originalData[i], "Original", Integer.toString(i + 1));
//        }
//
//        // Sort the data using the best algorithm for visualization
//        double[] sortedData = originalData.clone();
//        switch (bestAlgorithm) {
//            case "Insertion Sort":
//                InsertionSort.sort(sortedData);
//                break;
//            case "Shell Sort":
//                ShellSort.sort(sortedData);
//                break;
//            case "Merge Sort":
//                MergeSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Quick Sort":
//                QuickSort.sort(sortedData, 0, sortedData.length - 1);
//                break;
//            case "Heap Sort":
//                HeapSort.sort(sortedData);
//                break;
//            default:
//                // Default to original data if algorithm not recognized
//                break;
//        }
//
//        for (int i = 0; i < limit; i++) {
//            sortedDataset.addValue(sortedData[i], "Sorted", Integer.toString(i + 1));
//        }
//
//        // Create charts with enhanced aesthetics
//        JFreeChart originalChart = ChartFactory.createLineChart(
//                "Original Data",
//                "Index",
//                "Value",
//                originalDataset
//        );
//
//        JFreeChart sortedChart = ChartFactory.createLineChart(
//                "Sorted Data",
//                "Index",
//                "Value",
//                sortedDataset
//        );
//
//        // Customize chart appearance
//        customizeChart(originalChart, Color.BLUE);
//        customizeChart(sortedChart, Color.RED);
//
//        // Clear previous charts and update on the EDT
//        SwingUtilities.invokeLater(() -> {
//            chartContainer.removeAll();
//
//            // Add charts to the chart container
//            JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
//            chartsPanel.setBackground(Color.WHITE);
//            chartsPanel.add(new ChartPanel(originalChart));
//            chartsPanel.add(new ChartPanel(sortedChart));
//
//            chartContainer.add(chartsPanel, BorderLayout.CENTER);
//            chartContainer.validate();
//            chartContainer.repaint();
//        });
//    }
//
//    /**
//     * Customizes the appearance of a JFreeChart.
//     *
//     * @param chart The chart to customize.
//     * @param color The color to apply to the chart lines.
//     */
//    private void customizeChart(JFreeChart chart, Color color) {
//        chart.setBackgroundPaint(Color.WHITE);
//        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
//        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, color);
//        chart.getCategoryPlot().getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
//        chart.getCategoryPlot().getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
//        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 13));
//    }
//
//    /**
//     * Clears the chart container.
//     */
//    private void clearChart() {
//        chartContainer.removeAll();
//        chartContainer.revalidate();
//        chartContainer.repaint();
//    }
//
//    /**
//     * Sets a custom font for all UI components.
//     *
//     * @param f The FontUIResource to apply.
//     */
//    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
//        Enumeration<Object> keys = UIManager.getDefaults().keys();
//        while (keys.hasMoreElements()) {
//            Object key = keys.nextElement();
//            Object value = UIManager.get(key);
//            if (value instanceof javax.swing.plaf.FontUIResource)
//                UIManager.put(key, f);
//        }
//    }
//
//    /**
//     * Creates a titled border with custom styling.
//     *
//     * @param title The title of the border.
//     * @return A TitledBorder with custom fonts and colors.
//     */
//    private TitledBorder createTitleBorder(String title) {
//        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1), title);
//        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
//        border.setTitleColor(new Color(70, 130, 180));
//        return border;
//    }
//
//    /**
//     * Custom Rounded Border class to create rounded edges for buttons and panels.
//     */
//    public class RoundedBorder implements Border {
//
//        private int radius;
//
//        RoundedBorder(int radius) {
//            this.radius = radius;
//        }
//
//        public Insets getBorderInsets(Component c) {
//            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
//        }
//
//        public boolean isBorderOpaque() {
//            return false;
//        }
//
//        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//            Graphics2D g2 = (Graphics2D) g;
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2.setColor(new Color(200, 200, 200));
//            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
//        }
//    }
//
//    /**
//     * Main method to run the SortingGUI application.
//     */
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SortingGUI gui = new SortingGUI();
//            gui.setVisible(true);
//        });
//    }
//}
//


package com.sortingapp;

import com.sortingapp.sortingalgorithms.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * GUI class for the Sorting Algorithm Performance Evaluator application.
 */
public class SortingGUI extends JFrame {

    private JButton uploadButton;
    private JComboBox<String> columnSelector;
    private JButton sortButton;
    private JTextArea resultArea;
    private JTable dataTable;
    private JScrollPane tableScrollPane;
    private JPanel chartContainer;
    private JProgressBar progressBar;

    private CSVReaderUtil csvReader;
    private List<Double> columnData;
    private List<Integer> numericColumnIndices;
    private String[] headers;
    private String currentFilePath;

    public SortingGUI() {
        // Set the Nimbus look and feel
        setNimbusLookAndFeel();

        setTitle("Sorting Algorithm Performance Evaluator");
        setSize(1600, 900); // Increased width for better distribution
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set a custom font for the entire application
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14));

        // Set background color to a soft pastel (e.g., Alice Blue)
        getContentPane().setBackground(new Color(240, 248, 255));

        csvReader = new CSVReaderUtil();

        initUI();
    }

    /**
     * Initializes the user interface components with enhanced design and adds the menu bar.
     */
    private void initUI() {
        // Initialize Menu Bar
        createMenuBar();

        // Main Panel with Soft Background Color
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel for Upload and Column Selection with Semi-Transparent Background
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Set semi-transparent white background
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 220)); // White with 220 alpha
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        topPanel.setOpaque(false); // Make the panel transparent to show the background
        topPanel.setBorder(BorderFactory.createTitledBorder(createTitleBorder("File and Column Selection")));

        // Upload Button with Optional Icon
        uploadButton = new JButton("Upload CSV");
        uploadButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uploadButton.setBorder(new RoundedBorder(10));
        uploadButton.setMargin(new Insets(5, 15, 5, 15));
        uploadButton.setToolTipText("Upload a CSV file");
        // Attempt to load icon; if not found, proceed without it
        setButtonIcon(uploadButton, "/icons/upload.png");
        uploadButton.addActionListener(e -> uploadCSV());

        // Column Selector Label
        JLabel selectColumnLabel = new JLabel("Select Numeric Column:");
        selectColumnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Column Selector ComboBox
        columnSelector = new JComboBox<>();
        columnSelector.setEnabled(false);
        columnSelector.setPreferredSize(new Dimension(250, 35));
        columnSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        columnSelector.setBackground(Color.WHITE);
        columnSelector.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        columnSelector.setToolTipText("Choose a column containing numeric data for sorting");

        // Sort Button with Optional Icon
        sortButton = new JButton("Sort");
        sortButton.setBackground(new Color(34, 139, 34)); // Forest Green
        sortButton.setForeground(Color.WHITE);
        sortButton.setFocusPainted(false);
        sortButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sortButton.setBorder(new RoundedBorder(10));
        sortButton.setMargin(new Insets(5, 15, 5, 15));
        sortButton.setToolTipText("Perform sorting on selected column");
        // Attempt to load icon; if not found, proceed without it
        setButtonIcon(sortButton, "/icons/sort.png");
        sortButton.setEnabled(false);
        sortButton.addActionListener(e -> performSorting());

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(200, 25));
        progressBar.setIndeterminate(true);
        progressBar.setForeground(new Color(70, 130, 180));
        progressBar.setToolTipText("Sorting in progress... Please wait.");

        // Add components to the top panel
        topPanel.add(uploadButton);
        topPanel.add(selectColumnLabel);
        topPanel.add(columnSelector);
        topPanel.add(sortButton);
        topPanel.add(progressBar);

        // Center SplitPane: Horizontal Split (75% Left for Data & Results, 25% Right for Charts)
        JSplitPane outerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        outerSplitPane.setResizeWeight(0.75); // 75% for left, 25% for right
        outerSplitPane.setDividerSize(10);
        outerSplitPane.setDividerLocation((int) (1600 * 0.75)); // Initial divider location

        // Left Panel: Vertical SplitPane (Data Table on Top, Sorting Results at Bottom)
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftSplitPane.setResizeWeight(0.7); // 70% for data table, 30% for results
        leftSplitPane.setDividerSize(10);
        leftSplitPane.setDividerLocation(600); // Initial divider location

        // Data Table with Enhanced Design
        dataTable = new JTable();
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dataTable.setGridColor(new Color(200, 200, 200));
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        dataTable.setRowHeight(25);
        dataTable.setSelectionBackground(new Color(184, 207, 229));
        dataTable.setSelectionForeground(Color.BLACK);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(createTitleBorder("CSV Data Preview")));
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling

        // Sorting Results Area within ScrollPane
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(255, 255, 255, 200)); // White with 200 alpha
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setToolTipText("Displays the performance of each sorting algorithm");

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(createTitleBorder("Sorting Results")));
        resultScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling

        // Add components to the left split pane
        leftSplitPane.setTopComponent(tableScrollPane);
        leftSplitPane.setBottomComponent(resultScrollPane);

        // Chart Container with Enhanced Design
        chartContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Optional: Add a subtle background color or image
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 220)); // White with slight transparency
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        chartContainer.setOpaque(false); // Make the panel transparent to show the background
        chartContainer.setBorder(BorderFactory.createTitledBorder(createTitleBorder("Data Visualization")));

        // Add components to the outer split pane
        outerSplitPane.setLeftComponent(leftSplitPane);
        outerSplitPane.setRightComponent(chartContainer);

        // Add split pane to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(outerSplitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Creates and sets the menu bar with "File" and "Help" menus.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // "File" Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // "Exit" Menu Item
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        exitMenuItem.setToolTipText("Exit the application");
        exitMenuItem.addActionListener(e -> exitApplication());
        fileMenu.add(exitMenuItem);

        // "Help" Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // "About" Menu Item
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        aboutMenuItem.setToolTipText("View information about the application");
        aboutMenuItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutMenuItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Displays the "About" dialog with application information.
     */
    private void showAboutDialog() {
        String message = "<html><body style='width: 300px;'>" +
                "<h2>Sorting Algorithm Performance Evaluator</h2>" +
                "<p>Version: 1.0</p>" +
                "<p>Author: Gayan Jayasanka & Chamudi Kaveesha</p>" +
                "<p>This application allows users to upload CSV files, select numeric columns, " +
                "perform sorting using various algorithms, and visualize the performance results.</p>" +
                "</body></html>";

        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Exits the application gracefully after confirmation.
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Attempts to set an icon for a button. If the icon is not found, it skips setting the icon.
     *
     * @param button   The JButton to set the icon on.
     * @param iconPath The path to the icon resource.
     */
    private void setButtonIcon(JButton button, String iconPath) {
        URL iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            // Optionally, scale the icon to fit the button
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } else {
            // Icon not found; proceed without setting it
            System.err.println("Icon not found: " + iconPath);
        }
    }

    /**
     * Sets the Nimbus look and feel for the application.
     */
    private void setNimbusLookAndFeel() {
        try {
            boolean nimbusFound = false;
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    nimbusFound = true;
                    break;
                }
            }
            if (!nimbusFound) {
                // If Nimbus is not available, set the default look and feel.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
            e.printStackTrace();
        }
    }

    /**
     * Handles the CSV upload functionality with proper edge case handling.
     */
    private void uploadCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filepath = fileChooser.getSelectedFile().getAbsolutePath();
            currentFilePath = filepath;
            boolean success = csvReader.readCSV(filepath);
            if (success) {
                headers = csvReader.getHeaders();
                numericColumnIndices = csvReader.getNumericColumnIndices();
                if (headers == null || headers.length == 0) {
                    JOptionPane.showMessageDialog(this, "The CSV file has no headers.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (csvReader.getData().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "The CSV file is empty.", "Invalid CSV", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                populateColumns();
                displayDataTable();
                clearChart();
                resultArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to read the CSV file. Please ensure it's properly formatted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (option == JFileChooser.CANCEL_OPTION) {
            // User canceled the file chooser
            JOptionPane.showMessageDialog(this, "File upload canceled.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Populates the column selector ComboBox with numeric columns.
     */
    private void populateColumns() {
        columnSelector.removeAllItems();
        for (Integer index : numericColumnIndices) {
            columnSelector.addItem(headers[index]);
        }
        if (!numericColumnIndices.isEmpty()) {
            columnSelector.setEnabled(true);
            sortButton.setEnabled(true);
        } else {
            columnSelector.setEnabled(false);
            sortButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "No numeric columns found in the CSV file.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Displays the CSV data in the JTable with proper handling of inconsistent rows.
     */
    private void displayDataTable() {
        List<String[]> data = csvReader.getData();
        String[] tableHeaders = headers;
        DefaultTableModel tableModel = new DefaultTableModel(tableHeaders, 0) {
            // Make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (String[] row : data) {
            // Ensure the row has the same number of columns as headers
            String[] completeRow = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                if (i < row.length) {
                    completeRow[i] = row[i].trim();
                } else {
                    completeRow[i] = ""; // Fill missing columns with empty strings
                }
            }
            tableModel.addRow(completeRow);
        }

        dataTable.setModel(tableModel);
        // Adjust column widths dynamically based on content
        for (int i = 0; i < dataTable.getColumnCount(); i++) {
            dataTable.getColumnModel().getColumn(i).setPreferredWidth(150);
        }
    }

    /**
     * Initiates the sorting process using SwingWorker to keep UI responsive.
     */
    private void performSorting() {
        String selectedColumn = (String) columnSelector.getSelectedItem();
        if (selectedColumn == null) {
            JOptionPane.showMessageDialog(this, "Please select a column to sort.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int columnIndex = Arrays.asList(headers).indexOf(selectedColumn);
        columnData = new ArrayList<>();

        // Extract numeric data from the selected column
        for (String[] row : csvReader.getData()) {
            if (columnIndex < row.length) {
                String value = row[columnIndex].trim();
                if (value.isEmpty()) continue; // Skip empty values
                try {
                    double num = Double.parseDouble(value);
                    columnData.add(num);
                } catch (NumberFormatException e) {
                    // Skip non-numeric or invalid data
                }
            }
        }

        if (columnData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selected column contains no valid numeric data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Convert List<Double> to double[]
        double[] dataArray = columnData.stream().mapToDouble(Double::doubleValue).toArray();

        // Perform sorting and measure execution time in a background thread
        progressBar.setVisible(true);
        sortButton.setEnabled(false);
        columnSelector.setEnabled(false);
        uploadButton.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Exception sortException = null;

            @Override
            protected Void doInBackground() {
                try {
                    performAllSorts(dataArray.clone(), selectedColumn);
                } catch (Exception e) {
                    sortException = e;
                }
                return null;
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                sortButton.setEnabled(true);
                columnSelector.setEnabled(true);
                uploadButton.setEnabled(true);

                if (sortException != null) {
                    JOptionPane.showMessageDialog(SortingGUI.this,
                            "An error occurred during sorting: " + sortException.getMessage(),
                            "Sorting Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    /**
     * Performs all sorting algorithms and records their execution times.
     *
     * @param data           The data array to sort.
     * @param selectedColumn The name of the selected column.
     */
    private void performAllSorts(double[] data, String selectedColumn) {
        StringBuilder result = new StringBuilder();
        Map<String, Long> executionTimes = new LinkedHashMap<>();

        // Insertion Sort
        double[] insertionData = data.clone();
        long startTime = System.nanoTime();
        InsertionSort.sort(insertionData);
        long endTime = System.nanoTime();
        executionTimes.put("Insertion Sort", endTime - startTime);

        // Shell Sort
        double[] shellData = data.clone();
        startTime = System.nanoTime();
        ShellSort.sort(shellData);
        endTime = System.nanoTime();
        executionTimes.put("Shell Sort", endTime - startTime);

        // Merge Sort
        double[] mergeData = data.clone();
        startTime = System.nanoTime();
        MergeSort.sort(mergeData, 0, mergeData.length - 1);
        endTime = System.nanoTime();
        executionTimes.put("Merge Sort", endTime - startTime);

        // Quick Sort
        double[] quickData = data.clone();
        startTime = System.nanoTime();
        QuickSort.sort(quickData, 0, quickData.length - 1);
        endTime = System.nanoTime();
        executionTimes.put("Quick Sort", endTime - startTime);

        // Heap Sort
        double[] heapData = data.clone();
        startTime = System.nanoTime();
        HeapSort.sort(heapData);
        endTime = System.nanoTime();
        executionTimes.put("Heap Sort", endTime - startTime);

        // Display execution times
        result.append("Sorting Algorithm Performance (in nanoseconds):\n\n");
        String bestAlgorithm = null;
        long bestTime = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry : executionTimes.entrySet()) {
            result.append(String.format("%-15s : %d ns\n", entry.getKey(), entry.getValue()));
            if (entry.getValue() < bestTime) {
                bestTime = entry.getValue();
                bestAlgorithm = entry.getKey();
            }
        }

        result.append(String.format("\nBest Performing Algorithm: %s (%d ns)", bestAlgorithm, bestTime));
        // Update the result area on the EDT
        SwingUtilities.invokeLater(() -> resultArea.setText(result.toString()));

        // Visualize Original and Sorted Data
        visualizeData(data, bestAlgorithm, executionTimes.get(bestAlgorithm));
    }

    /**
     * Visualizes the original and sorted data using JFreeChart with enhanced visuals.
     *
     * @param originalData  The original data array.
     * @param bestAlgorithm The name of the best performing algorithm.
     * @param bestTime      The execution time of the best performing algorithm.
     */
    private void visualizeData(double[] originalData, String bestAlgorithm, long bestTime) {
        // Create datasets
        DefaultCategoryDataset originalDataset = new DefaultCategoryDataset();
        DefaultCategoryDataset sortedDataset = new DefaultCategoryDataset();

        // To avoid cluttering the chart, we'll plot only the first 100 data points
        int limit = Math.min(originalData.length, 100);

        for (int i = 0; i < limit; i++) {
            originalDataset.addValue(originalData[i], "Original", Integer.toString(i + 1));
        }

        // Sort the data using the best algorithm for visualization
        double[] sortedData = originalData.clone();
        switch (bestAlgorithm) {
            case "Insertion Sort":
                InsertionSort.sort(sortedData);
                break;
            case "Shell Sort":
                ShellSort.sort(sortedData);
                break;
            case "Merge Sort":
                MergeSort.sort(sortedData, 0, sortedData.length - 1);
                break;
            case "Quick Sort":
                QuickSort.sort(sortedData, 0, sortedData.length - 1);
                break;
            case "Heap Sort":
                HeapSort.sort(sortedData);
                break;
            default:
                // Default to original data if algorithm not recognized
                break;
        }

        for (int i = 0; i < limit; i++) {
            sortedDataset.addValue(sortedData[i], "Sorted", Integer.toString(i + 1));
        }

        // Create charts with enhanced aesthetics
        JFreeChart originalChart = ChartFactory.createLineChart(
                "Original Data",
                "Index",
                "Value",
                originalDataset
        );

        JFreeChart sortedChart = ChartFactory.createLineChart(
                "Sorted Data",
                "Index",
                "Value",
                sortedDataset
        );

        // Customize chart appearance
        customizeChart(originalChart, new Color(70, 130, 180)); // Steel Blue
        customizeChart(sortedChart, new Color(34, 139, 34)); // Forest Green

        // Clear previous charts and update on the EDT
        SwingUtilities.invokeLater(() -> {
            chartContainer.removeAll();

            // Add charts to the chart container
            JPanel chartsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
            chartsPanel.setBackground(new Color(240, 248, 255)); // Match main background
            chartsPanel.add(new ChartPanel(originalChart));
            chartsPanel.add(new ChartPanel(sortedChart));

            chartContainer.add(chartsPanel, BorderLayout.CENTER);
            chartContainer.validate();
            chartContainer.repaint();
        });
    }

    /**
     * Customizes the appearance of a JFreeChart.
     *
     * @param chart The chart to customize.
     * @param color The color to apply to the chart lines.
     */
    private void customizeChart(JFreeChart chart, Color color) {
        chart.setBackgroundPaint(new Color(240, 248, 255)); // Match main background
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, color);
        chart.getCategoryPlot().getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
        chart.getCategoryPlot().getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 14));
        chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Optional: Customize plot background and grid lines for better aesthetics
        chart.getCategoryPlot().setBackgroundPaint(new Color(255, 255, 255, 200)); // Semi-transparent white
        chart.getCategoryPlot().setDomainGridlinePaint(new Color(200, 200, 200));
        chart.getCategoryPlot().setRangeGridlinePaint(new Color(200, 200, 200));
    }

    /**
     * Clears the chart container.
     */
    private void clearChart() {
        chartContainer.removeAll();
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    /**
     * Sets a custom font for all UI components.
     *
     * @param f The FontUIResource to apply.
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    /**
     * Creates a titled border with custom styling.
     *
     * @param title The title of the border.
     * @return A TitledBorder with custom fonts and colors.
     */
    private TitledBorder createTitleBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1), title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(new Color(70, 130, 180)); // Steel Blue
        return border;
    }

    /**
     * Custom Rounded Border class to create rounded edges for buttons and panels.
     */
    public class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    /**
     * Main method to run the SortingGUI application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingGUI gui = new SortingGUI();
            gui.setVisible(true);
        });
    }
}
