package com.finalYearProject.queueManagement.helpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class TableDeleter {


        public static void main(String[] args) {
            cleanDatabase();
        }

        public static void cleanDatabase() {
            // Database connection details
            String url = "jdbc:mysql://localhost:3306/new";
            String username = "root";
            String password = "mysql2212";

            // Establishing database connection
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();

                // Fetching all table names
                String query = "SHOW TABLES";
                statement.execute(query);
                var resultSet = statement.getResultSet();

                // Deleting each table
                while (resultSet.next()) {
                    String tableName = "branch";
                    String dropQuery = "DROP TABLE " + tableName;
                    statement.executeUpdate(dropQuery);
                    System.out.println("Table '" + tableName + "' deleted successfully.");
                }

                System.out.println("All tables deleted successfully.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

