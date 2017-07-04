package network;

import fx.ClientGUI;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class MessageListenerTCP extends Thread {

    private final int MAX_CHAT_REFRESH_LENGTH = 4096;
    private String serverAddress;
    private int serverPort;
    private Socket serverSocket;
    private boolean running = true;
    private ClientTCP client;
    private BufferedReader socketBuffer;


    public MessageListenerTCP(Socket serverSocket, ClientTCP client) throws NumberFormatException {
        this.serverSocket = serverSocket;
        this.client = client;

        try {
            this.socketBuffer = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
            this.socketBuffer.mark(MAX_CHAT_REFRESH_LENGTH);
        } catch (IOException e) {
            this.terminate();
            return;
        }
    }

    public BufferedReader getReader() {
        return this.socketBuffer;
    }

    public void terminate() {
        this.running = false;
        this.client.setConnected(false);
    }

    public Socket getServerSocket() {
        return this.serverSocket;
    }

    @Override
    public void run() {
        this.waitForMessages();
    }

    /**
     * Waits for server to send a message. If the new message is null, then the server has close the
     * connection and we need to notify the client. If a new message is received, call ClientTCP::onServerMessage()
     */
    public void waitForMessages() {
        String inputLine;

        try {
            this.serverSocket.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (this.running) {
            try {
                inputLine = socketBuffer.readLine();
                if (inputLine == null) {
                    // server not available
                    this.terminate();
                } else {
                    this.client.onServerMessage(inputLine);
                }
            } catch (SocketTimeoutException e) {
                // timed out, server is there, has nothing to say
            } catch (IOException e) {
                this.terminate();
            }
        }
    }
}
