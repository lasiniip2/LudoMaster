package client;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Enhanced Ludo Board Panel with modern design
 * Features colorful player tokens, smooth rendering, and attractive styling
 */
public class BoardPanel extends JPanel {
    private Map<String, Integer> positions;
    
    // Modern color scheme
    private static final Color BOARD_BG = new Color(30, 35, 55);
    private static final Color CELL_BORDER = new Color(50, 60, 85);
    private static final Color PATH_CELL = new Color(240, 248, 255);
    private static final Color SAFE_CELL = new Color(144, 238, 144);
    
    // Player colors with better visibility
    private static final Color[] PLAYER_COLORS = {
        new Color(231, 76, 60),   // Red
        new Color(52, 152, 219),  // Blue
        new Color(46, 204, 113),  // Green
        new Color(241, 196, 15)   // Yellow
    };
    
    // Darker shades for borders
    private static final Color[] PLAYER_BORDERS = {
        new Color(192, 57, 43),   // Dark Red
        new Color(41, 128, 185),  // Dark Blue
        new Color(39, 174, 96),   // Dark Green
        new Color(243, 156, 18)   // Dark Yellow
    };
    
    public BoardPanel() {
        setBackground(BOARD_BG);
        setPreferredSize(new Dimension(550, 550));
    }
    
    public void updatePositions(Map<String, Integer> pos) {
        this.positions = pos;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smooth graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int cellSize = 35;
        int grid = 15;
        int boardWidth = grid * cellSize;
        int boardHeight = grid * cellSize;
        
        // Center the board
        int offsetX = (getWidth() - boardWidth) / 2;
        int offsetY = (getHeight() - boardHeight) / 2;
        
        // Draw board background with rounded corners
        g2d.setColor(new Color(245, 245, 250));
        g2d.fillRoundRect(offsetX - 10, offsetY - 10, boardWidth + 20, boardHeight + 20, 20, 20);
        
        // Draw shadow effect
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillRoundRect(offsetX - 8, offsetY - 6, boardWidth + 16, boardHeight + 16, 18, 18);
        
        // Draw grid cells
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                int x = offsetX + j * cellSize;
                int y = offsetY + i * cellSize;
                
                // Determine cell type for coloring
                boolean isSpecialCell = isSpecialPosition(i, j, grid);
                
                if (isSpecialCell) {
                    g2d.setColor(SAFE_CELL);
                } else if (isPathCell(i, j, grid)) {
                    g2d.setColor(PATH_CELL);
                } else {
                    g2d.setColor(getHomeAreaColor(i, j, grid));
                }
                
                g2d.fillRect(x, y, cellSize, cellSize);
                
                // Draw cell border
                g2d.setColor(CELL_BORDER);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRect(x, y, cellSize, cellSize);
                
                // Draw star on safe cells
                if (isSpecialCell) {
                    drawStar(g2d, x + cellSize / 2, y + cellSize / 2, 8, 3);
                }
            }
        }
        
        // Draw center home circle
        int centerX = offsetX + boardWidth / 2;
        int centerY = offsetY + boardHeight / 2;
        int centerRadius = cellSize * 2;
        
        // Draw outer glow
        for (int i = 3; i > 0; i--) {
            g2d.setColor(new Color(255, 215, 0, 50 - i * 10));
            g2d.fillOval(centerX - centerRadius - i * 3, centerY - centerRadius - i * 3, 
                        (centerRadius + i * 3) * 2, (centerRadius + i * 3) * 2);
        }
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillOval(centerX - centerRadius, centerY - centerRadius, centerRadius * 2, centerRadius * 2);
        g2d.setColor(new Color(218, 165, 32));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - centerRadius, centerY - centerRadius, centerRadius * 2, centerRadius * 2);
        
        // Draw "HOME" text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        String homeText = "HOME";
        int textX = centerX - fm.stringWidth(homeText) / 2;
        int textY = centerY + fm.getAscent() / 2;
        g2d.drawString(homeText, textX, textY);
        
        // Draw player tokens
        if (positions != null) {
            int index = 0;
            for (Map.Entry<String, Integer> entry : positions.entrySet()) {
                int pos = entry.getValue() % (grid * grid);
                int x = offsetX + (pos % grid) * cellSize;
                int y = offsetY + (pos / grid) * cellSize;
                
                Color playerColor = PLAYER_COLORS[index % 4];
                Color borderColor = PLAYER_BORDERS[index % 4];
                
                // Draw token shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillOval(x + 7, y + 9, cellSize - 12, cellSize - 12);
                
                // Draw token
                g2d.setColor(playerColor);
                g2d.fillOval(x + 5, y + 5, cellSize - 10, cellSize - 10);
                
                // Draw token border
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawOval(x + 5, y + 5, cellSize - 10, cellSize - 10);
                
                // Draw player initial
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));
                String initial = entry.getKey().substring(0, 1).toUpperCase();
                FontMetrics tokenFm = g2d.getFontMetrics();
                int initialX = x + (cellSize - tokenFm.stringWidth(initial)) / 2;
                int initialY = y + (cellSize + tokenFm.getAscent()) / 2 - 2;
                g2d.drawString(initial, initialX, initialY);
                
                // Draw player name below board
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                index++;
            }
            
            // Draw legend
            drawLegend(g2d, offsetX, offsetY + boardHeight + 20);
        }
    }
    
    private void drawLegend(Graphics2D g2d, int startX, int startY) {
        if (positions == null) return;
        
        int index = 0;
        int legendX = startX;
        int legendY = startY;
        
        for (Map.Entry<String, Integer> entry : positions.entrySet()) {
            Color playerColor = PLAYER_COLORS[index % 4];
            Color borderColor = PLAYER_BORDERS[index % 4];
            
            // Draw token
            g2d.setColor(playerColor);
            g2d.fillOval(legendX, legendY, 20, 20);
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(legendX, legendY, 20, 20);
            
            // Draw name
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2d.drawString(entry.getKey(), legendX + 28, legendY + 15);
            
            legendX += 130;
            if (legendX > startX + 400) {
                legendX = startX;
                legendY += 30;
            }
            index++;
        }
    }
    
    private void drawStar(Graphics2D g2d, int centerX, int centerY, int outerRadius, int innerRadius) {
        int points = 5;
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        
        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI / 2 + (i * Math.PI / points);
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = (int) (centerX + radius * Math.cos(angle));
            yPoints[i] = (int) (centerY - radius * Math.sin(angle));
        }
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.fillPolygon(xPoints, yPoints, points * 2);
    }
    
    private boolean isSpecialPosition(int i, int j, int grid) {
        // Define special safe positions (simplified for demo)
        int mid = grid / 2;
        return (i == mid && j == 2) || (i == 2 && j == mid) || 
               (i == mid && j == grid - 3) || (i == grid - 3 && j == mid);
    }
    
    private boolean isPathCell(int i, int j, int grid) {
        int mid = grid / 2;
        // Main paths
        return (i == mid) || (j == mid) || 
               (i == mid - 1) || (i == mid + 1) || 
               (j == mid - 1) || (j == mid + 1);
    }
    
    private Color getHomeAreaColor(int i, int j, int grid) {
        int third = grid / 3;
        
        // Top-left (Red)
        if (i < third && j < third) {
            return new Color(255, 200, 200);
        }
        // Top-right (Blue)
        if (i < third && j > grid - third) {
            return new Color(200, 220, 255);
        }
        // Bottom-left (Green)
        if (i > grid - third && j < third) {
            return new Color(200, 255, 200);
        }
        // Bottom-right (Yellow)
        if (i > grid - third && j > grid - third) {
            return new Color(255, 255, 200);
        }
        
        return new Color(250, 250, 255);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(550, 550);
    }
}

