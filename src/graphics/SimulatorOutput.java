package graphics;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import engine.MainEngine;
import util.GanttChartBlocks;
import model.Process;

public class SimulatorOutput extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;

    private JPanel mainPanel;
    private JPanel topPanel, bottomPanel;

    // Top panel components
    private JLabel timerLabel;
    private JLabel algorithmNameLabel;
    private JPanel ganttChartPanel;

    private JScrollPane ganttScrollPane;
    private JScrollPane tableScrollPane;

    // Animation
    private javax.swing.Timer simulationTimer;
    private int currentTime = 0;
    private int totalTime = 0;
    private JPanel liveGanttChart; // reference so the timer can repaint it

    // Bottom panel components
    private JButton goBackButton;
    private JPanel tableHeaderPanel; // fixed header row, outside the scroll pane

    // Colors matching the dark theme — kept for Gantt chart backward-compat
    private Color P1_COLOR = new Color(72, 187, 120);
    private Color P2_COLOR = new Color(176, 80, 100);
    private Color P3_COLOR = new Color(110, 90, 190);

    public SimulatorOutput(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
        initializePanels();
    }

    public void initializeMainPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(branding.dark);

        int mw = 20, mh = 20;

        wrapper.add(blankPanel(branding.dark, 0, mh), BorderLayout.NORTH);
        wrapper.add(blankPanel(branding.dark, 0, mh), BorderLayout.SOUTH);
        wrapper.add(blankPanel(branding.dark, mw, 0), BorderLayout.WEST);
        wrapper.add(blankPanel(branding.dark, mw, 0), BorderLayout.EAST);

        mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(branding.dark);
        wrapper.add(mainPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }
    
    public void initializePanels() {
        initializeTopPanel();
        initializeBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
    }
    
    public void initializeTopPanel() {
        topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(0, 200));
        topPanel.setBorder(new EmptyBorder(14, 18, 14, 18));

        // --- Timer (top-left) ---
        timerLabel = new JLabel("Timer: 0:06");
        timerLabel.setFont(branding.jetBrainsBMedium);
        timerLabel.setForeground(branding.light);

        // --- Algorithm name (top-right) ---
        algorithmNameLabel = new JLabel("First Come First Serve");
        algorithmNameLabel.setFont(branding.jetBrainsBMedium);
        algorithmNameLabel.setForeground(branding.light);
        algorithmNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Header row: timer left, algorithm right
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.add(timerLabel, BorderLayout.WEST);
        headerRow.add(algorithmNameLabel, BorderLayout.EAST);

        // --- Gantt Chart (centered) ---
        JPanel chartContent = createGanttChart();
        ganttScrollPane = new JScrollPane(chartContent,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ganttScrollPane.setBorder(null);
        ganttScrollPane.setOpaque(false);
        ganttScrollPane.getViewport().setOpaque(false);

        ganttChartPanel = new JPanel(new BorderLayout());
        ganttChartPanel.setOpaque(false);
        ganttChartPanel.add(ganttScrollPane, BorderLayout.CENTER);

        topPanel.add(headerRow, BorderLayout.NORTH);
        topPanel.add(ganttChartPanel, BorderLayout.CENTER);
    }
    
    public JPanel createGanttChart() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel chart = new JPanel() {
            private final String[] labels  = { "P1", "P2", "P3" };
            private final Color[] colors  = { P1_COLOR, P2_COLOR, P3_COLOR };
            private final int[] ticks = { 0, 2, 4, 6 };
            private final int BAR_H = 38;
            private final int BAR_W = 100;
            private final int TICK_Y = BAR_H + 6;
            private final int PAD_LEFT = 10;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int totalBars = labels.length;
                int totalW = totalBars * BAR_W;
                int startX = Math.max((getWidth() - totalW) / 2, PAD_LEFT);

                // Draw colored bars
                for (int i = 0; i < totalBars; i++) {
                    int x = startX + i * BAR_W;
                    g2.setColor(colors[i]);
                    g2.fillRect(x, 0, BAR_W, BAR_H);

                    // Bar label
                    g2.setColor(Color.WHITE);
                    g2.setFont(branding.jetBrainsBMedium);
                    FontMetrics fm = g2.getFontMetrics();
                    int lx = x + (BAR_W - fm.stringWidth(labels[i])) / 2;
                    int ly = (BAR_H + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(labels[i], lx, ly);
                }

                // Draw tick marks and numbers below bars
                g2.setFont(branding.jetBrainsBSmall);
                g2.setColor(branding.light);
                FontMetrics fm = g2.getFontMetrics();
                for (int i = 0; i <= totalBars; i++) {
                    int x = startX + i * BAR_W;
                    String t = String.valueOf(ticks[i]);
                    int tx = x - fm.stringWidth(t) / 2;
                    g2.drawString(t, tx, TICK_Y + fm.getAscent());
                }

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(320 + PAD_LEFT * 2, 60);
            }
        };
        chart.setOpaque(false);

        wrapper.add(chart);
        return wrapper;
    }
    
    public void initializeBottomPanel() {
        bottomPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
            }
        };
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 24, 24, 24));

        // --- Go Back button ---
        goBackButton = createGoBackButton("Go Back");

        JPanel goBackRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        goBackRow.setOpaque(false);
        goBackRow.add(goBackButton);

        // --- Process results table ---
        JPanel tableContent = createProcessTable();

        bottomPanel.add(goBackRow, BorderLayout.NORTH);
        bottomPanel.add(tableContent, BorderLayout.CENTER);
    }

    public JPanel createProcessTable() {
        // Outer wrapper holds header (fixed) + scrollable data rows
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 0, 0, 0));

        String[] headers = {
            "Process ID",
            "Burst Time",
            "Arrival Time",
            "Priority No.",
            "Waiting\nTime",
            "Turnaround\nTime",
            "Average\nWaiting\nTime",
            "Average\nTurnaround\nTime"
        };

        // Header row — saved as field so updateProcessTable can rebuild it
        // without touching the scroll pane
        tableHeaderPanel = new JPanel(new GridLayout(1, 8, 8, 0));
        tableHeaderPanel.setOpaque(false);
        for (String h : headers) {
            tableHeaderPanel.add(createMultiLineHeader(h));
        }

        // Data rows — only these scroll
        Object[][] data = {
            { "P1", "2", "0", "N/A", "0", "2", "0", "2" },
            { "P2", "2", "2", "N/A", "0", "2", "0", "2" },
            { "P3", "2", "4", "N/A", "0", "2", "0", "2" },
        };
        Color[] placeholderColors = { P1_COLOR, P2_COLOR, P3_COLOR };

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setOpaque(false);

        for (int i = 0; i < data.length; i++) {
            dataPanel.add(Box.createVerticalStrut(8));
            dataPanel.add(createDataRow(data[i], placeholderColors[i]));
        }

        // tableScrollPane wraps ONLY the data rows — never the header
        tableScrollPane = new JScrollPane(dataPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setBorder(null);
        tableScrollPane.setOpaque(false);
        tableScrollPane.getViewport().setOpaque(false);

        wrapper.add(tableHeaderPanel, BorderLayout.NORTH);
        wrapper.add(tableScrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    public JPanel createDataRow(Object[] values, Color idColor) {
        JPanel row = new JPanel(new GridLayout(1, values.length, 8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        for (int i = 0; i < values.length; i++) {
            String text = values[i].toString();
            JLabel cell = new JLabel(text, SwingConstants.CENTER);
            cell.setFont(branding.jetBrainsBMedium);

            // First column (Process ID): fill with the process's own color
            if (i == 0 && idColor != null) {
                cell.setOpaque(true);
                cell.setBackground(idColor);
                cell.setForeground(Color.WHITE);
            } else {
                cell.setOpaque(false);
                cell.setForeground(branding.light);
            }

            cell.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(branding.light, 1, true),
                new EmptyBorder(6, 4, 6, 4)
            ));
            row.add(cell);
        }
        return row;
    }

    public JPanel createMultiLineHeader(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        String[] lines = text.split("\n");
        JPanel linesPanel = new JPanel();
        linesPanel.setLayout(new BoxLayout(linesPanel, BoxLayout.Y_AXIS));
        linesPanel.setOpaque(false);

        for (String line : lines) {
            JLabel lbl = new JLabel(line, SwingConstants.CENTER);
            lbl.setFont(branding.jetBrainsBMedium);
            lbl.setForeground(branding.light);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            linesPanel.add(lbl);
        }

        panel.add(linesPanel, gbc);
        return panel;
    }

    public JButton createGoBackButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(branding.jetBrainsBMedium);
        btn.setForeground(branding.light);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 48));

        btn.addActionListener(e -> {
            System.out.println("@ Go Back to SimulatorMain");
            if (simulationTimer != null && simulationTimer.isRunning()) {
                simulationTimer.stop();
            }
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "SimulatorMain");
        });

        return btn;
    }

    public JPanel blankPanel(Color bg, int w, int h) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        if (w > 0) p.setPreferredSize(new Dimension(w, 0));
        if (h > 0) p.setPreferredSize(new Dimension(0, h));
        return p;
    }

    public void loadSimulationResults() {
        ArrayList<Process> results = mainEngine.getFinalProcesses();
        ArrayList<GanttChartBlocks> blocks = mainEngine.getGanttChartBlocks();

        // Calculate total simulation time from the last block's end time
        totalTime = 0;
        if (blocks != null) {
            for (GanttChartBlocks b : blocks)
                totalTime = Math.max(totalTime, b.getEndTime());
        }

        // Reset animation state
        currentTime = 0;
        timerLabel.setText("Timer: 0");

        updateGanttChart(blocks);
        updateProcessTable(results);

        // Stop any previous timer
        if (simulationTimer != null && simulationTimer.isRunning()) {
            simulationTimer.stop();
        }

        // Tick every second — each tick reveals one more unit on the Gantt chart
        simulationTimer = new javax.swing.Timer(1000, null);
        simulationTimer.addActionListener(e -> {
            currentTime++;
            timerLabel.setText("Timer: " + currentTime);
            if (liveGanttChart != null) liveGanttChart.repaint();
            if (currentTime >= totalTime) {
                simulationTimer.stop();
            }
        });
        simulationTimer.start();
    }

    private void updateGanttChart(ArrayList<GanttChartBlocks> blocks) {
        int PX_PER_UNIT = 30;
        int BAR_H = 38;
        int TICK_Y = BAR_H + 15;
        int PAD_LEFT = 10;

        liveGanttChart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (blocks == null || blocks.isEmpty()) return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // --- 1. Draw all bars at full color ---
                int x = PAD_LEFT;

                g2.setFont(branding.jetBrainsBSmall);
                g2.setColor(branding.light);
                FontMetrics fmTick = g2.getFontMetrics();
                g2.drawString("0", x - fmTick.stringWidth("0") / 2, TICK_Y);

                for (GanttChartBlocks block : blocks) {
                    int width = (block.getEndTime() - block.getStartTime()) * PX_PER_UNIT;
                    g2.setColor(block.getColor());
                    g2.fillRoundRect(x, 0, width, BAR_H, 10, 10);

                    // Label
                    g2.setColor(Color.WHITE);
                    g2.setFont(branding.jetBrainsBMedium);
                    FontMetrics fm = g2.getFontMetrics();
                    int lx = x + (width - fm.stringWidth(block.getProcessID())) / 2;
                    int ly = (BAR_H + fm.getAscent() - fm.getDescent()) / 2;
                    g2.drawString(block.getProcessID(), lx, ly);

                    // End tick
                    g2.setFont(branding.jetBrainsBSmall);
                    String t = String.valueOf(block.getEndTime());
                    g2.setColor(branding.light);
                    FontMetrics fmS = g2.getFontMetrics();
                    g2.drawString(t, x + width - fmS.stringWidth(t) / 2, TICK_Y);

                    x += width;
                }

                // --- 2. Overlay a dark mask over the unfilled (future) portion ---
                int filledPx = PAD_LEFT + currentTime * PX_PER_UNIT;
                int totalPx  = x; // x is now at the right edge of the last block
                if (filledPx < totalPx) {
                    g2.setColor(new Color(0, 0, 0, 160)); // semi-transparent dark
                    g2.fillRect(filledPx, 0, totalPx - filledPx, BAR_H);
                }

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                int totalWidth = PAD_LEFT;
                if (blocks != null) {
                    for (GanttChartBlocks b : blocks)
                        totalWidth += (b.getEndTime() - b.getStartTime()) * PX_PER_UNIT;
                }
                totalWidth += PAD_LEFT;
                return new Dimension(Math.max(totalWidth, 600), 60);
            }
        };
        liveGanttChart.setOpaque(false);

        // Wrap in GridBagLayout panel so the chart is centered in the scroll viewport
        JPanel ganttWrapper = new JPanel(new GridBagLayout());
        ganttWrapper.setOpaque(false);
        ganttWrapper.add(liveGanttChart);

        ganttScrollPane.setViewportView(ganttWrapper);
        ganttScrollPane.revalidate();
        ganttScrollPane.repaint();
    }

    private void updateProcessTable(ArrayList<Process> results) {
        String[] headers = {
            "Process ID", "Burst Time", "Arrival Time", "Priority No.",
            "Waiting\nTime", "Turnaround\nTime"
        };

        // Rebuild the fixed header panel (different column count than the placeholder)
        tableHeaderPanel.removeAll();
        tableHeaderPanel.setLayout(new GridLayout(1, headers.length, 8, 0));
        for (String h : headers) tableHeaderPanel.add(createMultiLineHeader(h));
        tableHeaderPanel.revalidate();
        tableHeaderPanel.repaint();

        // Only the data rows go into the scroll pane viewport
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setOpaque(false);

        for (Process p : results) {
            Object[] row = {
                p.getID(),
                String.valueOf(p.getBurstTime()),
                String.valueOf(p.getArrivalTime()),
                p.getPriority() > 0 ? String.valueOf(p.getPriority()) : "N/A",
                String.valueOf(p.getWaitingTime()),
                String.valueOf(p.getTurnaroundTime())
            };
            dataPanel.add(Box.createVerticalStrut(8));
            dataPanel.add(createDataRow(row, p.getColor()));
        }

        tableScrollPane.setViewportView(dataPanel);
        tableScrollPane.revalidate();
        tableScrollPane.repaint();
    }

    /**
     * Re-creates all data rows with fresh borders/colours from the current theme.
     * If no simulation has been run yet the placeholder rows are also refreshed.
     */
    public void refreshStyles() {
        java.util.ArrayList<model.Process> results = mainEngine.getFinalProcesses();
        if (results != null && !results.isEmpty()) {
            updateProcessTable(results);
        } else {
            // Rebuild the placeholder rows shown before any simulation runs
            Object[][] data = {
                { "P1", "2", "0", "N/A", "0", "2", "0", "2" },
                { "P2", "2", "2", "N/A", "0", "2", "0", "2" },
                { "P3", "2", "4", "N/A", "0", "2", "0", "2" },
            };
            Color[] placeholderColors = { P1_COLOR, P2_COLOR, P3_COLOR };

            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
            dataPanel.setOpaque(false);
            for (int i = 0; i < data.length; i++) {
                dataPanel.add(Box.createVerticalStrut(8));
                dataPanel.add(createDataRow(data[i], placeholderColors[i]));
            }
            tableScrollPane.setViewportView(dataPanel);
            tableScrollPane.revalidate();
            tableScrollPane.repaint();
        }
    }
}