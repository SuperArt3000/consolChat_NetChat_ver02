package main;

import db.User;
import server.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import static main.Const.EXIT_CODE;

public class Connection extends Thread {

    private String login;
    private final Socket socket;
//    private final Thread thread;
//    private final ConnectionListener eventListener;
    private final Server eventListener;
    private final BufferedReader in;
    private PrintWriter out;//**proba
    User user;

//    public Connection(ConnectionListener eventListener, Socket socket) throws IOException {
    public Connection(Server eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//**proba
        out = new PrintWriter(socket.getOutputStream(), true);//**proba
        start();
    }

    @Override
    public void run() {
        try {
            login = in.readLine();
            eventListener.onConnectionReady(Connection.this);
            //???? вот здесь может быть ошибка моя
            String mes = "";
            while (!mes.equals("exit")) {
                mes = in.readLine();
                eventListener.onReceiveString(Connection.this, mes);
            }
//            while (true) {
//                String mes = in.readLine();
//                if(mes.equals("exit")) break;
//                eventListener.onReceiveString(Connection.this, mes);
//            }
            eventListener.onDisconnect(Connection.this);

        } catch (IOException e) {
            eventListener.onException(Connection.this, e);

        }
        finally {
            close();

//            Connection.currentThread().interrupt();
//            Connection.this.close();

//            try {
//                in.close();
//                out.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            eventListener.onDisconnect(Connection.this);
        }
    }


    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }




    public synchronized void sendMessage(String message) {
        out.println(message);
    }

//    public synchronized void sendMessage(String message) {
//        out.write(message + "\r\n");
//        out.flush();
//    }

//    public synchronized void disconnect() {
//        thread.interrupt();
//        try {
//            socket.close();
//        } catch (IOException e) {
//            System.out.println("метод disconnect в Connection:");
//            eventListener.onException(this, e);
//        }
//    }

//    @Override
//    public String toString() {
//        return "Соединение: " + socket.getInetAddress() + ": " + socket.getPort();
//    }

    public String getLogin() {
        return login;
    }



}



//public class Connection {
//
//    private String login;
//    private final Socket socket;
//    private final Thread thread;
//    private final ConnectionListener eventListener;
//    private final BufferedReader in;
//    private final BufferedWriter out;
//
//
//    public Connection(ConnectionListener eventListener, Socket socket) throws IOException {
//        this.eventListener = eventListener;
//        this.socket = socket;
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
//
//        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
//
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    login = in.readLine();
//                    eventListener.onConnectionReady(Connection.this);
//                    while (!thread.isInterrupted()) {
////                    while (!in.readLine().equals(EXIT_CODE)) {//**proba
//
//                        eventListener.onReceiveString(Connection.this, in.readLine());
//                    }
//
//                } catch (IOException e) {
//                    System.out.println("конструктор Connection: ");
//                    eventListener.onException(Connection.this, e);
//                }
//                finally {
//                    eventListener.onDisconnect(Connection.this);
//                }
//            }
//        });
//        thread.start();
//    }
//
//
//
//
//
//    public synchronized void sendMessage(String message) {
//        try {
//            out.write(message + "\r\n");
//            out.flush();
//        } catch (IOException e) {
//            System.out.println("метод sendMessage в Connection:");
//            eventListener.onException(this, e);
//            disconnect();
//        }
//    }
//
//    public synchronized void disconnect() {
//        thread.interrupt();
//        try {
//            socket.close();
//        } catch (IOException e) {
//            System.out.println("метод disconnect в Connection:");
//            eventListener.onException(this, e);
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "Соединение: " + socket.getInetAddress() + ": " + socket.getPort();
//    }
//
//    public String getLogin() {
//        return login;
//    }
//
//}
