package fu.alp4;

import java.util.Arrays;

public class RealTimeScheduler {

    private Task[] managedTasks;
    private int simulationTime;
    private Task latestExecuted;

    public RealTimeScheduler(Task[] tasks, int st) {
        this.managedTasks = tasks;
        this.simulationTime = st;
    }

    public void simulateRMS() {
        this.setPrioritiesRMS();

        System.out.println("\nSimulation starting!\n\n");

        while(this.simulationTime > 0) {

            boolean taskAlreadyExecutedInCurrentLoop = false;

            // managed tasks already sorted by priority
            for(Task task : this.managedTasks) {
                if (task.getReady() && !taskAlreadyExecutedInCurrentLoop) {

                    if (this.latestExecuted == null) {
                        this.latestExecuted = task;
                    } else if (this.latestExecuted != task) {
                        System.out.printf("Task with #id %s was interrupted in favour of task with #id %s\n\n",
                                this.latestExecuted.getId(), task.getId());
                        this.latestExecuted = task;
                    }

                    task.execute();
                    taskAlreadyExecutedInCurrentLoop = true;

                } else {
                    task.keepBlocked();
                }
            }

            this.simulationTime--;
        }

    }

    private void setPrioritiesRMS() {
        // Sort managedTask by period
        Arrays.sort(this.managedTasks, (a, b) -> a.getPeriod() < b.getPeriod() ? -1 :
            a.getPeriod() == b.getPeriod() ? 0 : 1);

        for (int i = 0; i < this.managedTasks.length; i++) {
            Task currentTask = this.managedTasks[i];
            currentTask.setPriority(this.managedTasks.length - i);
            System.out.printf("Task id: %s; Period: %s; Priority: %s\n",
                    currentTask.getId(), currentTask.getPeriod(), currentTask.getPriority());
        }
    }

}
