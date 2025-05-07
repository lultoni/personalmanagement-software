package src;

import javax.swing.*;
import java.awt.*;

public class Employee_View extends JPanel {

    public Employee_View(Employee employee) {
        // TODO wie soll der view hier aussehen

        // TODO show all the infos of the employee
        setLayout(new GridLayout());
        add(new JLabel("<HTML>" + employee.toString() + "<HTML>"));
        // TODO muss zum bearbeiten oder feuern eine funktionalität haben (achtung bei managern feuern, die untergeordneten sind sonst führungslos)

    }

    public Employee_View(Employee employee) {
        // TODO show all the infos
        setLayout(new GridLayout());
        add(new JLabel("<HTML>" + employee.toString() + "<HTML>"));
    }

}
