package src;

import javax.swing.*;
import java.awt.*;

public class Employee_View extends JPanel {

    // TODO write this class; Das gibt nur einen Mitarbeiter zurück für das Window
    // TODO muss zum bearbeiten oder feuern eine funktionalität haben (achtung bei managern feuern, die untergeordneten sind sonst führungslos)

    public Employee_View(Employee employee) {
        // TODO show all the infos
        setLayout(new GridLayout());
        add(new JLabel("<HTML>" + employee.toString() + "<HTML>"));
    }

}
