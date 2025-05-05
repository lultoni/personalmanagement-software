package src;

import javax.swing.*;
import java.awt.*;

public class Employee_List_View_Single extends JPanel {

    private JLabel name_label;
    private JLabel role_label;
    private JLabel department_label;

    // TODO make the employee clickable and fire a event in main, so that the singular employee view can be opened in the main window content area

    public Employee_List_View_Single(Employee emp, int preferredHeight) {
        // Load and scale icon
        ImageIcon rawIcon = new ImageIcon("src/img_icon/default_emp_icon.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(preferredHeight, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon emp_icon = new ImageIcon(scaledImage);
        JLabel icon_label = new JLabel(emp_icon);

        // Labels
        if (emp == null) {
            name_label = new JLabel("error_name");
            role_label = new JLabel("error_role");
            department_label = new JLabel("error_department");
        } else {
            name_label = new JLabel(emp.getName());
            role_label = new JLabel(emp.getRole());
            department_label = new JLabel(emp.getDepartment());
        }

        set_styles();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Short.MAX_VALUE, preferredHeight + 10)); // add margin for spacing

        // Icon left
        icon_label.setPreferredSize(new Dimension(preferredHeight, preferredHeight));
        add(icon_label, BorderLayout.WEST);

        // Text panel center
        JPanel text_panel = new JPanel();
        text_panel.setLayout(new GridLayout(2, 1));

        text_panel.add(name_label);

        JPanel role_dept_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        role_dept_panel.add(role_label);
        role_dept_panel.add(department_label);
        text_panel.add(role_dept_panel);

        add(text_panel, BorderLayout.CENTER);
    }

    private void set_styles() {
        Main_Window.set_label_style("bold", name_label);
        Main_Window.set_label_style("italic_light", role_label);
        Main_Window.set_label_style("italic_light", department_label);
    }
}
