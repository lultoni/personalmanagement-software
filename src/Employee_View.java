package src;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class Employee_View extends JPanel {

    public Employee_View(Employee employee) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==== ICON (LEFT) ====
        int iconSize = 100;
        ImageIcon rawIcon = new ImageIcon("src/img_icon/default_emp_icon.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        ImageIcon empIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(empIcon);
        iconLabel.setPreferredSize(new Dimension(iconSize, iconSize));
        add(iconLabel, BorderLayout.WEST);

        // ==== EMPLOYEE INFO (CENTER) ====
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(employee.getName()));
        infoPanel.add(new JLabel("ID:"));
        infoPanel.add(new JLabel(String.valueOf(employee.getId())));
        infoPanel.add(new JLabel("E-Mail:"));
        infoPanel.add(new JLabel(employee.getEmailAddress()));
        infoPanel.add(new JLabel("Geburtstag:"));
        infoPanel.add(new JLabel(employee.getBirthdayString()));
        infoPanel.add(new JLabel("Startdatum:"));
        infoPanel.add(new JLabel(employee.getStartDateString()));
        infoPanel.add(new JLabel("Gehalt:"));
        infoPanel.add(new JLabel(formatSalary(employee.getSalary())));
        infoPanel.add(new JLabel("Abteilung:"));
        infoPanel.add(new JLabel(employee.getDepartment()));
        infoPanel.add(new JLabel("Rolle:"));
        infoPanel.add(new JLabel(employee.getRole()));
        infoPanel.add(new JLabel("Manager-ID:"));
        infoPanel.add(new JLabel(String.valueOf(employee.getManagerId())));
        add(infoPanel, BorderLayout.CENTER);

        if (Main.getSecurity_level() < 100) return;

        // ==== BUTTONS (BOTTOM) ====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Bearbeiten");
        JButton fireButton = new JButton("Entlassen");

        // TODO: add action listeners
        editButton.addActionListener(e -> {
            // Implementiere Bearbeiten-Dialog oder Funktion
            JOptionPane.showMessageDialog(this, "Bearbeiten nicht implementiert.");
        });

        fireButton.addActionListener(e -> {
            // Achtung bei Managern → ggf. Warnung anzeigen
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Möchten Sie diesen Mitarbeiter wirklich entlassen?",
                    "Bestätigung",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: Mitarbeiter entfernen
                JOptionPane.showMessageDialog(this, "Mitarbeiter entlassen.");
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(fireButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private String formatSalary(BigDecimal salary) {
        return salary == null ? "-" : salary + " €";
    }
}
