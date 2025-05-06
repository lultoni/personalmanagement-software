package src;

import java.awt.*;

public class Main {

    public static final String debug_pre_string = "db ~ ";
    private static Main_Window window;

    private static int security_level = 1;
    public static String loggedInUsername = null; // Store the username of the logged-in user

    public static void main (String[] args) {

        System.out.println("\n\n");
        System.out.println(debug_pre_string + "Starting personalmanagement-software");

        // Falls neue Employees erstellt werden sollen
        // Params: (Anzahl Mitarbeiter; vorhandene Daten Löschen; falls Mitarbeiter vorhanden Ersetzen)
        // Employee_Generator.main(new String[]{"1000", "True", "True"});

        // System.out.println("\nAll Employees:");
        // for (Employee employee: DB_API.getAllEmployees()) System.out.println(" - " + employee.toString());

        window = new Main_Window();
        window.onSecurityChange();

    }

    public static void callEvent(String event_name, Object event_args) {
        switch (event_name) {
            case null:
                System.out.println(Main.debug_pre_string + "Main.callEvent() ~ event_name was null ~ no event called");
                break;
            case "homepage_button_click":
                window.changeContentPage("homepage", event_args);
                break;
            case "search_button_click":
                window.changeContentPage("search", event_args);
                break;
            case "hir_button_click":
                window.changeContentPage("hierarchy", event_args);
                break;
            case "search_action":
                window.changeContentPage("emp_list", event_args);
                break;
            case "login_page_button_click":
                window.changeContentPage("login_screen", event_args);
                break;
            case "security_level_change":
                int old_level = security_level;
                security_level = (int) event_args;
                System.out.println(Main.debug_pre_string + "Main.callEvent() ~ changed security level from " + old_level + " to " + security_level);
                window.onSecurityChange();
                break;
            default:
                System.out.println(Main.debug_pre_string + "Main.callEvent() ~ Unexpected event_name: '" + event_name + "' ~ no event called");
                break;
        }
    }

    public static int getSecurity_level() {
        return security_level;
    }
}