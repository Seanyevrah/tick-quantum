package graphics;

import engine.MainEngine;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

public class MainMenuHowToUse extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;

    private JLabel titleLabel;
    private JTextArea algoDesc, outputDesc, removeHint;
    private JLabel tableTitleLabel;
    private JPanel algoImage, tableImage, buttonsImage, outputImage;
    private JButton backButton;

    public MainMenuHowToUse(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
    }

    // ── Layout ────────────────────────────────────────────────────────────
    private void initializeMainPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        int m = 40;
        wrapper.add(blankPanel(m, 0, false), BorderLayout.NORTH);
        wrapper.add(blankPanel(m, 0, false), BorderLayout.SOUTH);
        wrapper.add(blankPanel(0, m, true),  BorderLayout.WEST);
        wrapper.add(blankPanel(0, m, true),  BorderLayout.EAST);

        JPanel container = new JPanel(null) {
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
        container.setOpaque(false);

        buildContent(container);

        // Reposition everything whenever the container is resized
        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionContent(container.getWidth(), container.getHeight());
            }
        });

        wrapper.add(container, BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);
    }

    // ── Build (create & add components once) ─────────────────────────────
    private void buildContent(JPanel c) {
        titleLabel = new JLabel("How To Use", SwingConstants.CENTER);
        titleLabel.setFont(branding.jetBrainsBExtraLarge);
        titleLabel.setForeground(branding.light);
        c.add(titleLabel);

        algoDesc = makeText("Select your preferred algorithm by choosing from the dropdown menu.");
        c.add(algoDesc);

        algoImage = makeImagePanel(branding.howToUseAlgo, "howToUseAlgo");
        c.add(algoImage);

        outputDesc = makeText("The output of the simulation will be shown here along with the Gantt chart and the computation table.");
        c.add(outputDesc);

        tableTitleLabel = new JLabel("Input your data on this table shown below");
        tableTitleLabel.setFont(branding.jetBrainsBMedium);
        tableTitleLabel.setForeground(branding.light);
        c.add(tableTitleLabel);

        tableImage = makeImagePanel(branding.howToUseInput, "howToUseInput");
        c.add(tableImage);

        removeHint = makeText("To remove multiple processes, select the process under the Process Id column and select the Remove Process button.");
        c.add(removeHint);

        buttonsImage = makeImagePanel(branding.howToUseButtons, "howToUseButtons");
        c.add(buttonsImage);

        outputImage = makeImagePanel(branding.howToUseOutput, "howToUseOutput");
        c.add(outputImage);

        backButton = new JButton("Back to Menu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
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
        backButton.setBackground(branding.dark);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "MainMenu");
        });
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backButton.setBackground(branding.darkGray);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backButton.setBackground(branding.dark);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                backButton.setBackground(branding.darkGray);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                backButton.setBackground(branding.darkGray);
            }
        });
        c.add(backButton);
    }

    // ── Reposition (called on every resize) ───────────────────────────────
    private void repositionContent(int W, int H) {
        int pad   = (int) (W * 0.04);   // ~4 % side padding
        int titleH = (int) (H * 0.09);

        // ── Column X boundaries ───────────────────────────────────────────
        // Left column  : pad … 28 % W
        // Middle column: 30 % … 78 % W
        // Right column : 80 % … (W - pad)
        int leftW   = (int) (W * 0.26);
        int midX    = (int) (W * 0.30);
        int midW    = (int) (W * 0.48);
        int rightX  = (int) (W * 0.79);
        int rightW  = W - rightX - pad;

        // ── Row Y boundaries ─────────────────────────────────────────────
        int topRowY = titleH;                          // images start here
        int topRowH = (int) (H * 0.50);               // top half height
        int botRowY = (int) (H * 0.56);               // bottom half starts
        int botRowH = H - botRowY - pad;

        int btnW = 200, btnH = 48;

        // Title — centered across full width
        titleLabel.setBounds(0, (int)(titleH * 0.1), W, (int)(titleH * 0.8));

        // LEFT COLUMN
        algoDesc.setBounds(pad, topRowY, leftW, (int)(H * 0.12));
        algoImage.setBounds(pad + (int)(leftW * 0.1), topRowY + (int)(H * 0.13),
                            (int)(leftW * 0.85), (int)(topRowH * 0.62));
        outputDesc.setBounds(pad, botRowY, leftW, (int)(H * 0.14));

        // MIDDLE COLUMN
        int tableLabelH = (int)(H * 0.05);
        tableTitleLabel.setBounds(midX, topRowY, midW, tableLabelH);
        tableImage.setBounds(midX, topRowY + tableLabelH + 4, midW, (int)(topRowH * 0.48));
        removeHint.setBounds(midX, topRowY + tableLabelH + 4 + (int)(topRowH * 0.48) + 8,
                             (int)(midW * 0.68), (int)(H * 0.18));

        // RIGHT COLUMN
        buttonsImage.setBounds(rightX, topRowY, rightW, topRowH);

        // BOTTOM RIGHT — output preview
        outputImage.setBounds(midX + (int)(midW * 0.38), botRowY,
                              W - (midX + (int)(midW * 0.38)) - pad, botRowH);

        // BOTTOM LEFT — back button pinned to lower-left inside container
        backButton.setBounds(pad, H - btnH - pad, btnW, btnH);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private JPanel blankPanel(int height, int width, boolean isHorizontal) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        if (isHorizontal) p.setPreferredSize(new Dimension(width, 0));
        else              p.setPreferredSize(new Dimension(0, height));
        return p;
    }

    private JTextArea makeText(String t) {
        JTextArea ta = new JTextArea(t);
        ta.setFont(branding.jetBrainsBMedium);
        ta.setForeground(branding.light);
        ta.setOpaque(false);
        ta.setEditable(false);
        ta.setFocusable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    private JPanel makeImagePanel(Image image, String fieldName) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                int pw = getWidth(), ph = getHeight();
                if (image != null) {
                    int imgW = image.getWidth(this);
                    int imgH = image.getHeight(this);

                    if (imgW > 0 && imgH > 0) {
                        // Scale to fit inside the panel while preserving aspect ratio
                        double scale = Math.min((double) pw / imgW, (double) ph / imgH);
                        int drawW = (int) (imgW * scale);
                        int drawH = (int) (imgH * scale);

                        // Center within the panel
                        int drawX = (pw - drawW) / 2;
                        int drawY = (ph - drawH) / 2;

                        g2.drawImage(image, drawX, drawY, drawW, drawH, this);
                    }
                } else {
                    g2.setColor(new Color(28, 28, 28));
                    g2.fillRoundRect(0, 0, pw, ph, 12, 12);
                    g2.setColor(new Color(70, 70, 70));
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND, 0, new float[]{7, 4}, 0));
                    g2.drawRoundRect(1, 1, pw - 2, ph - 2, 12, 12);
                    g2.setStroke(new BasicStroke(1f));
                    g2.setColor(new Color(55, 55, 55));
                    g2.drawLine(10, 10, pw - 10, ph - 10);
                    g2.drawLine(pw - 10, 10, 10, ph - 10);
                    g2.setFont(branding.jetBrainsBMedium);
                    g2.setColor(new Color(110, 110, 110));
                    FontMetrics fm = g2.getFontMetrics();
                    String txt = "[branding." + fieldName + "]";
                    g2.drawString(txt, (pw - fm.stringWidth(txt)) / 2, ph / 2 + fm.getAscent() / 2 - 2);
                }
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (branding.backgroundDark != null) {
            g.drawImage(branding.backgroundDark, 0, 0, getWidth(), getHeight(), this);
        }
    }
}