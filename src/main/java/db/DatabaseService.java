package db;

import java.sql.*;

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
        }
        return connection;
    }


//    public static ArrayList<User> selectUsers() {
//        ArrayList<User> users = new ArrayList<>();
//        try {
//            Statement statement = getConnection().createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM chatdb.chatusers;");
//            while (resultSet.next()) {
//                String name = resultSet.getNString("name");
//                String dateandtime = resultSet.getString("dateandtime");
//                users.add(new User(name, dateandtime));
//            }
////            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return users;
//    }

    public static boolean createUser(User user){
        boolean result = false;
        try {
            String commandText = "INSERT INTO chatusers (name, dateandtime) VALUES(?,?)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(commandText);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getDateandTime());
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

    public static boolean createMessage(main.Connection connection, String message){
        boolean result = false;
        try {
            String commandText = "INSERT INTO messagehistory (name, message, dateandtime) VALUES(?,?,?)";
            PreparedStatement preparedStatement = getConnection().prepareStatement(commandText);
            preparedStatement.setString(1, connection.getUserName());
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, connection.currentDateAndTime());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    public static boolean deleteMessage(User user){
        boolean result = false;
        try {
            String commandText = "DELETE FROM messagehistory WHERE name = ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(commandText);
            preparedStatement.setString(1, user.getName());
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
