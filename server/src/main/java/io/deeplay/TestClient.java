package io.deeplay;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestClient {
    private static final Logger LOG = LoggerFactory.getLogger(TestClient.class);
    private static final int PORT = 8189;
    private static final String HOST = "localhost";
    private Socket socket = null;
    private ObjectDecoderInputStream input = null;
    private ObjectEncoderOutputStream output = null;

    private void start() {
        try {
            LOG.info("Попытка подключения к серверу");
            socket = new Socket(HOST, PORT);
            LOG.info("Успешно подключено к серверу");
        } catch (IOException e) {
            LOG.error("Connection failed", e);
        }

        try {
            input = new ObjectDecoderInputStream(socket.getInputStream());
            output = new ObjectEncoderOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LOG.error("Connection failed", e);
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
                    LOG.info("Получено сообщение от сервера: " + inputMessage);
                }
            } catch (IOException | ClassNotFoundException e) {
                LOG.error("Ошибка", e);
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
            LOG.error("Ошибка", e);
        }
    }

    private void sendMessage(String message) {
        try {
            output.writeObject(message);
            LOG.info("Отправлено сообщение серверу: " + message);
        } catch (IOException e) {
            LOG.error("Ошибка при отправке сообщения на сервер: " + e);
        }
    }


    public static void main(String[] args) {
        new TestClient().start();
    }
}
