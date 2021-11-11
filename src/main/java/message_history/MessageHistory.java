package message_history;

import main.Connection;
import java.util.LinkedList;

/**
 * Класс MessageHistory отвечает за сохранение сообщений и
 * последовательную отсылку 10-ти последних сообщений из чата
 * вновь подключившемуся пользователю.
 */

public class MessageHistory {

    private static final LinkedList<String> messHistory = new LinkedList<>();

    public static void addHistoryEl(String el) {
        if (messHistory.size() >= 10) {
            messHistory.removeFirst();
        }
        messHistory.add(el);

    }

    public static void printMessHistory(Connection connection) {
        if (messHistory.size() > 0) {
            connection.sendMessage("История 10 последних сообщений сообщений в чате:");
            for (String message : messHistory) {
                connection.sendMessage(message);
            }
            connection.sendMessage("/...конец истории сообщений.../");
        }
    }
}







