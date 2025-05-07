package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Employee_List_View extends JPanel {

    public Employee_List_View(ArrayList<Employee> employees) {
        setLayout(new BorderLayout());

        JPanel list_panel = new JPanel();
        list_panel.setLayout(new BoxLayout(list_panel, BoxLayout.Y_AXIS));
        list_panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Mitarbeiter-Ansichten hinzufügen
        for (Employee emp : employees) {
            Employee_List_View_Single emp_view = new Employee_List_View_Single(emp, 64);
            emp_view.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            emp_view.setAlignmentX(Component.LEFT_ALIGNMENT);
            list_panel.add(emp_view);
        }

        JScrollPane scrollPane = new JScrollPane(list_panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Dynamisch an Viewport-Breite anpassen
        scrollPane.getViewport().addChangeListener(e -> {
            int width = scrollPane.getViewport().getWidth();
            for (Component comp : list_panel.getComponents()) {
                if (comp instanceof Employee_List_View_Single) {
                    comp.setPreferredSize(new Dimension(width, comp.getPreferredSize().height));
                }
            }
            list_panel.revalidate();
        });

        add(scrollPane, BorderLayout.CENTER);
    }
}

