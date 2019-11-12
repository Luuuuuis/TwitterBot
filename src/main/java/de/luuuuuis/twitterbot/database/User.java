package de.luuuuuis.twitterbot.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    public static boolean userExists(long id) {
        try (ResultSet rs = DBManager.getResult("SELECT * FROM followers WHERE ID='" + id + "'")) {
            if (rs == null)
                return false;

            if (rs.next()) {
                return rs.getString("ID") != null;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void create(long id, int num) {
        if (userExists(id)) {
            System.err.println("SQL >> User already exists");
            return;
        }

        try (PreparedStatement preparedStatement = DBManager.connection.prepareStatement("INSERT INTO followers(ID, DATE, NUM) VALUES (?, ?, ?)")) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, System.currentTimeMillis());
            preparedStatement.setInt(3, num);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("TwitterBot >> Player created");
    }

}
