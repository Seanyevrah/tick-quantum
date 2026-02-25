package scheduler;

import model.Process;
import java.util.ArrayList;

public class RoundRobin {
    private int quantum;

    public RoundRobin() {}

    public ArrayList<Process> run(ArrayList<Process> process) {
        ArrayList<Process> processResult = new ArrayList<>();

        return processResult;
    }

    public void setQuantumTime(int quantum) {
        this.quantum = quantum;
    }

    public int getQuantumTime() {
        return quantum;
    }
}
