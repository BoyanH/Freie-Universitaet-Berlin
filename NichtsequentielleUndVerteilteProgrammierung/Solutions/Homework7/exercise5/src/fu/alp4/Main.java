package fu.alp4;

public class Main {

    public static void main(String[] args) {

        Task[] tasksForSimulation = {
                new Task(0,2,7,7),
                new Task(0,3,9,9),
                new Task(0,1,11,11),
                new Task(0,1,6,6)
        };
        RealTimeScheduler scheduler = new RealTimeScheduler(tasksForSimulation, 22);
        scheduler.simulateRMS();
    }
}
