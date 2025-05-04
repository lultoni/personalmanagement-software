package src;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void deleteAllEmployees() {
        String sql = "DELETE FROM Employees"; // SQL-Befehl zum Löschen aller Zeilen

        // Try-with-resources stellt sicher, dass Connection und Statement geschlossen werden
        try (Connection connection = getConnection(filename);
             Statement statement = connection.createStatement()) { // Einfaches Statement genügt hier

            int rowsAffected = statement.executeUpdate(sql); // Führt den DELETE-Befehl aus
            System.out.println(rowsAffected + " Mitarbeiter erfolgreich aus der Datenbank gelöscht.");

        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen aller Mitarbeiter: " + e.getMessage());
            e.printStackTrace(); // Für detaillierteres Debugging
        }
    }

    public static void addEmployees(List<Employee> employeesToAdd, boolean replaceOnDuplicate) {
        if (employeesToAdd == null || employeesToAdd.isEmpty()) {
            System.out.println("Keine Mitarbeiter zum Hinzufügen vorhanden.");
            return;
        }

        // SQL-String basierend auf replaceOnDuplicate auswählen
        // Spaltennamen müssen exakt mit der DB-Tabelle übereinstimmen!
        String sql;
        if (replaceOnDuplicate) {
            // Für SQLite: INSERT OR REPLACE
            // Für MySQL: INSERT INTO ... ON DUPLICATE KEY UPDATE col1=VALUES(col1), ...
            // Für PostgreSQL: INSERT INTO ... ON CONFLICT (pk_col) DO UPDATE SET col1 = EXCLUDED.col1, ...
            sql = "INSERT OR REPLACE INTO Employees (emp_id, name, birthday, email_adress, salary, start_date, manager_id, role, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Employees (emp_id, name, birthday, email_adress, salary, start_date, manager_id, role, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        int successfulInserts = 0;
        int failedInserts = 0;

        try {
            connection = getConnection(filename);
            // Wichtig: AutoCommit ausschalten für Batch-Verarbeitung und potenzielle Rollbacks
            connection.setAutoCommit(false);

            pstmt = connection.prepareStatement(sql);

            for (Employee emp : employeesToAdd) {
                try {
                    // Parameter im PreparedStatement setzen (Index beginnt bei 1)
                    pstmt.setInt(1, emp.getId());
                    pstmt.setString(2, emp.getName());
                    pstmt.setString(3, emp.getBirthdayString()); // Annahme: Geburtstag als String speichern
                    pstmt.setString(4, emp.getEmailAddress());
                    pstmt.setBigDecimal(5, emp.getSalary());     // Verwende setBigDecimal für NUMERIC/DECIMAL
                    pstmt.setString(6, emp.getStartDateString()); // Annahme: Startdatum als String speichern
                    pstmt.setInt(7, emp.getManagerId());      // Füge managerId hinzu (int)
                    pstmt.setString(8, emp.getRole());
                    pstmt.setString(9, emp.getDepartment());    // Füge department hinzu (String)

                    pstmt.addBatch(); // Füge den aktuellen Satz von Parametern zum Batch hinzu
                } catch (SQLException e) {
                    System.err.println("Fehler beim Vorbereiten des Batches für Mitarbeiter ID " + emp.getId() + ": " + e.getMessage());
                    failedInserts++;
                }
            }

            // Führe den Batch aus
            int[] results = pstmt.executeBatch();
            connection.commit(); // Transaktion abschließen, wenn alles gut ging

            // Ergebnisse auswerten (optional, executeBatch gibt oft nur die Anzahl der betroffenen Zeilen zurück)
            for (int result : results) {
                // Statement.SUCCESS_NO_INFO (-2) ist normal für erfolgreiche Batches bei manchen Treibern
                // Statement.EXECUTE_FAILED (-3) zeigt einen Fehler im Batch an
                if (result >= 0 || result == Statement.SUCCESS_NO_INFO) {
                    successfulInserts++;
                } else if (result == Statement.EXECUTE_FAILED) {
                    // Es ist schwierig herauszufinden, welcher Eintrag fehlgeschlagen ist,
                    // ohne die Batches einzeln oder in kleineren Gruppen auszuführen.
                    // Für diese Implementierung zählen wir sie als fehlgeschlagen, falls EXECUTE_FAILED zurückgegeben wird.
                    // Die Gesamtzahl der Fehler könnte höher sein als die Anzahl der EXECUTE_FAILED-Rückgaben.
                    System.err.println("Einige Batch-Operationen sind fehlgeschlagen (Rückgabewert: " + result + ").");
                    // Wir können nicht sicher sagen, wie viele fehlgeschlagen sind,
                    // aber wir wissen, dass mindestens einer es tat, wenn wir hier landen.
                }
            }
            // Adjust failed count if it wasn't incremented during batch prep errors
            if (failedInserts == 0 && successfulInserts < employeesToAdd.size()) {
                failedInserts = employeesToAdd.size() - successfulInserts;
            }


            System.out.println("Mitarbeiter-Batch-Verarbeitung abgeschlossen. Erfolgreich: " + successfulInserts + ", Fehlgeschlagen: " + failedInserts);


        } catch (BatchUpdateException bue) {
            System.err.println("Fehler während der Batch-Ausführung: " + bue.getMessage());
            // Detailliertere Informationen über fehlgeschlagene Updates
            int[] updateCounts = bue.getUpdateCounts();
            failedInserts = 0; // Reset counter, use updateCounts
            successfulInserts = 0;
            for (int i = 0; i < updateCounts.length; i++) {
                if (updateCounts[i] >= 0 || updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                    successfulInserts++;
                } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                    failedInserts++;
                    // Versuche, den fehlerhaften Mitarbeiter zu identifizieren (Index i in der ursprünglichen Liste)
                    if (i < employeesToAdd.size()) {
                        System.err.println(" -> Fehler wahrscheinlich bei Mitarbeiter ID: " + employeesToAdd.get(i).getId());
                    }
                }
            }
            System.err.println("Batch-Update-Details - Erfolgreich: " + successfulInserts + ", Fehlgeschlagen: " + failedInserts);

            // Versuche, die Transaktion zurückzurollen
            try {
                if (connection != null) {
                    System.err.println("Versuche Rollback der Transaktion...");
                    connection.rollback();
                    System.err.println("Rollback erfolgreich.");
                }
            } catch (SQLException ex) {
                System.err.println("Fehler beim Rollback der Transaktion: " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Allgemeiner SQL-Fehler beim Hinzufügen von Mitarbeitern: " + e.getMessage());
            e.printStackTrace();
            // Versuche, die Transaktion zurückzurollen
            try {
                if (connection != null) {
                    System.err.println("Versuche Rollback der Transaktion...");
                    connection.rollback();
                    System.err.println("Rollback erfolgreich.");
                }
            } catch (SQLException ex) {
                System.err.println("Fehler beim Rollback der Transaktion: " + ex.getMessage());
            }

        } finally {
            // Ressourcen im finally-Block schließen (wichtig!)
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
            try {
                if (connection != null) {
                    // AutoCommit wieder auf den Standardwert setzen (normalerweise true)
                    try {
                        connection.setAutoCommit(true);
                    } catch (SQLException e) { /* Ignorieren, wenn schon geschlossen */ }
                    connection.close();
                }
            } catch (SQLException e) { e.printStackTrace(); }
        }
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
