package network;

import fx.ServerGUI;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class ServerTCP extends AbstractChatServer {

    private boolean running = false;
    private ServerSocket serverSocket;
    private ConnectionAccepterTCP connectionAccepter;
    private List<ClientCommunicationThread> clientMessagers;

    public ServerTCP(ServerGUI gui) {
        super(gui);
        this.clientMessagers = new LinkedList<ClientCommunicationThread>();
    }

    @Override
    public void receiveConsoleCommand(String command, String msg) {
        switch(command) {
            case "/exclude":
                this.removeClientByName(msg);
                break;
            case "/mute":
                String[] words = msg.split(" ");
                if (words.length < 2) {
                    this.gui.pushConsoleMessage("Invalid number of arguments for command /mute");
                    return;
                }
                this.muteClientByNameForSeconds(words[0], words[1]);
                break;
            default:
                this.gui.pushConsoleMessage(String.format("Unrecognized console command %s", command));
        }

    }

    @Override
    public void start(String port) {
        SocketAddress address;
        int portNumber;

        if (this.running) {
            return;
        }

        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            System.out.println("Port must be an integer!");
            return;
        }
        address = new InetSocketAddress("localhost", portNumber);
        try {
            this.serverSocket = new ServerSocket(); // create unbound server socket
            this.serverSocket.bind(address);
            this.running = true;
            this.gui.setSymbolColor(Color.GREEN);
            this.connectionAccepter = new ConnectionAccepterTCP(this.serverSocket, this);
            this.connectionAccepter.start();

        } catch (IOException e) {
            e.printStackTrace();
            this.terminate();
            return;
        }

    }

    @Override
    public void stop() {
        this.running = false;
        this.gui.setSymbolColor(Color.RED);

        if (this.connectionAccepter != null) {
            this.connectionAccepter.terminate();
        }

        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.removeAllClients();

    }

    @Override
    public void terminate() {
        this.stop();

    }

    public void addNewClient(Socket connection) {
        ClientCommunicationThread newClientThread = new ClientCommunicationThread(connection, this);
        newClientThread.start();
        this.clientMessagers.add(newClientThread);
    }

    public void removeClient(ClientCommunicationThread client) {
        this.clientMessagers.remove(client);
        this.gui.removeClient(client.getClientId());
        client.terminate();
    }

    public void removeAllClients() {
        while(!this.clientMessagers.isEmpty()) {
            ClientCommunicationThread crnt = this.clientMessagers.remove(0);
            this.gui.removeClient(crnt.getClientId());
            crnt.terminate();
        }
    }

    public void onNewMessage(ClientCommunicationThread clientMessager, String message) {
        if (clientMessager.mutedBefore > System.currentTimeMillis()) {
            clientMessager.getWriter().println("You are muted!");
            clientMessager.getWriter().flush();
            return; // muted
        }

        for (ClientCommunicationThread client : this.clientMessagers) {
            client.getWriter().printf("%s: %s\n", clientMessager.getUName(), message);
            client.getWriter().flush();
        }
    }

    public void addClientToGUI(int id, String uname) {
        this.gui.addClient(id, uname);
    }

    public void clientRenamed(int id, String oldName, String newName) {
        this.gui.removeClient(id);
        this.gui.addClient(id, newName);

        // better send not as direct message but as command, wanted to
        // refresh all mesages, but the gui windows is not flushable ;/
        for (ClientCommunicationThread client : this.clientMessagers) {
            client.getWriter().printf("/r %s %s\n", oldName, newName);
            client.getWriter().flush();
        }
    }

    public void sendPrivateMessage(ClientCommunicationThread fromClient, String toClientName, String message) {
        fromClient.getWriter().printf("*private* %s: %s\n", fromClient.getUName(), message);
        fromClient.getWriter().flush();
        this.getClientByName(toClientName).getWriter().printf("*private* %s: %s\n", fromClient.getUName(), message);
        this.getClientByName(toClientName).getWriter().flush();
    }

    private ClientCommunicationThread getClientByName(String name) {
        for (ClientCommunicationThread client : this.clientMessagers) {
            if (client.getUName().equals(name)) {
                return client;
            }
        }

        return  null;
    }

    private void removeClientByName(String name) {
        this.removeClient(this.getClientByName(name));
    }

    private void muteClientByNameForSeconds(String name, String seconds) {
        int milliseconds;

        try {
            milliseconds = Integer.parseInt(seconds) * 1000;
            this.getClientByName(name).mutedBefore = System.currentTimeMillis() + milliseconds;
        } catch (NumberFormatException e) {
            this.gui.pushConsoleMessage("Invalid argument for seconds to mute parsed!");
        }
    }

}
