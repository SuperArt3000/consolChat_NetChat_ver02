package message_history;

import main.Connection;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageHistory {

//    private static final ArrayList<String> messHistory = new ArrayList<>();
    private static final LinkedList<String> messHistory = new LinkedList<>();


//    public static void addHistoryEl(String el) {
//        messHistory.add(el);
//    }
//
//    public static String getHistory(String el) {
//        return el;
//    }


    public static void addHistoryEl(String el) {
        // если сообщений больше 10, удаляем первое и добавляем новое
        // иначе просто добавить
        if (messHistory.size() >= 10) {
            messHistory.removeFirst();
        }
        messHistory.add(el);

    }

    /**
     * отсылаем последовательно каждое сообщение из списка
     * в поток вывода данному клиенту (новому подключению)
     */

    public static void printMessHistory(Connection connection) {
        if (messHistory.size() > 0) {
            connection.sendMessage("История 10 последних сообщений сообщений в чате:");
            for (String mess : messHistory) {
                connection.sendMessage(mess);
            }
            connection.sendMessage("/...конец истории сообщений.../");
        }
    }

//    public static void printMessHistory(Connection connection) {
//        String send;
//        if (messHistory.size() > 0) {
//            connection.sendMessage("История 10 последних сообщений сообщений в чате:");
//            for (int i = messHistory.size() - 1; i > messHistory.size()-11; i--) {
//                send = messHistory.get(i);
//                connection.sendMessage(send);
//            }
//            connection.sendMessage("/...конец истории сообщений.../");
//        }
//    }


}







