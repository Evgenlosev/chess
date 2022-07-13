package io.deeplay;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TemporaryClient {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(TemporaryClient.class);
    private static final int PORT = 8189;
    private static final String HOST = "localhost";
    private Socket socket = null;
    private ObjectDecoderInputStream input = null;
    private ObjectEncoderOutputStream output = null;

    private void start() {
        try {
            logger.info("Попытка подключения к серверу");
            socket = new Socket(HOST, PORT);
            logger.info("Успешно подключено к серверу");
        } catch (IOException e) {
            logger.error("Connection failed", e);
        }

        try {
            input = new ObjectDecoderInputStream(socket.getInputStream());
            output = new ObjectEncoderOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error("Connection failed", e);
            shutDown();
        }

        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
                while (true) {
                    String textMessage = br.readLine();
                    sendMessage(textMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    String inputMessage = (String) input.readObject();
                    logger.info("Получено сообщение от сервера: " + inputMessage);
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.error("Ошибка", e);
            }
        }).start();
    }

    private void shutDown() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка", e);
        }
    }

    private void sendMessage(String message) {
        try {
            output.writeObject(message);
            logger.info("Отправлено сообщение серверу: " + message);
        } catch (IOException e) {
            logger.error("Ошибка при отправке сообщения на сервер: " + e);
        }
    }


    public static void main(String[] args) {
        new TemporaryClient().start();
    }
}
