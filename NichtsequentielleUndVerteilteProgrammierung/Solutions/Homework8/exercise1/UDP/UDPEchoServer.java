import java.net.*;
import java.io.*;

public class UDPEchoServer {

    public static void main(String[] args) {
	
		// not as it should be done, I hope enough for this exercise. Usually each error should be handled individually
    	try {

			int port = 1337;
			InetAddress laddr = InetAddress.getLocalHost();
			byte[] inDataBuffer = new byte[256];
			DatagramSocket udSocket = new DatagramSocket(1337, laddr);

			DatagramPacket udClientPacket;
			DatagramPacket udResponsePacket;
			int clientPort;
			InetAddress clientAddress;
			String clientMessage;

			System.out.println("Started UDP server on localhost:1337");

			// keep accepting client connections
			while (true) {
				udClientPacket = new DatagramPacket(inDataBuffer, inDataBuffer.length);
				udSocket.receive(udClientPacket); // wait to receive client user-datagramm-packet
				clientMessage = new String(udClientPacket.getData()); // get String from send byte[]
				clientAddress = udClientPacket.getAddress();
				clientPort = udClientPacket.getPort();

				udResponsePacket = new DatagramPacket(inDataBuffer, inDataBuffer.length, clientAddress, clientPort);
				udSocket.send(udResponsePacket);

				System.out.printf("Received message \"%s\" and wrote it back to client.", clientMessage);

				// nothing to close with UDP (except for the server socket, in our case NEVER! ^^)
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
