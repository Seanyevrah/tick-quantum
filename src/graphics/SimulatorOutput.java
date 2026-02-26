package graphics;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import engine.MainEngine;

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

    // Bottom panel components
    private JButton goBackButton;
    private JPanel processTablePanel;

    // Colors matching the dark theme
    private static final Color P1_COLOR     = new Color(72, 187, 120);   // green
    private static final Color P2_COLOR     = new Color(176, 80, 100);   // pink-red
    private static final Color P3_COLOR     = new Color(110, 90, 190);   // purple

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
        ganttChartPanel = createGanttChart();

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

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int totalBars = labels.length;
                int totalW = totalBars * BAR_W;
                int startX = (getWidth() - totalW) / 2;

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
                return new Dimension(320, 60);
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
        processTablePanel = createProcessTable();

        bottomPanel.add(goBackRow, BorderLayout.NORTH);
        bottomPanel.add(processTablePanel, BorderLayout.CENTER);
    }

    public JPanel createProcessTable() {
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

        // Header row
        JPanel headerRow = new JPanel(new GridLayout(1, 8, 8, 0));
        headerRow.setOpaque(false);
        for (String h : headers) {
            headerRow.add(createMultiLineHeader(h));
        }

        // Data rows
        Object[][] data = {
            { "P1", "2", "0", "N/A", "0", "2", "0", "2" },
            { "P2", "2", "2", "N/A", "0", "2", "0", "2" },
            { "P3", "2", "4", "N/A", "0", "2", "0", "2" },
        };

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setOpaque(false);

        for (Object[] row : data) {
            dataPanel.add(Box.createVerticalStrut(8));
            dataPanel.add(createDataRow(row));
        }

        wrapper.add(headerRow, BorderLayout.NORTH);
        wrapper.add(dataPanel, BorderLayout.CENTER);

        return wrapper;
    }

    public JPanel createDataRow(Object[] values) {
        JPanel row = new JPanel(new GridLayout(1, 8, 8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        for (Object val : values) {
            JLabel cell = new JLabel(val.toString(), SwingConstants.CENTER);
            cell.setFont(branding.jetBrainsBMedium);
            cell.setForeground(branding.light);
            cell.setOpaque(false);
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
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT); // horizontal center in BoxLayout
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
}