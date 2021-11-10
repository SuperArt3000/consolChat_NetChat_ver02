package main;

import db.User;
import message_history.MessageHistory;
import server.Server;

import static main.Const.EXIT_CODE;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Connection extends Thread {

    private String login;
    private final Socket socket;
    private final Server eventListener;
    private final BufferedReader in;
    private final PrintWriter out;

    private String message;//**proba

    //    public Connection(ConnectionListener eventListener, Socket socket) throws IOException {
    public Connection(Server eventListener, Socket socket) throws IOException {

        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        start();

    }

    @Override
    public void run() {
        try {
            login = in.readLine();
            eventListener.onConnectionReady(Connection.this);
            String mes = "";
            while (!mes.equals(EXIT_CODE)) {
                mes = in.readLine();
                eventListener.onReceiveString(Connection.this, mes);
                message = mes;//**proba
            }
            eventListener.onDisconnect(Connection.this);
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
        } finally {
            close();
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

//    public synchronized void disconnect() {//**
//        currentThread().interrupt();
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    @Override
//    public String toString() {
//        return "Соединение: " + socket.getInetAddress() + ": " + socket.getPort();
//    }

    public String getLogin() {
        return login;
    }
    public String getMessage() {
        return message;
    }

    public String currentDateAndTime() {
        Date date = new Date(); // текущая дата
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateAndTime = sdf.format(date);
        return currentDateAndTime;
    }




}
