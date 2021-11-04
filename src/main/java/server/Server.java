package server;

import db.DatabaseService;
import db.User;
import main.Connection;
import main.ConnectionListener;
import main.Const;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static main.Const.*;

/**
 * Обеспечивает работу программы в режиме сервера
 */
public class Server implements ConnectionListener {

    /**
     * Специальная "обёртка" для ArrayList, которая обеспечивает
     *  доступ к массиву из разных потоков
     */
//    private final ArrayList<Connection> connections = new ArrayList<>();
    private final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());//*проба

    private final ServerSocket server;

    public Connection connection;



    //*****
//    public String name;
//    ArrayList<User> users = DatabaseService.selectUsers();
    private final ArrayList<User> users = new ArrayList<>();

    //*****

    /**
     * Конструктор создаёт сервер. Затем для каждого подключения создаётся
     * объект Connection и добавляет его в список подключений.
     */
    public Server() {
            System.out.println("Сервер запущен");
        try{
            server = new ServerSocket(Const.PORT);
            while(true) {
                try {
                    new Connection(this, server.accept());
//                    connection = new Connection(this, server.accept());
//                    connection.start();
                } catch (IOException e) {
                    System.out.println("Ошибка соединения в сервере: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("сервер виноват");
            serverClose();
//            try {
//                server.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            connection.close();
//            closeAll();
        }
    }

private void serverClose(){
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
User user = new User(connection.getLogin());
DatabaseService.createUser(user);
users.add(user);
        //*****
        System.out.println("Подключение пользователя: " + connection.getLogin());
        sendMessage(ANSI_YELLOW + connection.getLogin() + " заходит в чат" + ANSI_RESET);
    }

//    @Override
//    public synchronized void onReceiveString(Connection connection, String message) {
//        sendMessage(connection, message);
//    }

   @Override
    public synchronized void onReceiveString(Connection connection, String message) {
            sendMessage(connection, message);
    }



    @Override
    public synchronized void onDisconnect(Connection connection) {

        connections.remove(connection);
        System.out.println("Отключение пользователя: " + connection.getLogin());
        sendMessage(ANSI_YELLOW + connection.getLogin() + " выходит из чата" + ANSI_RESET);

        //*****
        for (User user: users) {
            if(user.getName().equals(connection.getLogin())){
                DatabaseService.deleteUser(user);
                System.out.println("пользователь " + user.getName() + "удален из базы данных");
            }
        }
        //****

//        this.connection.close();//**проба

    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("Ошибка соединения: " + ": от метода onException :" + e);
    }


    private synchronized void sendMessage(String message){
        Iterator<Connection> connectionIterator = connections.iterator();
        while(connectionIterator.hasNext()) {
            connectionIterator.next().sendMessage(message);
        }
    }

    private synchronized void sendMessage(Connection connection, String message){
        Iterator<Connection> connectionIterator = connections.iterator();
        while(connectionIterator.hasNext()) {
            Connection nextConnection = connectionIterator.next();
            if(!connection.equals(nextConnection))
            nextConnection.sendMessage(ANSI_RED + connection.getLogin() + ANSI_RESET + ": " + message);
        }
    }
}