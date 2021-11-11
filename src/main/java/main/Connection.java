package main;

import server.Server;
import static main.Const.EXIT_CODE;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Connection extends Thread {

    private String name;
    private final Socket socket;
    private final Server eventListener;
    private final BufferedReader in;
    private final PrintWriter out;

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
            name = in.readLine();
            eventListener.onConnectionReady(Connection.this);
            while (true) {
                String message = in.readLine();
                eventListener.onReceiveString(Connection.this, message);
                System.out.println("logging: " + "пользователь " + Connection.this.getUserName() +
                        " отправил сообщение - " +  "\"" + message + "\"" + " * " + currentDateAndTime());
                if(message.equals(EXIT_CODE)){
                    break;
                }
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

    public String getUserName() {
        return name;
    }

    public String currentDateAndTime() {
        Date date = new Date(); // текущая дата
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(date);
    }
}
