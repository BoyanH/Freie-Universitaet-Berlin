package network;

import fx.ClientGUI;
import javafx.scene.paint.Color;

public class ClientTCP extends AbstractChatClient {

    private ConnectionThreadTCP connectionThread;
    private boolean clientConnected;

    public ClientTCP(ClientGUI gui) {
        super(gui);
    }

    @Override
    public void sendChatMessage(String msg) {

    }

    @Override
    public void connect(String address, String port) {
        if (this.clientConnected)
            return;

        boolean connected = true;

        try {
            this.connectionThread = new ConnectionThreadTCP(address, port, this);
            this.connectionThread.start();
            this.gui.setSymbolColor(Color.GREEN);
        } catch (NumberFormatException e) {
            this.gui.pushChatMessage("System: Port must be a valid integer!");
            connected = false;
        }

        this.clientConnected = connected;
    }

    @Override
    public void disconnect() {

    }

    public void setConnected(boolean connected) {
        this.clientConnected = connected;
        this.gui.setSymbolColor(this.clientConnected ? Color.GREEN : Color.RED);
    }

    @Override
    public void terminate() {
        this.connectionThread.terminate();
    }

    public void onServerMessage(String message) {
        System.out.println(message);
    }

    public void connectToChat() {

    }
}
