package graphics;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.MainEngine;
import graphics.Branding;

public class MainGUI {
    public Branding branding;
    private MainEngine engine;
    private JFrame mainFrame;
    private JPanel mainPanel;

    private SimulatorMain simulatorMain;
    private SimulatorOutput simulatorOutput;

    public MainGUI(MainEngine engine) {
        this.engine = engine;
        this.branding = new Branding();

        initializeMainFrame();
        initializeMainPanel();

        mainFrame.setVisible(true);
    }

    public void initializeMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setSize(new Dimension(1240,720));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle("QuantumQueue");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
    }

    public void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setBackground(branding.dark);
        mainPanel.setLayout(new CardLayout());
        
        simulatorMain = new SimulatorMain(engine, branding, mainPanel);
        simulatorOutput = new SimulatorOutput(engine, branding, mainPanel);
        
        mainPanel.add(simulatorMain, "SimulatorMain");
        mainPanel.add(simulatorOutput, "SimulatorOutput");
        mainFrame.add(mainPanel);
    }


    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public SimulatorMain getSimulatorMain() {
        return simulatorMain;
    }
}