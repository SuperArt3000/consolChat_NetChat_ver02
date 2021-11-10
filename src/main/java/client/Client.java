package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import main.Const;

import static main.Const.EXIT_CODE;

public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    private String login;
//    private static String str;

    public Client() {
        Scanner scan = new Scanner(System.in);
        String ip = "127.0.0.1";
        try {
            socket = new Socket(ip, Const.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Введите свой ник:");
            login = scan.nextLine();
            out.println(login);

            // Запускаем вывод всех входящих сообщений в консоль
            Resender resend = new Resender();
            resend.start();

            String str = "";
            while (!str.equals(EXIT_CODE)) {
                str = scan.nextLine();
                out.println(str);
            }
            resend.setStop();
//            this.close();//**proba - попробовать
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            Client.this.close();
        close();
        }
    }


    //Закрывает входной и выходной потоки и сокет
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }

//    public static String getMessage(){
//        return str;
//    }

    //Считывает все сообщения от сервера и печатает их в консоль.
    public class Resender extends Thread {

        private boolean stoped;

        public synchronized void setStop() {
            stoped = true;
            System.out.println(login + " Вы вышли из чата");//**
        }

        @Override
        public void run() {
            try {
                while (!stoped) {
                    String str = in.readLine();
                    System.out.println(str);
                }
            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            }
        }
    }
}