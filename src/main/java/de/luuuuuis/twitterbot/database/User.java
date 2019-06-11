package de.luuuuuis.twitterbot.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {


    public static boolean userExists(Long ID) {
        try (ResultSet rs = DBManager.getResult("SELECT * FROM followers WHERE ID='" + ID + "'")) {
            if (rs.next()) {
                return rs.getString("ID") != null;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void create(Long ID, int num) {
        if (userExists(ID)) {
            System.err.println("SQL >> User already exists");
            return;
        }

        try (PreparedStatement preparedStatement = DBManager.connection.prepareStatement("INSERT INTO followers(ID, DATE, NUM) VALUES (?, ?, ?)")) {

            preparedStatement.setLong(1, ID);
            preparedStatement.setLong(2, System.currentTimeMillis());
            preparedStatement.setInt(3, num);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("BetaKey >> Player created");

    }

}
