package engine;

import graphics.MainGUI;

public class MainEngine {
    private MainGUI gui;

    public MainEngine() {}


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