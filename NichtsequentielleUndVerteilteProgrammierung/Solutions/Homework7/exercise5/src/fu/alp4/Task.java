package fu.alp4;

public class Task {

    static int idCounter;

    private int release;
    private int execution;
    private int executionLeft;
    private int timePassed;
    private int deadline;
    private int deadlineAfter;
    private int period;

    private int priority;
    private int id;

    /**
     * A Task manages itself in terms of setting its own deadlineAfter and executionLeft.
     * The idea is to let the scheduler-class only implement the scheduler logic, as if it was
     * gathering the data from the Process-Control-Block
     */

    public Task(int r, int e, int d, int p) {
        this.release = r;
        this.execution = e;
        this.executionLeft = release == 0 ? e : 0;
        this.deadline = d;
        this.deadlineAfter = d;
        this.period = p;
        this.timePassed = 0;
        this.setPriority(idCounter); // initially set priority = highest possible, will be changed later on

        /**
         * Priority can be between 1/n and n/n for n tasks in the scheduler.
         */

        this.id = ++idCounter;
    }


    public int getPeriod() {
        return period;
    }

    public boolean getReady() {
        return this.executionLeft > 0;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return this.id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void execute() {
        this.executionLeft--;
        this.passTime();
        System.out.printf("Task with id %s and priority %s executed. \n\t Execution left: %s; Deadline after: %s ;Time passed: %s\n\n",
                this.id, this.getPriority(), this.executionLeft, this.deadlineAfter, this.timePassed);
    }

    public void keepBlocked() {
        this.passTime();
    }

    private void passTime() {
        this.timePassed++;
        this.deadlineAfter--;

        if (this.executionLeft > 0 && this.deadlineAfter == 0) {
            System.out.printf("Task with id %s and priority %s missed it's deadline!\n\n", this.id, this.getPriority());
        } else if (this.timePassed % this.period == 0 || this.timePassed == this.release) {
            this.deadlineAfter = this.deadline;
            this.executionLeft = this.execution;
        }
    }
}
