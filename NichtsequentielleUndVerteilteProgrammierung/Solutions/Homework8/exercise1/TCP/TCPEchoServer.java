import java.net.*;
import java.io.*;

public class TCPEchoServer {

    public static void main(String[] args) {
	
		// not as it should be done, I hope enough for this exercise. Usually each error should be handled individually
    	try {

			int port = 1337;
			String ipAddress = "localhost";
			SocketAddress address = new InetSocketAddress(ipAddress, port);
			ServerSocket socket = new ServerSocket(); // create unbound server socket
			socket.bind(address); // bind it to the given address (ip + port)

			System.out.println("TCP server running on localhost:1337");

			// keep accepting client connections
			while (true) {
				Socket connection = socket.accept(); // wait for client connections and accept them
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String currentLine = br.readLine();

				System.out.print("Received message \"");
				// read each line from the client and write it back
				while (currentLine != null) {
					System.out.print(currentLine);
					out.writeBytes(currentLine + '\n');
					currentLine = br.readLine();
				}
				System.out.println("\" and wrote it back to client!");

				// close all streams and connections when finished
				out.close();
				br.close();
				connection.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
