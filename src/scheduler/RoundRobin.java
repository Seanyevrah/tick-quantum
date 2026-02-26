package scheduler;

import model.Process;
import util.GanttChartBlocks;
import java.util.ArrayList;

public class RoundRobin {
    private ArrayList<GanttChartBlocks> ganttChartBlocks;
    private int quantumTime;

    public RoundRobin() {
        ganttChartBlocks = new ArrayList<>();
    }

    public ArrayList<Process> run(ArrayList<Process> process) {
        ArrayList<Process> processResult = new ArrayList<>();

        return processResult;
    }


    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public ArrayList<GanttChartBlocks> getGanttChartBlocks() {
        return ganttChartBlocks;
    }

    public void setGanttChartBlocks(ArrayList<GanttChartBlocks> ganttChartBlocks) {
        this.ganttChartBlocks = ganttChartBlocks;
    }

    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
    }
}
