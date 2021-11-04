package db;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseService {

    final static String URL = "jdbc:mysql://127.0.0.1:3306/chatdb" +
            "?serverTimezone=Europe/Moscow&useSSL=false";

    final static String LOGIN = "root";
    final static String PASSWORD = "depeche150580";
    private static Connection connection;

    private static Connection getConnection() {
        if(connection != null){
            return connection;
        }
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return connection;
        }
    }


    public static ArrayList<User> selectUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM chatdb.chatusers;");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getNString("name");
//                String message = resultSet.getString("message");
//                Time dateandtime = resultSet.getTime("dateandtime");
//                users.add(new User(id, name, message, dateandtime));
                users.add(new User(id, name));
            }
//            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return users;
        }
    }

    public static boolean createUser(User user){
        boolean result = false;
        try {
            String commandText = "INSERT INTO chatusers (name) VALUES(?)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(commandText);
            preparedStatement.setString(1, user.getName());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public static boolean deleteUser(User user){
        boolean result = false;
        try {
            String commandText = "DELETE FROM chatusers WHERE name = ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(commandText);
            preparedStatement.setString(1, user.getName());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }


}
