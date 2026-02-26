package scheduler;

import model.Process;
import util.GanttChartBlocks;
import java.util.ArrayList;

public class PriorityNonPreemptive {
    private ArrayList<GanttChartBlocks> ganttChartBlocks;

    public PriorityNonPreemptive() {
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
}
