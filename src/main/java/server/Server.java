package server;

import db.DatabaseService;
import db.User;
import main.Connection;
import main.ConnectionListener;
import main.Const;
import message_history.MessageHistory;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.*;
import static main.Const.*;

public class Server implements ConnectionListener {

    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    private final ServerSocket server;
    private final ArrayList<User> users = new ArrayList<>();
    private User user;

    public Server() {
        System.out.println("Сервер запущен");
        try {
            server = new ServerSocket(Const.PORT);
            while (true) {
                try {
                    new Connection(this, server.accept());
                } catch (IOException e) {
                    System.out.println("Ошибка соединения " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            serverClose();
        }
    }

    private void serverClose() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
        userStatus(connection);
        user = new User(connection.getUserName(), currentDateAndTime());
        DatabaseService.createUser(user);
        users.add(user);

        System.out.println(ANSI_YELLOW + "(" + currentDateAndTime() + ") " + "Подключение пользователя: " +
                connection.getUserName() + ANSI_RESET);
        sendMessage(ANSI_YELLOW + "(" + currentDateAndTime() + ") " + connection.getUserName() +
                " - заходит в чат" + ANSI_RESET);
        MessageHistory.printMessHistory(connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String message) {
        sendMessage(connection, message);
        DatabaseService.createMessage(connection, message);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        System.out.println(ANSI_RED + "(" + currentDateAndTime() + ") " + "Отключение пользователя: " +
                connection.getUserName() + ANSI_RESET);
        sendMessage(ANSI_RED + "(" + currentDateAndTime() + ") " + connection.getUserName() +
                " - выходит из чата" + ANSI_RESET);

        for (User user : users) {
            if (user.getName().equals(connection.getUserName())) {
                DatabaseService.deleteMessage(user);//**proba
                DatabaseService.deleteUser(user);
                System.out.println(ANSI_RED + "пользователь: " + user.getName() + " удален из базы данных" + ANSI_RESET);
            }
        }
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Ошибка соединения: " + e);
    }


    private synchronized void sendMessage(String message) {
        Iterator<Connection> connectionIterator = connections.iterator();
        while (connectionIterator.hasNext()) {
            connectionIterator.next().sendMessage(message);
        }
    }

    private synchronized void sendMessage(Connection connection, String message) {
        String finalMessage = (ANSI_YELLOW + "(" + currentDateAndTime() + ") " + ANSI_RESET + ANSI_RED + connection.getUserName() + ANSI_RESET + ": " + message);
        MessageHistory.addHistoryEl(finalMessage);
        Iterator<Connection> connectionIterator = connections.iterator();
        while (connectionIterator.hasNext()) {
            Connection nextConnection = connectionIterator.next();
            if (!connection.equals(nextConnection))
                nextConnection.sendMessage(finalMessage);
        }
    }

    public String currentDateAndTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public void userStatus(Connection connection) {
        if (connections.size() > 1) {
            connection.sendMessage("Сейчас пользователи он лайн: ");
            Iterator<Connection> connectionIterator = connections.iterator();
            while (connectionIterator.hasNext()) {
                Connection nextConnection = connectionIterator.next();
                if (!connection.equals(nextConnection)) {
                    connection.sendMessage(nextConnection.getUserName());
                }
            }
        }
    }
}