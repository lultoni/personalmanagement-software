package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main_Window extends JFrame {

    private JPanel top_panel;
    private JPanel bottom_panel;
    private JPanel left_panel;
    private JPanel right_panel;
    private JPanel content_panel;
    private ArrayList<JButton> when_guest_disabled;

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

        when_guest_disabled = new ArrayList<>();

        setLayout(new BorderLayout());

        top_panel = new JPanel(new GridLayout());
        bottom_panel = new JPanel(new GridLayout());
        left_panel = new JPanel(new GridLayout(0, 1));
        right_panel = new JPanel(new GridLayout());
        content_panel = new JPanel(new GridLayout());

        JLabel company_name_label = new JLabel("BtBC - personalmanagement-software");
        set_label_style("title", company_name_label);

        top_panel.add(company_name_label);

        ImageIcon rawIcon = new ImageIcon("src/img_icon/homepage_icon.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon homepage_icon = new ImageIcon(scaledImage);
        JButton homepage_button = new JButton(homepage_icon);
        homepage_button.addActionListener(_ -> Main.callEvent("homepage_button_click", null));

        left_panel.add(homepage_button);

        rawIcon = new ImageIcon("src/img_icon/search_icon.png");
        scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon search_icon = new ImageIcon(scaledImage);
        JButton search_button = new JButton(search_icon);
        search_button.addActionListener(_ -> Main.callEvent("search_button_click", null));

        left_panel.add(search_button);
        when_guest_disabled.add(search_button);

        rawIcon = new ImageIcon("src/img_icon/hir_icon.png");
        scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon hir_icon = new ImageIcon(scaledImage);
        JButton hir_button = new JButton(hir_icon);
        hir_button.addActionListener(_ -> Main.callEvent("hir_button_click", null));

        left_panel.add(hir_button);
        when_guest_disabled.add(hir_button);

        rawIcon = new ImageIcon("src/img_icon/login_icon.png");
        scaledImage = rawIcon.getImage().getScaledInstance(preferredWidth, preferredHeight, Image.SCALE_SMOOTH);
        ImageIcon login_icon = new ImageIcon(scaledImage);
        JButton login_button = new JButton(login_icon);
        login_button.addActionListener(_ -> Main.callEvent("login_page_button_click", null));

        left_panel.add(login_button);

        changeContentPage("login_screen", null);

        add(top_panel, BorderLayout.NORTH);
        add(bottom_panel, BorderLayout.SOUTH);
        add(left_panel, BorderLayout.WEST);
        add(right_panel, BorderLayout.EAST);
        add(content_panel, BorderLayout.CENTER);

    }

    public void onSecurityChange() {
        int security_level = Main.getSecurity_level();

        for (JButton button: when_guest_disabled) button.setEnabled(security_level >= 10);
    }

    public void changeContentPage(String page_tag, Object event_args) {
        JPanel newPage;

        switch (page_tag) {
            case null:
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ page_tag was null");
                newPage = new Homepage_View();
                break;
            case "homepage":
                newPage = new Homepage_View();
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'homepage'");
                break;
            case "search":
                newPage = new Employee_Search();
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'search'");
                break;
            case "emp_list":
                if (event_args instanceof ArrayList) {
                    newPage = new Employee_List_View((ArrayList<Employee>) event_args);
                    System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'emp_list'");
                } else {
                    System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'emp_list' called with invalid args, showing homepage");
                    newPage = new Homepage_View();
                }
                break;
            case "login_screen":
                newPage = new Login_Screen();
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'login_screen'");
                break;
            case "employee_view":
                newPage = new Employee_View((Employee) event_args);
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'employee_view'");
                break;
            case "hierarchy":
                if (event_args == null) {
                    Employee ceo = null;
                    for (Employee employee: DB_API.getAllEmployees()) if (employee.getId() == 1) {
                        ceo = employee;
                        break;
                    }
                    event_args = ceo;
                }
                Employee emp = (Employee) event_args;
                newPage = new Hierarchy_View(emp);
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ 'hierarchy' ~ " + emp.getId());
                break;
            default:
                newPage = new Homepage_View();
                System.out.println(Main.debug_pre_string + "Main_Window.changeContentPage() ~ Unexpected page_tag: '" + page_tag + "' | moved to homepage");
                break;
        }

        if (newPage != null) {
            Component currentCenterComponent = ((BorderLayout)getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
            if (currentCenterComponent != null) {
                remove(currentCenterComponent);
            }

            this.content_panel = newPage;
            add(this.content_panel, BorderLayout.CENTER);

            revalidate();
            repaint();
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
            case "bold_center":
                label.setFont(new Font(default_font_name, Font.BOLD, default_font_size));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case "light_center":
                label.setFont(new Font(default_font_name, default_font_style, default_font_size));
                label.setForeground(new Color(66, 66, 66));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            default:
                System.out.println(Main.debug_pre_string + "set_label_style() ~ Unexpected style_tag: '" + style_tag + "' | default settings applied");
                label.setFont(new Font(default_font_name, default_font_style, default_font_size));
                break;
        }

    }

}
