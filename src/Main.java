package src;

public class Main {

    public static final String debug_pre_string = "db ~ ";

    public static void main (String[] args) {

        System.out.println("\n\n");
        System.out.println(debug_pre_string + "Starting personalmanagement-software");

        // Falls neue Employees erstellt werden sollen
        // Params: (Anzahl Mitarbeiter; vorhandene Daten Löschen; falls Mitarbeiter vorhanden Ersetzen)
        // Employee_Generator.main(new String[]{"1000", "True", "True"});

        // System.out.println("\nAll Employees:");
        // for (Employee employee: DB_API.getAllEmployees()) System.out.println(" - " + employee.toString());

        Main_Window window = new Main_Window();

    }

}