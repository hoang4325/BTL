package com.view.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Header extends javax.swing.JPanel {

    private javax.swing.JLabel timeLabel;

    public Header() {
        initComponents();
        setOpaque(false);
        Timer timer = new Timer(1000, e -> repaint());
        timer.start();
//        initDateTime();
    }

//    private void initDateTime() {
//        DateTimeLabel dateTimeLabel = new DateTimeLabel();
//        dateTimeLabel.setForeground(Color.WHITE);  // Màu chữ
//         dateTimeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
//    
//    // Đặt preferred size để label đủ không gian hiện font lớn
//    dateTimeLabel.setPreferredSize(new Dimension(300, 50));
//        add(dateTimeLabel, BorderLayout.EAST);  // Đặt sang bên phải thanh header
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 772, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.decode("#F5AE1A"));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(0, 0, 25, getHeight());
        g2.fillRect(getWidth() - 25, getHeight() - 25, getWidth(), getHeight());
        g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        String dateTime = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy"));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(dateTime);
        int x = getWidth() - textWidth - 20; // cách lề phải 20px
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(dateTime, x, y);
        super.paintComponent(grphcs);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
