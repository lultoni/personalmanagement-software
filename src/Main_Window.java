package src;

import javax.swing.*;
import java.awt.*;

public class Main_Window extends JFrame {

    private JPanel top_panel;
    private JPanel bottom_panel;
    private JPanel left_panel;
    private JPanel right_panel;
    private JPanel content_panel;
    private JPanel homepage_panel;

    public Main_Window() {
        setTitle("personalmanagement-software");
        setBounds(25, 25, 800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
        setVisible(true);
    }

    private void init() {

        int preferredHeight = 64; // This is for the size of the left action bar icons
        int preferredWidth = 64;

        setLayout(new BorderLayout());

        homepage_panel = new Homepage_View();

        top_panel = new JPanel(new GridLayout());
        bottom_panel = new JPanel(new GridLayout());
        left_panel = new JPanel(new GridLayout(0, 1));
        right_panel = new JPanel(new GridLayout());
        content_panel = new JPanel(new GridLayout());

        JLabel company_name_label = new JLabel("BtBC - personalmanagement-software");
        set_label_style("title", company_name_label);

        top_panel.add(company_name_label);

        // TODO: homepage
        ImageIcon rawIcon = new ImageIcon("src/img_icon/homepage_icon.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon homepage_icon = new ImageIcon(scaledImage);
        JButton homepage_button = new JButton(homepage_icon);
        homepage_button.addActionListener(_ -> Main.callEvent("homepage_button_click"));

        left_panel.add(homepage_button);

        // TODO: search (hat eine eigene Klasse)
        rawIcon = new ImageIcon("src/img_icon/search_icon.png");
        scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon search_icon = new ImageIcon(scaledImage);
        JButton search_button = new JButton(search_icon);
        search_button.addActionListener(_ -> Main.callEvent("search_button_click"));

        left_panel.add(search_button);

        // TODO: hierarchy (hat eine eigene Klasse, aber wollen wir das fr hier haben?)
        rawIcon = new ImageIcon("src/img_icon/hir_icon.png");
        scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon hir_icon = new ImageIcon(scaledImage);
        JButton hir_button = new JButton(hir_icon);
        hir_button.addActionListener(_ -> Main.callEvent("hir_button_click"));

        left_panel.add(hir_button);

        changeContentPage("homepage");

        add(top_panel, BorderLayout.NORTH);
        add(bottom_panel, BorderLayout.SOUTH);
        add(left_panel, BorderLayout.WEST);
        add(right_panel, BorderLayout.EAST);
        add(content_panel, BorderLayout.CENTER);

    }

    public void changeContentPage(String page_tag) {
        switch (page_tag) {
            case null:
                System.out.println(Main.debug_pre_string + "changeContentPage() ~ page_tag was null");
                break;
            case "homepage":
                this.content_panel = homepage_panel;
                break;
            default:
                System.out.println(Main.debug_pre_string + "changeContentPage() ~ Unexpected page_tag: '" + page_tag + "' | moved to homepage");
                break;
        }
    }

    public static void set_label_style(String style_tag, JLabel label) {

        String default_font_name = "Arial";
        int default_font_style = Font.PLAIN;
        int default_font_size = 16;

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
                label.setFont(new Font(default_font_name, Font.ITALIC, (int) (default_font_size * 0.8)));
                label.setForeground(new Color(66, 66, 66));
                break;
            default:
                System.out.println(Main.debug_pre_string + "set_label_style() ~ Unexpected style_tag: '" + style_tag + "' | default settings applied");
                label.setFont(new Font(default_font_name, default_font_style, default_font_size));
                break;
        }

    }

}
