package client;

import model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class LudoBoardUI extends JFrame {
    private JLabel infoLabel;
    private JButton rollDiceButton;
    private ObjectOutputStream out;
    private String playerName;
    @SuppressWarnings("unused")
    private Map<String, Integer> positions = new HashMap<>();
    private BoardPanel boardPanel;

    public LudoBoardUI(String playerName, ObjectOutputStream out) {
        this.playerName = playerName;
        this.out = out;

        setTitle("🎲 Ludo Game - " + playerName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        infoLabel = new JLabel("Waiting for game to start...", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(infoLabel, BorderLayout.NORTH);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        rollDiceButton = new JButton("🎲 Roll Dice");
        rollDiceButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollDiceButton.setEnabled(false);
        rollDiceButton.addActionListener((ActionEvent e) -> rollDice());
        add(rollDiceButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void rollDice() {
        try {
            out.writeObject(new Message("ROLL", "", playerName));
            out.flush();
            rollDiceButton.setEnabled(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error sending roll: " + e.getMessage());
        }
    }

    public void updatePositionsFromState(String stateText) {
        Map<String, Integer> newPos = new HashMap<>();
        try {
            String[] parts = stateText.split(",");
            for (String p : parts) {
                if (p.contains("=")) {
                    String[] kv = p.trim().split("=");
                    newPos.put(kv[0], Integer.parseInt(kv[1]));
                }
            }
            positions = newPos;
            boardPanel.updatePositions(newPos);
        } catch (Exception ignored) {}
    }

    public void showMessage(String msg) {
        infoLabel.setText(msg);
    }

    public void enableRoll(boolean enable) {
        rollDiceButton.setEnabled(enable);
    }
}
