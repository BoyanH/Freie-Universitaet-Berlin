package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;

public class ConnectionAccepterTCP extends Thread {

    private ServerSocket serverSocket;
    private ServerTCP server;
    private boolean running = true;
    private LinkedList<Socket> connections;

    public ConnectionAccepterTCP(ServerSocket socket, ServerTCP server) {
        this.serverSocket = socket;
        this.server = server;
        this.connections = new LinkedList<>();
    }

    @Override
    public void run() {
        this.waitForConections();
    }

    public void waitForConections() {
        String inputLine;
        BufferedReader socketBuffer = null;

        while (this.running) {
            try {
                Socket connection = this.serverSocket.accept(); // wait for client connections and accept them

                if (!this.connections.contains(connection)) {
                    this.server.addNewClient(connection);
                    this.connections.add(connection);
                }

            } catch (IOException e) {
                // socket setTimeout exception, whatever
            }
        }
    }

    public void terminate() {
        this.running = false;
    }
}
