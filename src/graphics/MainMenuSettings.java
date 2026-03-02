package graphics;

import engine.MainEngine;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

public class MainMenuSettings extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;

    private Runnable backAction;
    private Runnable themeChangeListener;

    // Content components
    private JLabel titleLabel;
    private JLabel themeSectionLabel;
    private JPanel themeUnderline;
    private JPanel darkCheckbox, lightCheckbox;
    private JLabel darkLabel, lightLabel;
    private JButton backButton;
    private JPanel container;

    // Checkbox state
    private boolean darkSelected  = true;
    private boolean lightSelected = false;

    public MainMenuSettings(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
    }

    public void setBackAction(Runnable action) {
        this.backAction = action;
    }

    public void setThemeChangeListener(Runnable listener) {
        this.themeChangeListener = listener;
    }

    // ── Layout ────────────────────────────────────────────────────────────
    private void initializeMainPanel() {
        JPanel margin = new JPanel(new BorderLayout());
        margin.setOpaque(false);

        int m = 60, n = 300;
        margin.add(blankPanel(m, 0, false), BorderLayout.NORTH);
        margin.add(blankPanel(m, 0, false), BorderLayout.SOUTH);
        margin.add(blankPanel(0, n, true),  BorderLayout.WEST);
        margin.add(blankPanel(0, n, true),  BorderLayout.EAST);

        container = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        container.setOpaque(false);

        buildContent(container);

        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionContent(container.getWidth(), container.getHeight());
            }
        });

        margin.add(container, BorderLayout.CENTER);
        add(margin, BorderLayout.CENTER);
    }

    // ── Build components — all fields assigned before any listener fires ──
    private void buildContent(JPanel c) {

        // Title
        titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(branding.jetBrainsBExtraLarge);
        titleLabel.setForeground(branding.light);
        c.add(titleLabel);

        // "Theme" section label
        themeSectionLabel = new JLabel("Theme");
        themeSectionLabel.setFont(branding.jetBrainsBLarge);
        themeSectionLabel.setForeground(branding.light);
        c.add(themeSectionLabel);

        // Underline beneath "Theme"
        themeUnderline = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        themeUnderline.setOpaque(false);
        c.add(themeUnderline);

        // ── Create BOTH checkboxes first so neither field is null when
        //    a MouseListener fires on the other one. ──────────────────────
        darkCheckbox  = makeCheckbox(true);
        lightCheckbox = makeCheckbox(false);
        c.add(darkCheckbox);
        c.add(lightCheckbox);

        darkLabel = new JLabel("Dark");
        darkLabel.setFont(branding.jetBrainsBMedium);
        darkLabel.setForeground(branding.light);
        c.add(darkLabel);

        lightLabel = new JLabel("Light");
        lightLabel.setFont(branding.jetBrainsBMedium);
        lightLabel.setForeground(branding.light);
        c.add(lightLabel);

        // Back to Menu button
        backButton = new JButton("Back to Menu") {
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
        backButton.setFont(branding.jetBrainsBMedium);
        backButton.setForeground(branding.light);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (backAction != null) backAction.run();
            if (parentContainer != null) {
                CardLayout cl = (CardLayout) parentContainer.getLayout();
                cl.show(parentContainer, "MainMenu");
            }
        });
        c.add(backButton);
    }

    // ── Reposition on resize ──────────────────────────────────────────────
    private void repositionContent(int W, int H) {
        int pad = (int) (W * 0.05);

        // Title
        titleLabel.setBounds(0, (int)(H * 0.04), W, (int)(H * 0.15));

        // "Theme" section
        int sectionY = (int) (H * 0.22);
        int sectionH = (int) (H * 0.09);
        themeSectionLabel.setBounds(pad, sectionY, (int)(W * 0.6), sectionH);

        // Underline
        themeUnderline.setBounds(pad, sectionY + sectionH, W - pad * 2, 2);

        // Checkbox row
        int rowY   = sectionY + sectionH + (int)(H * 0.04);
        int cbSize = (int) (H * 0.07);
        int gap    = (int) (W * 0.015);

        int darkX  = pad * 2;
        darkCheckbox.setBounds(darkX, rowY, cbSize, cbSize);
        darkLabel.setBounds(darkX + cbSize + gap, rowY, (int)(W * 0.12), cbSize);

        int lightX = (int)(W * 0.48);
        lightCheckbox.setBounds(lightX, rowY, cbSize, cbSize);
        lightLabel.setBounds(lightX + cbSize + gap, rowY, (int)(W * 0.12), cbSize);

        // Back to Menu — pinned to bottom-left
        int btnW = 200, btnH = 48;
        backButton.setBounds(pad, H - btnH - pad, btnW, btnH);
    }

    // ── Checkbox factory ──────────────────────────────────────────────────
    private JPanel makeCheckbox(boolean isDark) {
        JPanel cb = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int s   = Math.min(getWidth(), getHeight());
                int arc = (int)(s * 0.2);

                // Outer rounded square border
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, s - 2, s - 2, arc, arc);

                // Inner fill — reads from the outer instance fields directly
                boolean active = isDark ? darkSelected : lightSelected;
                if (active) {
                    int inset = (int)(s * 0.18);
                    g2.fillRoundRect(inset, inset, s - inset * 2, s - inset * 2, arc / 2, arc / 2);
                }
                g2.dispose();
            }
        };
        cb.setOpaque(false);
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cb.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                boolean wasAlreadySelected = isDark ? darkSelected : lightSelected;
                if (wasAlreadySelected) return; // clicking the active option does nothing

                darkSelected  = isDark;
                lightSelected = !isDark;

                // Swap branding.dark ↔ branding.light
                branding.toggleTheme();

                // Walk every component in the UI and reset its background/foreground,
                // then repaint the whole card panel
                if (themeChangeListener != null) themeChangeListener.run();
            }
        });

        return cb;
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private JPanel blankPanel(int height, int width, boolean isHorizontal) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        if (isHorizontal) p.setPreferredSize(new Dimension(width, 0));
        else              p.setPreferredSize(new Dimension(0, height));
        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (branding.backgroundDark != null) {
            g.drawImage(branding.backgroundDark, 0, 0, getWidth(), getHeight(), this);
        }
    }
}