package src;

import java.awt.*;

public class Main {

    public static final String debug_pre_string = "db ~ ";
    private static Main_Window window;

    public static void main (String[] args) {

        System.out.println("\n\n");
        System.out.println(debug_pre_string + "Starting personalmanagement-software");

        // Falls neue Employees erstellt werden sollen
        // Params: (Anzahl Mitarbeiter; vorhandene Daten Löschen; falls Mitarbeiter vorhanden Ersetzen)
        // Employee_Generator.main(new String[]{"1000", "True", "True"});

        // System.out.println("\nAll Employees:");
        // for (Employee employee: DB_API.getAllEmployees()) System.out.println(" - " + employee.toString());

        window = new Main_Window();

    }

    public static void callEvent(String event_name) {
        switch (event_name) {
            case null:
                System.out.println(Main.debug_pre_string + "callEvent() ~ event_name was null ~ no event called");
                break;
            case "homepage_button_click":
                window.changeContentPage("homepage");
                break;
            case "search_button_click":
                window.changeContentPage("search");
                break;
            case "hir_button_click":
                window.changeContentPage("hierarchy");
                break;
            default:
                System.out.println(Main.debug_pre_string + "callEvent() ~ Unexpected event_name: '" + event_name + "' ~ no event called");
                break;
        }
    }
}