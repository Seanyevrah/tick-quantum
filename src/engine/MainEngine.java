package engine;

import java.util.ArrayList;

import graphics.*;
import scheduler.FCFS;
import scheduler.PriorityNonPreemptive;
import scheduler.PriorityPreemptive;
import scheduler.RoundRobin;
import scheduler.SJF;
import scheduler.SRTF;
import model.Process;

public class MainEngine {
    private MainGUI gui;

    private FCFS fcfs;
    private SJF sjf;
    private SRTF srtf;
    private RoundRobin roundRobin;
    private PriorityPreemptive priorityPreemptive;
    private PriorityNonPreemptive priorityNonPreemptive;

    public MainEngine() {
        initializeAlgorithms();
    }

    public void initializeAlgorithms() {
        fcfs = new FCFS();
        sjf = new SJF();
        srtf = new SRTF();
        roundRobin = new RoundRobin();
        priorityPreemptive = new PriorityPreemptive();
        priorityNonPreemptive = new PriorityNonPreemptive();
    }

    public void runSimulation(ArrayList<Process> processes, String algorithm, int quantum) {
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to simulate.");
            return;
        }

        System.out.println("Running algorithm: " + algorithm);

        switch (algorithm) {
            case "FCFS (First Come First Serve)":
                fcfs.run(processes);
                break;

            case "SJF (Shortest Job First)":
                sjf.run(processes);
                break;

            case "SRTF (Shortest Remaining Time First)":
                srtf.run(processes);
                break;

            case "Round Robin":
                roundRobin.setQuantumTime(quantum);
                roundRobin.run(processes);
                break;

            case "Priority (Preemptive)":
                priorityPreemptive.run(processes);
                break;

            case "Priority (Non-Preemptive)":
                priorityNonPreemptive.run(processes);
                break;

            default:
                System.out.println("Unknown algorithm: " + algorithm);
                break;
        }

        // After simulation, you could update the GUI
        // e.g., gui.getSimulatorMain().updateGanttChart(result);
    }


    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public void setGUI(MainGUI gui) {
        this.gui = gui;
    }
    public MainGUI getGUI() {
        return gui;
    }
}