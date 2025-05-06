package src;

import javax.swing.*;
import java.awt.*;

public class Homepage_View extends JPanel {

    // TODO: placeholder homepage ersetzen durch was fähiges

    public Homepage_View() {

        setLayout(new GridLayout(0, 1));

        JLabel homepage_title = new JLabel("Willkommen bei der personalmanagement-software von der BtBC!");
        Main_Window.set_label_style("bold_center", homepage_title);

        JLabel homepage_description = new JLabel(getDescriptionText(Main.getSecurity_level()));
        Main_Window.set_label_style("light_center", homepage_description);

        add(homepage_title);
        add(homepage_description);

    }

    private String getDescriptionText(int securityLevel) {
        return switch (securityLevel) {
            case 1 -> "Da du nur ein Gast bist, hast du leider keine Zugangsrechte auf diese Software.";
            case 10 -> "Hey Mitarbeiter! Immer schön fleißig arbeiten!";
            case 100 -> "Hey Manger! Versuch nicht zu viele Mitarbeiter zu feuern haha";
            case 1000 -> "*beep boop* GUTEN TAG HERR ADMIN *beep boop*";
            default -> "Error vom Security Level";
        };
    }

}
