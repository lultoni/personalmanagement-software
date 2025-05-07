package src;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class Hierarchy_View_Single extends JPanel {

    public Hierarchy_View_Single(Employee employee) {
        // TODO implement this class
        setLayout(new GridLayout());
        add(new JLabel(employee.getName() + " - " + employee.getId()));
        setBorder(new BevelBorder(BevelBorder.LOWERED));
    }

}
