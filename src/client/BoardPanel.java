package client;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class BoardPanel extends JPanel {
    private Map<String, Integer> positions;
    private final Color[] playerColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

    public void updatePositions(Map<String, Integer> pos) {
        this.positions = pos;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = 25;
        int grid = 15;

        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        if (positions != null) {
            int index = 0;
            for (Map.Entry<String, Integer> entry : positions.entrySet()) {
                int pos = entry.getValue() % (grid * grid);
                int x = (pos % grid) * cellSize;
                int y = (pos / grid) * cellSize;

                g.setColor(playerColors[index % 4]);
                g.fillOval(x + 5, y + 5, cellSize - 10, cellSize - 10);

                g.setColor(Color.BLACK);
                g.drawString(entry.getKey(), x + 2, y + cellSize - 2);
                index++;
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
}
