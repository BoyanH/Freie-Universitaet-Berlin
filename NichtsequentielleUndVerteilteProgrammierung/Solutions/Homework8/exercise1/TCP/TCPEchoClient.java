import java.net.*;
import java.io.*;

public class TCPEchoClient {

    public static void main(String[] args) {
	
    	try {

			int port = 1337;
			String ipAddress = "localhost";
			Socket socket = new Socket(ipAddress, port); // create a client socket and bind it to our ServerSocket

			// Define all required IO streams and variables required for reading/writing from/to server
			DataOutputStream out;
			BufferedReader stdin;
			BufferedReader serverIn;
			String currentLine;
			String serverResponseLine;

			System.out.println("TCP client connected to localhost:1337"); // after new Socket() call has executed, client should be connected

			// initialize IO buffers
			out = new DataOutputStream(socket.getOutputStream());
			stdin = new BufferedReader(new InputStreamReader(System.in));
			serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			System.out.print("Message to send to server: ");
			currentLine = stdin.readLine(); // read from console (stdin)
			out.writeBytes(currentLine + '\n'); // write to server
			serverResponseLine = serverIn.readLine(); // read response from server
			System.out.printf("Server response: %s\n", serverResponseLine);

			// close all buffers and socket connection
			out.close();
			stdin.close();
			serverIn.close();
			socket.close();
			


		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
