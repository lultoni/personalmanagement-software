package src;

import java.sql.*;

public class DB_API {

    private static final String filename = "database.db";

    public static Connection getConnection(String filename){
        Connection connection = null;
        try{
            //Verbindung zur SQLite-Datenbank herstellen
            connection = DriverManager.getConnection("jdbc:sqlite:"+filename);
            //System.out.println("Verbindung zur SQLite-Datenbank hergestellt.");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }

// FOLLOWING IS A EXAMPLE METHOD, MAYBE WE CAN USE IT LATER ON IDK
//    public static void showAllPlayers() {
//        try{
//            String sql = "SELECT * FROM player;";
//            Connection connection = getConnection(filename);
//            PreparedStatement statement = connection.prepareStatement(sql);
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()){
//                int id = resultSet.getInt("player_id");
//                String name = resultSet.getString("name");
//                int self_rating = resultSet.getInt("self-rating");
//
//                String taskOutput = "(" + id + ") " + name + " --- " + self_rating;
//
//                System.out.println(taskOutput);
//            }
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//    }

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
