package util;

import java.awt.Color;

public class GanttChartBlocks {
    private int startTime;
    private int endTime;
    private String processID;
    private Color color;

    public GanttChartBlocks(String processID, Color color, int startTime, int endTime) {
        this.processID = processID;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
