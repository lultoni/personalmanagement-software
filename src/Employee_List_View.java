package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Employee_List_View extends JPanel {

    public Employee_List_View(ArrayList<Employee> employees) {
        setLayout(new BorderLayout());

        // Panel, das die Liste enthält
        JPanel list_panel = new JPanel();
        list_panel.setLayout(new BoxLayout(list_panel, BoxLayout.Y_AXIS));

        // Für jeden Mitarbeiter ein Single-View hinzufügen
        for (Employee emp : employees) {
            Employee_List_View_Single emp_view = new Employee_List_View_Single(emp, 64);
            emp_view.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // optional spacing
            list_panel.add(emp_view);
        }

        // Scrollpane drumherum
        JScrollPane scrollPane = new JScrollPane(list_panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling

        add(scrollPane, BorderLayout.CENTER);
    }
}
