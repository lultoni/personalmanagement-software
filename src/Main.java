package src;

import src.DB_API.*;

public class Main {

    public static final String debug_pre_string = "db ~ ";

    public static void main (String[] args) {

        System.out.println(debug_pre_string + "Starting personalmanagement-software");
        DB_API.SQL_command("SELECT * FROM Employees;");

    }

}