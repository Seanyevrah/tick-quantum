package graphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.*;
import engine.MainEngine;
import graphics.SimulatorMain.RoundedBorder;
import scheduler.*;
import model.Process;

public class SimulatorMain extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;

    private JPanel mainPanel;
    private JPanel leftPanel, rightPanel;

    // Left panel components
    private JLabel algorithmLabel;
    private JComboBox<String> algorithmComboBox;
    private JLabel quantumTimeLabel;
    private JTextField quantumTimeField;
    private JButton simulateButton;
    private JButton addProcessButton;
    private JButton removeProcessButton;
    private JButton importTextFileButton;
    private JButton randomProcessesButton;

    // Right panel components
    private JPanel processTablePanel;
    private JLabel processIdHeader, burstTimeHeader, arrivalTimeHeader, priorityNoHeader;

    private ArrayList<Process> processes = new ArrayList<>();

    public SimulatorMain(MainEngine mainEngine, Branding branding) {
        this.branding = branding;
        this.mainEngine = mainEngine;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
        initializePanels();

        updateAlgorithmFields();
    }

    public void initializeMainPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(branding.dark);

        int mw = 20, mh = 20;

        JPanel north = blankPanel(branding.dark, 0, mh);
        JPanel south = blankPanel(branding.dark, 0, mh);
        JPanel west = blankPanel(branding.dark, mw, 0);
        JPanel east = blankPanel(branding.dark, mw, 0);

        wrapper.add(north, BorderLayout.NORTH);
        wrapper.add(south, BorderLayout.SOUTH);
        wrapper.add(west, BorderLayout.WEST);
        wrapper.add(east, BorderLayout.EAST);

        mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBackground(branding.dark);
        wrapper.add(mainPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }
    
    public void initializePanels() {
        initializeLeftPanel();
        initializeRightPanel();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }
    
    private void initializeLeftPanel() {
        leftPanel = new JPanel(new BorderLayout()) {
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
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(350, 0));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel algorithmCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        algorithmCard.setOpaque(false);
        algorithmCard.setLayout(new BoxLayout(algorithmCard, BoxLayout.Y_AXIS));
        algorithmCard.setBorder(new EmptyBorder(16, 16, 16, 16));
        algorithmCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        algorithmCard.setPreferredSize(new Dimension(getWidth(), 300));
        algorithmCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));

        algorithmLabel = new JLabel("Algorithm");
        algorithmLabel.setFont(branding.jetBrainsBMedium);
        algorithmLabel.setForeground(branding.light);
        algorithmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] algorithms = {
            "FCFS (First Come First Serve)",
            "SJF (Shortest Job First)",
            "SRTF (Shortest Remaining Time First)",
            "Round Robin",
            "Priority (Preemptive)",
            "Priority (Non-Preemptive)"
        };
        algorithmComboBox = new JComboBox<>(algorithms);
        styleComboBox(algorithmComboBox);
        algorithmComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        algorithmComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        algorithmComboBox.addActionListener(e -> updateAlgorithmFields());

        quantumTimeLabel = new JLabel("Quantum Time");
        quantumTimeLabel.setFont(branding.jetBrainsBSmall);
        quantumTimeLabel.setForeground(branding.darkGray);
        quantumTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        quantumTimeField = new JTextField();
        styleTextField(quantumTimeField);
        quantumTimeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantumTimeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        simulateButton = createSimulateButton("Simulate");
        JPanel simulateWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        simulateWrapper.setOpaque(false);
        simulateWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        simulateWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        simulateWrapper.add(simulateButton);
        simulateButton.addActionListener(e -> {
            if (!validateProcessTable()) return;
            
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            int quantum = 0;

            if (algorithm.contains("Round Robin")) {
                quantum = Integer.parseInt(quantumTimeField.getText().trim());
            }

            ArrayList<Process> processes = mainEngine.getGUI().getSimulatorMain().getProcesses();
            mainEngine.runSimulation(processes, algorithm, quantum);
        });

        algorithmCard.add(algorithmLabel);
        algorithmCard.add(Box.createVerticalStrut(8));
        algorithmCard.add(algorithmComboBox);
        algorithmCard.add(Box.createVerticalStrut(14));
        algorithmCard.add(quantumTimeLabel);
        algorithmCard.add(Box.createVerticalStrut(6));
        algorithmCard.add(quantumTimeField);
        algorithmCard.add(Box.createVerticalStrut(50));
        algorithmCard.add(simulateWrapper);

        JPanel actionsCard = new JPanel();
        actionsCard.setOpaque(false);
        actionsCard.setLayout(new BoxLayout(actionsCard, BoxLayout.Y_AXIS));
        actionsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        addProcessButton = createActionButton("+ ",   "Add Process");
        removeProcessButton = createActionButton("\u2014 ", "Remove Process");
        importTextFileButton = createActionButton("\u21B5 ", "Import Text File");
        randomProcessesButton = createActionButton("\u2684 ", "Random Processes");

        addProcessButton.addActionListener(e -> addProcess());
        removeProcessButton.addActionListener(e -> removeProcess());
        randomProcessesButton.addActionListener(e -> randomProcesses());
        importTextFileButton.addActionListener(e -> importFromFile());

        actionsCard.add(addProcessButton);
        actionsCard.add(Box.createVerticalStrut(20));
        actionsCard.add(removeProcessButton);
        actionsCard.add(Box.createVerticalStrut(20));
        actionsCard.add(importTextFileButton);
        actionsCard.add(Box.createVerticalStrut(20));
        actionsCard.add(randomProcessesButton);
        actionsCard.add(Box.createVerticalGlue());
        
        content.add(algorithmCard);
        content.add(Box.createVerticalStrut(20));
        content.add(actionsCard);
        content.add(Box.createVerticalGlue());

        leftPanel.add(content, BorderLayout.CENTER);
    }
    
    public void initializeRightPanel() {
        rightPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                g2.dispose();
            }
        };
        rightPanel.setOpaque(false);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel headerRow = new JPanel(new GridLayout(1, 4, 0, 0));
        headerRow.setOpaque(false);
        headerRow.setPreferredSize(new Dimension(0, 60));
        headerRow.setBorder(new EmptyBorder(10, 20, 10, 20));

        headerRow.add(createHeaderLabel("Process ID", false));
        headerRow.add(createHeaderLabel("Burst Time", false));
        headerRow.add(createHeaderLabel("Arrival Time", false));
        priorityNoHeader = createHeaderLabel("Priority No.", true);
        headerRow.add(priorityNoHeader);

        processTablePanel = new JPanel();
        processTablePanel.setLayout(new BoxLayout(processTablePanel, BoxLayout.Y_AXIS));
        processTablePanel.setOpaque(false);
        processTablePanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        processTablePanel.add(createProcessRow("P1", branding.processColor[0], "2", "0", false));
        processTablePanel.add(Box.createVerticalStrut(20));
        processTablePanel.add(createProcessRow("P2", branding.processColor[1], "2", "2", false));
        processTablePanel.add(Box.createVerticalStrut(20));
        processTablePanel.add(createProcessRow("P3", branding.processColor[2], "2", "4", false));

        Process p1 = new Process("P1", branding.processColor[0], 0, 2, 1);
        Process p2 = new Process("P2", branding.processColor[0], 2, 2, 2);
        Process p3 = new Process("P3", branding.processColor[0], 4, 2, 3);

        processes.add(p1);
        processes.add(p2);
        processes.add(p3);

        JPanel topContent = new JPanel(new BorderLayout());
        topContent.setOpaque(false); 
        topContent.add(headerRow, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(processTablePanel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        topContent.add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 14));
        footer.setOpaque(false);
        JButton backToMenuButton = createSimulateButton("Back To Menu");
        backToMenuButton.setPreferredSize(new Dimension(190, 44));
        footer.add(backToMenuButton);

        wrapper.add(topContent, BorderLayout.CENTER);
        wrapper.add(footer, BorderLayout.SOUTH);

        rightPanel.add(wrapper, BorderLayout.CENTER);
    }

    public JPanel createProcessRow(String name, Color labelColor,
                                    String burst, String arrival, boolean priorityEnabled) {
        JPanel row = new JPanel(new GridLayout(1, 4, 20, 0));
        row.setBackground(branding.dark);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        
        JLabel idLabel = new JLabel(name, SwingConstants.CENTER);
        idLabel.setFont(branding.jetBrainsBMedium);
        idLabel.setForeground(branding.light);
        idLabel.setOpaque(true);
        idLabel.setBackground(labelColor);
        idLabel.setBorder(new RoundedBorder(10, labelColor));
        
        JTextField burstField = new JTextField(burst);
        styleTextField(burstField);
        burstField.setHorizontalAlignment(JTextField.CENTER);
        
        JTextField arrivalField = new JTextField(arrival);
        styleTextField(arrivalField);
        arrivalField.setHorizontalAlignment(JTextField.CENTER);
        
        JTextField priorityField = new JTextField();
        priorityField.setHorizontalAlignment(JTextField.CENTER);
        priorityField.setText("");
        
        styleProcessField(priorityField, priorityEnabled);

        row.add(idLabel);
        row.add(burstField);
        row.add(arrivalField);
        row.add(priorityField);

        return row;
    }

    public JLabel createHeaderLabel(String text, boolean muted) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(branding.jetBrainsBMedium);
        lbl.setForeground(muted ? branding.darkGray : branding.light);
        return lbl;
    }
    
    public JButton createSimulateButton(String text) {
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
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(160, 44));
        btn.setMaximumSize(new Dimension(200, 44));
        return btn;
    }
    
    public JButton createActionButton(String icon, String text) {
        JButton btn = new JButton(icon + text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(branding.jetBrainsBMedium);
        btn.setForeground(branding.light);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        btn.setPreferredSize(new Dimension(350, 62));
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        return btn;
    }

    public void styleTextField(JTextField field) {
        field.setFont(branding.jetBrainsRMedium);
        field.setForeground(branding.light);
        field.setBackground(branding.dark);
        field.setCaretColor(branding.light);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(branding.light, 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    public void styleComboBox(JComboBox<String> box) {
        box.setFont(branding.jetBrainsRMedium);
        box.setForeground(branding.light);
        box.setBackground(branding.dark);
        box.setBorder(new LineBorder(branding.light, 1, true));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? branding.darkGray : branding.dark);
                setForeground(branding.light);
                setFont(branding.jetBrainsRMedium);
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return this;
            }
        });
    }

    public JPanel blankPanel(Color bg, int w, int h) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        if (w > 0) p.setPreferredSize(new Dimension(w, 0));
        if (h > 0) p.setPreferredSize(new Dimension(0, h));
        return p;
    }

    public void addProcess() {
        int currentCount = processTablePanel.getComponentCount() / 2 + 2;
        if (currentCount > 20) return;

        String id = "P" + currentCount;
        Color color = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
        
        // Determine if priority column should be enabled
        boolean priorityEnabled = isPriorityAlgorithm();

        JPanel newRow = createProcessRow(id, color, "1", "0", priorityEnabled);
        processTablePanel.add(Box.createVerticalStrut(20));
        processTablePanel.add(newRow);
        processTablePanel.revalidate();
        processTablePanel.repaint();
    }

    public void removeProcess() {
        int rowCount = processTablePanel.getComponentCount();
        if (rowCount <= 5) return;

        processTablePanel.remove(rowCount - 1);
        processTablePanel.remove(rowCount - 2);
        processTablePanel.revalidate();
        processTablePanel.repaint();
    }

    public void randomProcesses() {
        processTablePanel.removeAll();

        int processCount = 3 + (int)(Math.random() * 18); // 3–20 processes
        boolean priorityEnabled = isPriorityAlgorithm();
        
        ArrayList<Integer> priorities = new ArrayList<>();
        if (priorityEnabled) {
            for (int i = 1; i <= 20; i++) {
                priorities.add(i);
            }
            Collections.shuffle(priorities);
        }

        for (int i = 0; i < processCount; i++) {
            String id = "P" + (i + 1);

            // Burst: 1–30
            String burst = String.valueOf(1 + (int)(Math.random() * 30));

            // Arrival: 0–30
            String arrival = String.valueOf((int)(Math.random() * 31));

            Color color = branding.processColor[i % branding.processColor.length];

            JPanel row = createProcessRow(id, color, burst, arrival, priorityEnabled);

            if (priorityEnabled) {
                String priority = String.valueOf(priorities.get(i));
                JTextField priorityField = (JTextField) row.getComponent(3);
                priorityField.setText(priority);
                styleProcessField(priorityField, true);
            } else {
                JTextField priorityField = (JTextField) row.getComponent(3);
                styleProcessField(priorityField, false);
                styleHeaderLabel(priorityNoHeader, false);
            }

            processTablePanel.add(row);
            if (i < processCount - 1) {
                processTablePanel.add(Box.createVerticalStrut(20));
            }
        }

        processTablePanel.revalidate();
        processTablePanel.repaint();
    }

    public void importFromFile() {
        JOptionPane.showMessageDialog(this, "Import functionality not implemented yet.");
    }

    private boolean isPriorityAlgorithm() {
        String algo = (String) algorithmComboBox.getSelectedItem();
        return algo.contains("Priority");
    }

    private boolean isRoundRobinAlgorithm() {
        String algo = (String) algorithmComboBox.getSelectedItem();
        return algo.contains("Round Robin");
    }

    private void updateAlgorithmFields() {
        boolean roundRobin = isRoundRobinAlgorithm();
        boolean priority = isPriorityAlgorithm();

        styleProcessField(quantumTimeField, roundRobin);
        styleHeaderLabel(quantumTimeLabel, roundRobin);
        
        styleHeaderLabel(priorityNoHeader, priority);
        for (Component comp : processTablePanel.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel row = (JPanel) comp;
                JTextField priorityField = (JTextField) row.getComponent(3);
                priorityField.setEnabled(priority);
                styleProcessField(priorityField, priority);
            }
        }
    }

    private void styleProcessField(JTextField field, boolean enabled) {
        field.setEnabled(enabled);
        field.setBackground(branding.dark);
        field.setForeground(enabled ? branding.light : branding.darkGray);
        field.setCaretColor(enabled ? branding.light : branding.darkGray);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(enabled ? branding.light : branding.darkGray, 2, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }
    
    private void styleHeaderLabel(JLabel label, boolean enabled) {
        label.setForeground(enabled ? branding.light : branding.darkGray);
    }

    public boolean validateProcessTable() {
        if (algorithmComboBox.getSelectedItem().toString().contains("Round Robin")) {
            if (!isInteger(quantumTimeField.getText())) {
                showValidationError("Quantum Time must be an integer.");
                return false;
            }

            int quantum = Integer.parseInt(quantumTimeField.getText().trim());

            if (quantum < 1 || quantum > 10) {
                showValidationError("Quantum Time must be between 1 and 10.");
                return false;
            }
        }
        
        Set<Integer> usedPriorities = new HashSet<>();

        for (Component comp : processTablePanel.getComponents()) {
            if (!(comp instanceof JPanel)) continue;

            JPanel row = (JPanel) comp;
            Component[] fields = row.getComponents();

            JTextField burstField = (JTextField) fields[1];
            JTextField arrivalField = (JTextField) fields[2];
            JTextField priorityField = (JTextField) fields[3];

            // ---- Burst ----
            if (!isInteger(burstField.getText())) {
                showValidationError("Burst Time must be an integer.");
                return false;
            }

            int burst = Integer.parseInt(burstField.getText().trim());
            if (burst < 1 || burst > 30) {
                showValidationError("Burst Time must be between 1 and 30.");
                return false;
            }

            // ---- Arrival ----
            if (!isInteger(arrivalField.getText())) {
                showValidationError("Arrival Time must be an integer.");
                return false;
            }

            int arrival = Integer.parseInt(arrivalField.getText().trim());
            if (arrival < 0 || arrival > 30) {
                showValidationError("Arrival Time must be between 0 and 30.");
                return false;
            }

            // ---- Priority ----
            if (priorityField.isEnabled()) {
                if (!isInteger(priorityField.getText())) {
                    showValidationError("Priority must be an integer.");
                    return false;
                }

                int priority = Integer.parseInt(priorityField.getText().trim());

                if (priority < 1 || priority > 20) {
                    showValidationError("Priority must be between 1 and 20.");
                    return false;
                }

                if (!usedPriorities.add(priority)) {
                    showValidationError("Priority values must not duplicate.");
                    return false;
                }
            }
        }

        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public ArrayList<Process> getProcesses() {
        return this.processes;
    }

    
    // ==================================================
    //                   ROUND BORDER
    // ==================================================
    
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color  = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius * 2, radius * 2);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) { return new Insets(4, 8, 4, 8); }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(4, 8, 4, 8);
            return insets;
        }
    }
}