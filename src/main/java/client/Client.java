package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import main.Const;

import static main.Const.ANSI_RED;
import static main.Const.ANSI_RESET;
import static main.Const.EXIT_CODE;

public class Client {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public Client() {
        Scanner scan = new Scanner(System.in);
/*
		System.out.println("Введите IP для подключения к серверу.");
		System.out.println("Формат: xxx.xxx.xxx.xxx");
		String ip = scan.nextLine();
*/
        String ip = "127.0.0.1";
        try {

        // Подключаемся в серверу и получаем потоки(in и out) для передачи сообщений
        socket = new Socket(ip, Const.PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        System.out.print("Введите свой ник:");
        out.println(scan.nextLine());

        // Запускаем вывод всех входящих сообщений в консоль
        Resender resend = new Resender();
        resend.start();

//         Пока пользователь не введёт "exit" отправляем на сервер всё, что
//         введено из консоли

        String str = "";
            while (!str.equals("exit")) {
                str = scan.nextLine();
                out.println(str);
            }
//        while (!str.equals(EXIT_CODE)) {
//            str = scan.nextLine();
//            out.println(str);
//        }
            resend.setStop();

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        Client.this.close();
//        close();

    }
}


    /**
     * Закрывает входной и выходной потоки и сокет
     */
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Потоки не были закрыты!");
        }
    }



     /* Класс в отдельной нити пересылает все сообщения от сервера в консоль.
   Работает пока не будет вызван метод setStop().

      */

    public class Resender extends Thread {
        private boolean stoped;

        public synchronized void setStop() {
            stoped = true;
        }

        /**
         * Считывает все сообщения от сервера и печатает их в консоль.
         * Останавливается вызовом метода setStop()
         */

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
//        @Override
//        public void run() {
//            try {
//                String str = "";
//                while (!stoped) {
//                    try {
//                        str = in.readLine();
//                    } catch (SocketException e){
//                        System.out.println(ANSI_RED + "Произошло отключение от сервера!" + ANSI_RESET);
//                    }
//                    System.out.println(str);
//                }
//            } catch (IOException e) {
//                System.err.println("Ошибка при получении сообщения.");
//                e.printStackTrace();
//            }
//        }



    }




    //**************************

//    private void closeService() {
//        try {
//            if (!socket.isClosed()) {
//                socket.close();
//                in.close();
//                out.close();
//            }
//        } catch (IOException e) {
////            e.printStackTrace();
//        }
//    }

//    // отдельный поток для чтения сообщений с сервера
//    private class ReadMessage extends Thread {
//        @Override
//        public void run() {
//            String message;
//            try {
//                while (true) {
//                    message = in.readLine(); // ждем сообщения от сервера
//                    if (message.equals("stop")) {
//                        Client.this.close(); // если пришло "stop" закрываем соединение
//                        break; // выходим
//                    }
//                    System.out.println(message); // отправляем сообщения с сервера на консоль
//                }
//            } catch (IOException e) {
//                Client.this.close(); //если ошибка во входящем потоке закрываем соединение
//            }
//        }
//    }

//
//    // отдельный поток отправляющий сообщения с консоли на сервер
//    public class WriteMessage extends Thread {
//        public Date time;
//        public String dtime;
//        public SimpleDateFormat dt1;
//
//        @Override
//        public void run() {
//            while (true) {
//                String message;
//                try {
//                    time = new Date(); // текущая дата
//                    dt1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // берем только дату и время до секунд
//                    dtime = dt1.format(time); // время
//                    message = userKeyboard.readLine(); // сообщения с консоли
//                    if (message.equals("stop")) {
//                        System.out.println(nickName + " Вы вышли из чата");//работает оставляем
//                        out.write("(" + dtime + ") " + "Пользователь " + nickName + " вышел из чата");//работает
//                        out.flush();//работает
//                        Client.this.close(); // если пришло "stop" закрываем соединение
//                        break; // выходим
//                    } else {
//                        out.write( "(" + dtime + ") " + nickName + ": " + message + "\n"); // отправляем на сервер
//                    }
//                    out.flush(); // чистим
//                } catch (IOException e) {
//                    Client.this.close(); // в случае исключения закрываем соединение
//
//                }
//
//            }
//        }
//    }
//*****************************

}

