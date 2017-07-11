package alpv.calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

public class ClientInputThread extends Thread {

    private CalendarServer stub;
    private BufferedReader readBuffer;
    private EventCallback ec;
    String userName;

    public ClientInputThread(CalendarServer stub) {
        this.stub = stub;
        this.readBuffer = new BufferedReader(new InputStreamReader(System.in));;
    }

    @Override
    public void run () {

        while(true) {

            try {
                String newLine = this.readBuffer.readLine();
                this.parseCommand(newLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseCommand(String line) {
        String eventName;
        Long milliseconds;

        try {
            if (this.userName == null && !line.equals("setName")) {
                System.out.println("Cannot execute any commands without setting your name first. Execute \"setName\" first ");
            }

            switch (line) {
                case "newEvent":
                    System.out.print("Enter event name: ");
                    eventName = this.readBuffer.readLine();
                    System.out.print("After how many seconds is the event happening: ");
                    milliseconds = Long.parseLong(this.readBuffer.readLine()) * 1000;
                    String[] user = {this.userName};

                    stub.addEvent(new Event(eventName, user, new Date(new Date().getTime() + milliseconds)));
                    break;
                case "setName":
                    System.out.print("Enter your name: ");
                    this.userName = this.readBuffer.readLine();

                    if (this.ec != null) {
                        stub.UnregisterCallback(this.ec);
                        this.ec = new EventCallbackImpl(this.userName);
                        stub.RegisterCallback(ec, this.userName);
                    }

                    break;
                case "addUserToEvent":
                    List<Event> eventsList = stub.listEvents(this.userName);
                    for (Event e : eventsList) {
                        System.out.printf("#%s \"%s\" starting on %s\n", e.getId(), e.getName(), e.getBegin());
                    }
                    System.out.print("Which event id? : ");
                    long eventId = Long.parseLong(this.readBuffer.readLine());
                    System.out.print("Which user? : ");
                    String newUser = this.readBuffer.readLine();

                    for (Event e : eventsList) {
                        if (e.getId() == eventId) {
                            Event newEvent = new Event(e.getName(), e.getUser(), e.getBegin());
                            String[] newUsers = new String[newEvent.getUser().length];

                            for (int i = 0; i < newEvent.getUser().length; i++) {
                                newUsers[i] = newEvent.getUser()[i];
                            }
                            newUsers[newUsers.length-1] = newUser;
                            stub.updateEvent(newEvent.getId(), newEvent);
                            break;
                        }
                    }

                    break;
                case "subscribeForEvents":
                    this.ec = new EventCallbackImpl(this.userName);
                    stub.RegisterCallback(ec, this.userName);
                    break;
                case "unsubscribe":
                    stub.UnregisterCallback(this.ec);
                    this.ec = null;
                    break;
                case "removeEvent":
                    List<Event> eventList = stub.listEvents(this.userName);
                    for (Event e : eventList) {
                        System.out.printf("#%s \"%s\" starting on %s\n", e.getId(), e.getName(), e.getBegin());
                    }
                    System.out.print("Which event id? : ");
                    long eId = Long.parseLong(this.readBuffer.readLine());

                    stub.removeEvent(eId);
                    break;
                case "nextEvent":
                    Event next = stub.getNextEvent(this.userName);
                    System.out.printf("Next event awaited, event name: %s\n", next.getName());
                default:
                    System.out.println("Command not recognized. Check ClientInputThread.java for more info");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
