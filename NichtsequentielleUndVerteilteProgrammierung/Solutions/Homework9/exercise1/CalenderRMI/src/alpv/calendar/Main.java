package alpv.calendar;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
	private static final String	USAGE	= String.format("usage: java -jar UB%%X_%%NAMEN server PORT%n" +
														"         (to start a server)%n" +
														"or:    java -jar UB%%X_%%NAMEN client SERVERIPADDRESS SERVERPORT%n" +
														"         (to start a client)");

	private static CalendarServer calendarServerStub;

	/**
	 * Starts a server/client according to the given arguments. 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int i = 0;

			if(args[i].equals("server")) {
				try {

					/**
					 * Set the java's rmi server's hostname to 127.0.0.1 AKA localhost, we don't want a global server
					 */
					System.setProperty("java.rmi.server.hostname","127.0.0.1");

					// Bind the remote object's stub in the registry
					Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[1]));

					/**
					 * It's important to always keep a reference to the class registered in the LocateRegistry,
					 * otherwise it could get garbage-collected before it was successfully registered
					 */
					CalendarServerImpl calendarServerImpl = new CalendarServerImpl();
					calendarServerStub = (CalendarServer) UnicastRemoteObject.exportObject(calendarServerImpl, Integer.parseInt(args[1]));
					registry.bind("CalendarServer", calendarServerStub);

					System.err.println("Server ready");
				} catch (Exception e) {
					System.err.println("Server exception: " + e.toString());
					e.printStackTrace();
				}
			}
			else if(args[i].equals("client")) {
				try {
					Registry registry = LocateRegistry.getRegistry(args[1], Integer.parseInt(args[2])
					);
					CalendarServer stub = (CalendarServer) registry.lookup("CalendarServer");
					new ClientInputThread(stub).start();

				} catch (Exception e) {
					System.err.println("Client exception: " + e.toString());
					e.printStackTrace();
				}
			}
			else
				throw new IllegalArgumentException();
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.err.println(USAGE);
		}
		catch(NumberFormatException e) {
			System.err.println(USAGE);
		}
		catch(IllegalArgumentException e) {
			System.err.println(USAGE);
		}
	}
}