package src;

import javax.swing.*;
import java.awt.*;

public class Homepage_View extends JPanel {

    public Homepage_View() {

        setLayout(new GridLayout());

        JLabel homepage_title = new JLabel("Willkommen bei der personalmanagement-software von der BtBC");
        Main_Window.set_label_style("bold", homepage_title);

        add(homepage_title);

    }

}
