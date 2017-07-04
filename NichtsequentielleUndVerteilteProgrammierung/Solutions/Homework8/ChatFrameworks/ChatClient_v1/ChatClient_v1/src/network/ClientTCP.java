package network;

import fx.ClientGUI;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTCP extends AbstractChatClient {

    private MessageListenerTCP messageListenerTCP;
    private boolean clientConnected;
    private Socket serverSocket;
    private PrintWriter socketWriter;

    public ClientTCP(ClientGUI gui) {
        super(gui);
    }

    @Override
    public void sendChatMessage(String msg) {
        if (this.socketWriter != null) {
            this.socketWriter.println(msg);
        }
    }

    @Override
    public void connect(String address, String port) {
        if (this.clientConnected) {
            return;
        }

        boolean connected = true;
        int portNumber;

        try {

            portNumber = Integer.parseInt(port);
            this.serverSocket = new Socket(address, portNumber);
            this.socketWriter = new PrintWriter(this.serverSocket.getOutputStream(), true); // auto-flush output stream
            this.connectToChat();

            this.messageListenerTCP = new MessageListenerTCP(this.serverSocket, this);
            this.messageListenerTCP.start();


        } catch (NumberFormatException e) {
            this.gui.pushChatMessage("System: Port must be a valid integer!");
            connected = false;
        } catch (IOException e) {
            this.terminate();
        }

        this.clientConnected = connected;
    }

    @Override
    public void disconnect() {
        this.terminate();
    }

    @Override
    public void terminate() {
        this.setConnected(false);

        if (this.messageListenerTCP != null) {
            this.messageListenerTCP.terminate();
        }
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
            if (this.socketWriter != null) {
                this.socketWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUName(String name) {
        super.setUName(name);

        // notify server
        this.socketWriter.printf("/n %s\n", this.getUName());
    }

    public void setConnected(boolean connected) {
        this.clientConnected = connected;
        this.gui.setSymbolColor(this.clientConnected ? Color.GREEN : Color.RED);
    }

    public void onServerMessage(String message) {
        if (message.charAt(0) == '/') {
            this.handleServerCommand(message);
        } else {
            this.gui.pushChatMessage(message);
        }
    }

    public void connectToChat() {
        if (this.socketWriter != null) {
            this.socketWriter.printf("/n %s\n", this.getUName());
            this.socketWriter.flush();
            this.setConnected(true);
        }
    }

    private void handleServerCommand(String command) {
        switch(command.substring(0,2)) {
            case "/r":
                int firstSpaceIdx = command.indexOf(' ');
                int secondSpaceIdx = command.indexOf(' ', firstSpaceIdx + 1);
                this.gui.pushChatMessage(
                        String.format("%s renamed to %s",
                                command.substring(firstSpaceIdx + 1, secondSpaceIdx),
                                command.substring(secondSpaceIdx + 1)
                        )
                );
                break;
            default:
                System.out.println("Unrecognized server command!");
        }
    }
}
