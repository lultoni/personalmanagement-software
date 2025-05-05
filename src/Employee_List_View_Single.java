package src;

import javax.swing.*;
import java.awt.*;

public class Employee_List_View_Single extends JPanel {

    // TODO write this class; this uses the src.img_icon's and shows the name with the role and department

    private ImageIcon emp_icon;
    private JLabel name_label;
    private JLabel role_label;
    private JLabel department_label;

    public Employee_List_View_Single(Employee emp) {

        // TODO different icons for managers and CEO/CFO/... in the future
        emp_icon = new ImageIcon("/img_icon/default_emp_icon.png");

        if (emp == null) {
            name_label = new JLabel("error_name");
            role_label = new JLabel("error_role");
            department_label = new JLabel("error_department");
            set_styles();
            return;
        }

        name_label = new JLabel(emp.getName());
        role_label = new JLabel(emp.getRole());
        department_label = new JLabel(emp.getDepartment());
        set_styles();

        setLayout(new BorderLayout());

        JLabel icon_label = new JLabel(emp_icon);
        add(icon_label, BorderLayout.WEST);

    }

    private void set_styles() {
        Main_Window.set_label_style("bold", name_label);
        Main_Window.set_label_style("italic_light", role_label);
        Main_Window.set_label_style("italic_light", department_label);
    }

}
