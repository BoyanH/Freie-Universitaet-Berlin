package network;

import fx.ClientGUI;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ConnectionThreadTCP extends Thread {

    private String serverAddress;
    private int serverPort;
    private Socket serverSocket;
    private boolean running = true;
    private ClientTCP client;


    public ConnectionThreadTCP(String address, String port, ClientTCP client) throws NumberFormatException {
        this.serverAddress = address;
        this.serverPort = Integer.parseInt(port);
        this.client = client;
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
        try {
            this.serverSocket = new Socket(this.serverAddress, this.serverPort);
            this.client.setConnected(true);
            this.client.connectToChat();
            this.waitForMessages();
        } catch (IOException e) {
            System.out.println("Couldn't connect to server");
            this.terminate();
        }
    }

    /**
     * Waits for server to send a message. If the new message is null, then the server has close the
     * connection and we need to notify the client. If a new message is received, call ClientTCP::onServerMessage()
     */
    public void waitForMessages() {
        String inputLine;
        BufferedReader socketBuffer;

        try {
            socketBuffer = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
        } catch (IOException e) {
            this.terminate();
            return;
        }

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
            } catch (java.net.SocketTimeoutException e) {
                // timed out, server is there, has nothing to say
            } catch (IOException e) {
                this.terminate();
            }
        }
    }
}
