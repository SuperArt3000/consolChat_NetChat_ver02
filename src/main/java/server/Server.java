package server;

import client.Client;
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

    //    private final ArrayList<Connection> connections = new ArrayList<>();
    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());//*проба

    private final ServerSocket server;

    private final ArrayList<User> users = new ArrayList<>();

    User user;


    /**
     * Конструктор создаёт сервер. Затем для каждого подключения создаётся
     * объект Connection и добавляет его в список подключений.
     */
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
        //*****
//        user = new User(connection.getLogin(), currentDateAndTime(), connection.getMessage());
        user = new User(connection.getLogin(), currentDateAndTime());
        DatabaseService.createUser(user);
        users.add(user);

//        new User(connection.getLogin(), connection.getMessage(), currentDateAndTime());
//        DatabaseService.createMessage(user);

        //*****
        System.out.println(ANSI_YELLOW + "(" + currentDateAndTime() + ") " + "Подключение пользователя: " + connection.getLogin() + ANSI_RESET);
        sendMessage(ANSI_YELLOW + connection.getLogin() + " - заходит в чат" + ANSI_RESET);
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
        System.out.println(ANSI_YELLOW + "(" + currentDateAndTime() + ") " + "Отключение пользователя: " + connection.getLogin() + ANSI_RESET);
        sendMessage(ANSI_YELLOW + connection.getLogin() + " - выходит из чата" + ANSI_RESET);

        //*****
        for (User user : users) {
            if (user.getName().equals(connection.getLogin())) {
                DatabaseService.deleteMessage(user);//**proba
                DatabaseService.deleteUser(user);
                System.out.println("пользователь: " + user.getName() + " удален из базы данных");
            }
        }
        //****

    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Ошибка соединения: " + e);
    }


    private synchronized void sendMessage(String message) {
        Iterator<Connection> connectionIterator = connections.iterator();
        while (connectionIterator.hasNext()) {
            connectionIterator.next().sendMessage("(" + currentDateAndTime() + ") " + message);
        }
    }

    private synchronized void sendMessage(Connection connection, String message) {
        String finalMessage = (ANSI_YELLOW + "(" + currentDateAndTime() + ") " + ANSI_RESET + ANSI_RED + connection.getLogin() + ANSI_RESET + ": " + message);
        MessageHistory.addHistoryEl(finalMessage);
        Iterator<Connection> connectionIterator = connections.iterator();
        while (connectionIterator.hasNext()) {
            Connection nextConnection = connectionIterator.next();
            if (!connection.equals(nextConnection))
//                nextConnection.sendMessage(ANSI_YELLOW + "(" + currentDateAndTime() + ") " + ANSI_RESET + ANSI_RED + connection.getLogin() + ANSI_RESET + ": " + message);
                nextConnection.sendMessage(finalMessage);
        }
//        DatabaseService.createMessage(connection, finalMessage);
    }


    //*******
    public String currentDateAndTime() {
        Date date = new Date(); // текущая дата
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(date);
    }

    public String getMessage(String message) {
        return message;
    }


//    public java.sql.Date dateAndTime() {
////        Calendar calendar = Calendar.getInstance();
////        java.util.Date currentDate = calendar.getTime();
////        java.sql.Date date = new java.sql.Date(currentDate.getTime());
//
////        return new java.sql.Date(Calendar.getInstance().getTime().getTime());
//        java.sql.Date myAnotherDate = new java.sql.Date( (new java.util.Date()).getTime());
//        return myAnotherDate;
//    }


    //*******

}