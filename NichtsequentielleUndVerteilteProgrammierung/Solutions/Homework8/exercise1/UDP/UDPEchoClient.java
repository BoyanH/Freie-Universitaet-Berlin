import java.net.*;
import java.io.*;

public class UDPEchoClient {

    public static void main(String[] args) {
	
		// not as it should be done, I hope enough for this exercise. Usually each error should be handled individually
    	try {

			int port = 1337;
			InetAddress laddr = InetAddress.getLocalHost();
			byte[] responseDataBuffer = new byte[256];
			byte[] sendBuffer;
			DatagramSocket udSocket = new DatagramSocket(); // create new unbound datagramm socket

			DatagramPacket udClientPacket;
			DatagramPacket udResponsePacket = new DatagramPacket(responseDataBuffer, responseDataBuffer.length);
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

			// read from stdin
			System.out.print("Enter message to send to server: ");
			sendBuffer = stdin.readLine().getBytes();

			// create a DatagramPacket from the read message and send to server
			udClientPacket = new DatagramPacket(sendBuffer, sendBuffer.length, laddr, port);
			udSocket.send(udClientPacket);

			// read server response
			udSocket.receive(udResponsePacket);
			System.out.printf("Server response: %s\n", new String(udResponsePacket.getData()));

			udSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
