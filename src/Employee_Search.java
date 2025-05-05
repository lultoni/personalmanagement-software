package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Employee_Search extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField roleField;
    private JTextField departmentField;
    private JButton searchButton;
    private JButton clearButton;

    private ArrayList<Employee> allEmployees;

    public Employee_Search() {
        try {
            this.allEmployees = DB_API.getAllEmployees();
            if (this.allEmployees == null) {
                this.allEmployees = new ArrayList<>();
                JOptionPane.showMessageDialog(this,
                        "Konnte Mitarbeiterdaten nicht laden.",
                        "Datenbankfehler",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            this.allEmployees = new ArrayList<>();
            JOptionPane.showMessageDialog(this,
                    "Fehler beim Laden der Mitarbeiterdaten: " + e.getMessage(),
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setLayout(new BorderLayout(5, 5));

        JPanel searchCriteriaPanel = new JPanel(new GridBagLayout());
        searchCriteriaPanel.setBorder(BorderFactory.createTitledBorder("Suchkriterien"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        searchCriteriaPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        idField = new JTextField(10);
        searchCriteriaPanel.add(idField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 1;
        searchCriteriaPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        nameField = new JTextField(20);
        searchCriteriaPanel.add(nameField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 2;
        searchCriteriaPanel.add(new JLabel("E-Mail:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        emailField = new JTextField(20);
        searchCriteriaPanel.add(emailField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 2; gbc.gridy = 0;
        searchCriteriaPanel.add(new JLabel("Rolle:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        roleField = new JTextField(15);
        searchCriteriaPanel.add(roleField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 2; gbc.gridy = 1;
        searchCriteriaPanel.add(new JLabel("Abteilung:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        departmentField = new JTextField(15);
        searchCriteriaPanel.add(departmentField, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchButton = new JButton("Suchen");
        clearButton = new JButton("Zurücksetzen");
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        searchCriteriaPanel.add(buttonPanel, gbc);

        add(searchCriteriaPanel, BorderLayout.NORTH);

        searchButton.addActionListener(_ -> performSearch());
        clearButton.addActionListener(_ -> clearSearch());
    }

    private void performSearch() {
        String idStr = idField.getText().trim();
        String nameStr = nameField.getText().trim().toLowerCase();
        String emailStr = emailField.getText().trim().toLowerCase();
        String roleStr = roleField.getText().trim().toLowerCase();
        String departmentStr = departmentField.getText().trim().toLowerCase();

        ArrayList<Employee> filteredList = allEmployees.stream()
                .filter(emp -> {
                    if (!idStr.isEmpty()) {
                        try {
                            int searchId = Integer.parseInt(idStr);
                            if (emp.getId() != searchId) {
                                return false;
                            }
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    if (!nameStr.isEmpty() && (emp.getName() == null || !emp.getName().toLowerCase().contains(nameStr))) {
                        return false;
                    }
                    if (!emailStr.isEmpty() && (emp.getEmailAddress() == null || !emp.getEmailAddress().toLowerCase().contains(emailStr))) {
                        return false;
                    }
                    if (!roleStr.isEmpty() && (emp.getRole() == null || !emp.getRole().toLowerCase().contains(roleStr))) {
                        return false;
                    }
                    if (!departmentStr.isEmpty() && (emp.getDepartment() == null || !emp.getDepartment().toLowerCase().contains(departmentStr))) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        Main.callEvent("search_action", filteredList);
    }

    private void clearSearch() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        roleField.setText("");
        departmentField.setText("");

        // Call the event handler with the full list to reset the view
        // Main.callEvent("search_action", new ArrayList<>(this.allEmployees));
    }

}