package src;

import javax.swing.*;
import java.awt.*;

public class Main_Window extends JFrame {

    public Main_Window() {
        setTitle("personalmanagement-software");
        setBounds(25, 25, 700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init() {

        // TODO include Employee Visualizer
        // TODO have a bit of an outside border holding information and functionality

        setLayout(new BorderLayout());

        JPanel top_panel = new JPanel(new GridLayout());
        JPanel bottom_panel = new JPanel(new GridLayout());
        JPanel left_panel = new JPanel(new GridLayout());
        JPanel right_panel = new JPanel(new GridLayout());
        JPanel content_panel = new JPanel(new GridLayout());

        JLabel company_name_label = new JLabel("BtBC - personalmanagement-software");
        set_label_style("title", company_name_label);

        top_panel.add(company_name_label);

        add(top_panel, BorderLayout.NORTH);
        add(bottom_panel, BorderLayout.SOUTH);
        add(left_panel, BorderLayout.WEST);
        add(right_panel, BorderLayout.EAST);
        add(content_panel, BorderLayout.CENTER);

    }

    public static void set_label_style(String style_tag, JLabel label) {

        String default_font_name = "Arial";
        int default_font_style = Font.PLAIN;
        int default_font_size = 12;

        switch (style_tag) {
            case null:
                System.out.println(Main.debug_pre_string + "set_label_style() ~ style_tag was null");
                break;
            case "title":
                label.setFont(new Font(default_font_name, Font.BOLD, default_font_size * 2));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case "bold":
                label.setFont(new Font(default_font_name, Font.BOLD, default_font_size));
                break;
            case "italic_light":
                label.setFont(new Font(default_font_name, Font.ITALIC, (int) (default_font_size * 0.75)));
                label.setForeground(new Color(66, 66, 66));
                break;
            default:
                System.out.println(Main.debug_pre_string + "set_label_style() ~ Unexpected style_tag: " + style_tag + " | default settings applied");
                label.setFont(new Font(default_font_name, default_font_style, default_font_size));
                break;
        }

    }

}
