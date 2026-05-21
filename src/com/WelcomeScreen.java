package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class WelcomeScreen extends JFrame {

    // ===== PARTICLE CLASS =====
    class Particle {
        int x, y, size, speed;

        public Particle(int width, int height) {
            Random r = new Random();
            x = r.nextInt(width);
            y = r.nextInt(height);
            size = 2 + r.nextInt(4);
            speed = 1 + r.nextInt(2);
        }

        public void move(int height) {
            y -= speed;
            if (y < 0) y = height;
        }
    }

    private ArrayList<Particle> particles = new ArrayList<>();

    public WelcomeScreen() {

        setTitle("Expense Tracker");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== MAIN PANEL WITH ANIMATION =====
        JPanel panel = new JPanel() {

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Gradient Background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(58,123,213),
                        getWidth(), getHeight(), new Color(0,210,255)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw Particles
                g2d.setColor(new Color(255,255,255,120));
                for (Particle p : particles) {
                    g2d.fillOval(p.x, p.y, p.size, p.size);
                }
            }
        };

        panel.setLayout(new GridBagLayout());

        // ===== CREATE PARTICLES =====
        for (int i = 0; i < 80; i++) {
            particles.add(new Particle(750, 500));
        }

        // Animation Timer
        new Timer(30, e -> {
            for (Particle p : particles) {
                p.move(getHeight());
            }
            panel.repaint();
        }).start();

        // ===== GLASS CARD =====
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 260));
        card.setBackground(new Color(255,255,255,230));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // ===== LOGO =====
        JLabel logo = new JLabel("💰");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== TITLE =====
        JLabel title = new JLabel("Expense Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== TAGLINE =====
        JLabel tagline = new JLabel("Track. Save. Grow.");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tagline.setForeground(Color.GRAY);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ===== BUTTON =====
        JButton startBtn = new JButton("Get Started →");
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        startBtn.setBackground(new Color(58,123,213));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        // ===== HOVER ANIMATION =====
        startBtn.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                startBtn.setBackground(new Color(0,102,204));
                startBtn.setBounds(startBtn.getX()-2, startBtn.getY()-2,
                        startBtn.getWidth()+4, startBtn.getHeight()+4);
            }

            public void mouseExited(MouseEvent e) {
                startBtn.setBackground(new Color(58,123,213));
                startBtn.setBounds(startBtn.getX()+2, startBtn.getY()+2,
                        startBtn.getWidth()-4, startBtn.getHeight()-4);
            }
        });

        // ===== ADD COMPONENTS =====
        card.add(Box.createVerticalGlue());
        card.add(logo);
        card.add(Box.createRigidArea(new Dimension(0,10)));
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0,10)));
        card.add(tagline);
        card.add(Box.createRigidArea(new Dimension(0,20)));
        card.add(startBtn);
        card.add(Box.createVerticalGlue());

        panel.add(card);
        add(panel);

        // ===== BUTTON ACTION =====
        startBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}