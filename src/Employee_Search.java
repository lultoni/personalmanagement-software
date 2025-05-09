package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Employee_Search extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> roleComboBox;
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
        searchCriteriaPanel.add(new JLabel("Abteilung:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        departmentComboBox = new JComboBox<>();
        departmentComboBox.addItem(""); // Leere Auswahl
        for (String dep : DB_Employee_Generator.getDepartments()) {
            departmentComboBox.addItem(dep);
        }
        searchCriteriaPanel.add(departmentComboBox, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        gbc.gridx = 2; gbc.gridy = 1;
        searchCriteriaPanel.add(new JLabel("Rolle:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        roleComboBox = new JComboBox<>();
        roleComboBox.addItem(""); // Leere Auswahl
        updateRolesByDepartment((String) departmentComboBox.getSelectedItem());
        searchCriteriaPanel.add(roleComboBox, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;

        departmentComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateRolesByDepartment((String) e.getItem());
            }
        });

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

    private void updateRolesByDepartment(String department) {
        roleComboBox.removeAllItems();
        roleComboBox.addItem(""); // Leere Auswahl

        if (department == null || department.isEmpty()) {
            // Alle Rollen aus allen Abteilungen sammeln
            DB_Employee_Generator.getRolesByDepartment().values().stream()
                    .flatMap(List::stream)
                    .distinct()
                    .sorted()
                    .forEach(roleComboBox::addItem);
            return;
        }

        List<String> roles = DB_Employee_Generator.getRolesByDepartment().getOrDefault(department,
                DB_Employee_Generator.getRolesByDepartment().get("Default"));
        for (String role : roles) {
            roleComboBox.addItem(role);
        }
    }

    private void performSearch() {
        String idStr = idField.getText().trim();
        String nameStr = nameField.getText().trim().toLowerCase();
        String emailStr = emailField.getText().trim().toLowerCase();
        String roleStr = ((String) roleComboBox.getSelectedItem()).trim().toLowerCase();
        String departmentStr = ((String) departmentComboBox.getSelectedItem()).trim().toLowerCase();

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
                    if (!nameStr.isEmpty() && !isSimilar(emp.getName(), nameStr)) {
                        return false;
                    }
                    if (!emailStr.isEmpty() && !isSimilar(emp.getEmailAddress(), emailStr)) {
                        return false;
                    }
                    if (!roleStr.isEmpty() && !isSimilar(emp.getRole(), roleStr)) {
                        return false;
                    }
                    if (!departmentStr.isEmpty() && !isSimilar(emp.getDepartment(), departmentStr)) {
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
        departmentComboBox.setSelectedIndex(0);
        updateRolesByDepartment((String) departmentComboBox.getSelectedItem());
        roleComboBox.setSelectedIndex(0);
    }

    private boolean isSimilar(String source, String target) {
        if (source == null || target == null) return false;
        source = source.toLowerCase();
        target = target.toLowerCase();
        int distance = levenshtein(source, target);
        int maxLength = Math.max(source.length(), target.length());
        return maxLength == 0 || ((1.0 - (double) distance / maxLength) >= 0.5);
    }

    private int levenshtein(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }
}
