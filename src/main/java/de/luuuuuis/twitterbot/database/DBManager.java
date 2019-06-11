package de.luuuuuis.twitterbot.database;

import java.sql.*;

public class DBManager {

    public static Connection connection;

    public static void init() {

        String url = "jdbc:sqlite:TwitterDB.sqlite";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            if (conn != null) {
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("SQLITE >> Connected to " + metaData.getDatabaseProductName());

                connection = conn;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Create Tables
        try {
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS followers(ID BIGINT, DATE BIGINT, NUM INT)");
            System.out.println("SQL >> Successfully created all tables");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void close() {
        if (!isConnected()) return;
        try {
            connection.close();
            System.out.println("SQL >> Successfully closed the connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getResult(String query) {
        if (!isConnected()) return null;

        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean isConnected() {
        return connection != null;
    }
}
