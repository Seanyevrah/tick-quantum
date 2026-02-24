package graphics;

import scheduler.*;
import util.*;

public class SimulatorMain {
    private FCFS fcfs;
    private SJF sjf;
    private SRTF srtf;
    private RoundRobin roundRobin;
    private PriorityPreemptive priorityPreemptive;
    private PriorityNonPreemptive priorityNonPreemptive;

    public SimulatorMain() {
        initializeSchedulingAlgorithms();
    }

    public void initializeSchedulingAlgorithms() {
        fcfs = new FCFS();
        sjf = new SJF();
        srtf = new SRTF();
        roundRobin = new RoundRobin();
        priorityPreemptive = new PriorityPreemptive();
        priorityNonPreemptive = new PriorityNonPreemptive();
    }
}
