package com.sortingapp;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to read CSV files and identify numeric columns.
 */
public class CSVReaderUtil {
    private String[] headers;
    private List<String[]> data;

    /**
     * Reads a CSV file from the given file path.
     *
     * @param filePath Path to the CSV file.
     * @return true if reading is successful, false otherwise.
     */
    public boolean readCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            data = reader.readAll();
            if (!data.isEmpty()) {
                headers = data.get(0);
                data.remove(0); // Remove header from data
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "CSV file is empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (IOException | CsvException e) {
            JOptionPane.showMessageDialog(null, "Error reading CSV file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the headers of the CSV file.
     *
     * @return Array of header names.
     */
    public String[] getHeaders() {
        return headers;
    }

    /**
     * Returns the data rows of the CSV file.
     *
     * @return List of data rows, each row is a String array.
     */
    public List<String[]> getData() {
        return data;
    }

    /**
     * Identifies and returns the indices of numeric columns.
     *
     * @return List of indices corresponding to numeric columns.
     */
    public List<Integer> getNumericColumnIndices() {
        List<Integer> numericIndices = new ArrayList<>();
        if (data == null || data.isEmpty()) {
            return numericIndices;
        }

        // Check the first non-empty row to determine numeric columns
        for (int col = 0; col < headers.length; col++) {
            boolean isNumeric = true;
            for (String[] row : data) {
                if (col >= row.length) {
                    isNumeric = false;
                    break;
                }
                String value = row[col];
                if (value == null || value.trim().isEmpty()) {
                    continue; // Ignore empty cells
                }
                if (!NumberUtils.isParsable(value)) {
                    isNumeric = false;
                    break;
                }
            }
            if (isNumeric) {
                numericIndices.add(col);
            }
        }

        return numericIndices;
    }
}
