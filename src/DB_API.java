package src;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class DB_API {

    private static final String filename = "database.db";

    public static Connection getConnection(String filename){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:"+filename);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        String sql = "SELECT emp_id, name, birthday, email_adress, salary, start_date, manager_id, role, department FROM Employees";

        try (Connection connection = getConnection(filename);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                String birthday = resultSet.getString("birthday");
                String email = resultSet.getString("email_adress");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                String startDate = resultSet.getString("start_date");

                int managerId = 0;
                int managerIdRaw = resultSet.getInt("manager_id");
                if (!resultSet.wasNull()) {
                    managerId = managerIdRaw;
                }

                String role = resultSet.getString("role");
                String department = resultSet.getString("department");

                Employee employee = new Employee(id, name, birthday, email, salary, startDate, managerId, role, department);
                employees.add(employee);
            }

        } catch (SQLException e) {
            System.err.println("Fehler beim Abrufen der Mitarbeiter: " + e.getMessage());
        }

        return employees;
    }

    public static void SQL_command(String command) {
        try{
            Connection connection = getConnection(filename);
            PreparedStatement statement = connection.prepareStatement(command);
            ResultSet resultSet = statement.executeQuery();

            try {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                String header = "\n| ";
                for (int i = 1; i <= columnCount; i++) {
                    header += metaData.getColumnName(i);
                    if (i + 1 <= columnCount) header += " | ";
                }
                System.out.println(header + " |");
                while (resultSet.next()) {
                    String line = " - | ";
                    for (int i = 1; i <= columnCount; i++) {
                        line += resultSet.getObject(i);
                        if (i + 1 <= columnCount) line += " | ";
                    }
                    System.out.println(line + " |");
                }
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
