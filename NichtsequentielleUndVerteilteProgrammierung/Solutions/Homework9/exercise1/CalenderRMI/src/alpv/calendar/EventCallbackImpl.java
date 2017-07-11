package alpv.calendar;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EventCallbackImpl extends UnicastRemoteObject implements  EventCallback {

    public String uName;

    public EventCallbackImpl(String name) throws RemoteException {
        this.uName = name;
    }

    @Override
    public void call(Event e) throws RemoteException {
        System.out.printf("%s event started! Whohoo; In callback for user %s \n", e.getName(), this.uName);
    }
}
