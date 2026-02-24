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
        
        simulatorMain = new SimulatorMain(engine, branding);
        // scrnStartScreen = new ScreenStart(engine, branding);
        // scrnDesktop = new ScreenDesktop(engine, branding);
        // scrnGameOver = new ScreenGameOver(engine, branding);

        mainPanel.add(simulatorMain, "SimulatorMain");
        // mainPanel.add(scrnStartScreen, "ScreenStart");
        // mainPanel.add(scrnDesktop, "ScreenDesktop");
        // mainPanel.add(scrnGameOver, "ScreenGameOver");
        mainFrame.add(mainPanel);
    }
}