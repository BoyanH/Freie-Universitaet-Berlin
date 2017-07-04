package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientCommunicationThread extends Thread {

    static int clientsCount = 0;

    private int id;
    private Socket clientConnection;
    private String clientName;
    private boolean running = true;
    private ServerTCP server;
    private PrintWriter socketWriter;
    public long mutedBefore = System.currentTimeMillis();

    public ClientCommunicationThread(Socket connection, ServerTCP server) {
        this.id = ++clientsCount;
        this.clientConnection = connection;
        this.server = server;
        try {
            this.socketWriter = new PrintWriter(this.clientConnection.getOutputStream(), true); // auto-flush output stream
        } catch (IOException e) {
            e.printStackTrace();
            this.terminate();
        }
    }

    public String getUName() {
        return this.clientName;
    }

    public void setUName(String name) {
        if (this.clientName == null) {
            this.server.addClientToGUI(this.id, name);
        } else {
            this.server.clientRenamed(this.getClientId(), this.clientName, name);
        }
        this.clientName = name;
    }

    public PrintWriter getWriter() {
        return this.socketWriter;
    }

    public int getClientId() {
        return this.id;
    }

    @Override
    public void run() {
        this.waitForMessages();
    }

    public void terminate() {
        this.running = false;
        try {
            this.clientConnection.close();
            this.socketWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void waitForMessages() {
        String inputLine;
        BufferedReader socketBuffer;

        try {
            socketBuffer = new BufferedReader(new InputStreamReader(this.clientConnection.getInputStream()));
            socketBuffer.mark(4096);
        } catch (IOException e) {
            e.printStackTrace();
            this.terminate();
            return;
        }

        try {
            this.clientConnection.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (this.running) {
            try {
                inputLine = socketBuffer.readLine();
                if (inputLine == null) {
                    // client disconnected
                    this.server.removeClient(this);
                } else if (inputLine.charAt(0) == '/') {
                    this.parseClientCommand(inputLine);
                } else {
                    this.server.onNewMessage(this, inputLine);
                }
            } catch (SocketTimeoutException e) {
                // timed out, client is there, has nothing to say
            } catch (IOException e) {
                this.server.removeClient(this);
            }
        }

        try {
            socketBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseClientCommand(String clientMessage) {;
        String clientName;

        if (clientMessage != null && clientMessage.substring(0, 2).equals("/n")) {
            clientName = clientMessage.substring(3);
            this.setUName(clientName);
        }
    }
}
