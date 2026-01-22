package client;

import model.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Ludo Board UI with integrated chat functionality
 * Features modern dark theme with blue accents, split layout with game board and chat
 */
public class LudoBoardUI extends JFrame {
    private JLabel infoLabel;
    private JButton rollDiceButton;
    private ObjectOutputStream out;
    private String playerName;
    @SuppressWarnings("unused")
    private Map<String, Integer> positions = new HashMap<>();
    private BoardPanel boardPanel;
    private ChatPanel chatPanel;
    
    // Modern Blue Theme Colors
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color PRIMARY_HOVER = new Color(0, 105, 217);
    private static final Color DARK_BG = new Color(18, 18, 28);
    private static final Color PANEL_BG = new Color(25, 30, 48);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(64, 169, 255);

    public LudoBoardUI(String playerName, ObjectOutputStream out) {
        this.playerName = playerName;
        this.out = out;

        setTitle("🎲 Ludo Game - " + playerName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);

        // Create main layout with modern design
        initializeComponents();

        setSize(1200, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Top panel - Game info and controls
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Split between game board and chat
        JSplitPane splitPane = createMainSplitPane();
        add(splitPane, BorderLayout.CENTER);
        
        // Bottom panel - Dice roll button
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Left side - Title
        JLabel titleLabel = new JLabel("🎲 Ludo Master");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Center - Info label
        infoLabel = new JLabel("Waiting for game to start...", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoLabel.setForeground(ACCENT_COLOR);
        panel.add(infoLabel, BorderLayout.CENTER);
        
        // Right side - Player name
        JLabel playerLabel = new JLabel("Player: " + playerName);
        playerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        playerLabel.setForeground(TEXT_COLOR);
        panel.add(playerLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JSplitPane createMainSplitPane() {
        // Left side - Game board
        JPanel boardContainer = new JPanel(new BorderLayout());
        boardContainer.setBackground(DARK_BG);
        boardContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        boardPanel = new BoardPanel();
        boardContainer.add(boardPanel, BorderLayout.CENTER);
        
        // Right side - Chat panel
        chatPanel = new ChatPanel(playerName, out);
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardContainer, chatPanel);
        splitPane.setDividerLocation(600);
        splitPane.setDividerSize(3);
        splitPane.setResizeWeight(0.6); // Give 60% to game board
        splitPane.setBorder(null);
        splitPane.setBackground(DARK_BG);
        
        return splitPane;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 20, 15, 20));
        
        // Dice roll button with modern styling
        rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        rollDiceButton.setBackground(PRIMARY_COLOR);
        rollDiceButton.setForeground(Color.WHITE);
        rollDiceButton.setFocusPainted(false);
        rollDiceButton.setBorderPainted(false);
        rollDiceButton.setPreferredSize(new Dimension(180, 50));
        rollDiceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rollDiceButton.setEnabled(false);
        
        // Hover effect
        rollDiceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (rollDiceButton.isEnabled()) {
                    rollDiceButton.setBackground(PRIMARY_HOVER);
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rollDiceButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        rollDiceButton.addActionListener((ActionEvent e) -> rollDice());
        panel.add(rollDiceButton);
        
        return panel;
    }

    private void rollDice() {
        try {
            out.writeObject(new Message(Message.ROLL, "", playerName));
            out.flush();
            rollDiceButton.setEnabled(false);
            showMessage("Rolling dice...");
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
        SwingUtilities.invokeLater(() -> {
            infoLabel.setText(msg);
        });
    }

    public void enableRoll(boolean enable) {
        SwingUtilities.invokeLater(() -> {
            rollDiceButton.setEnabled(enable);
            if (enable) {
                rollDiceButton.setBackground(PRIMARY_COLOR);
            } else {
                rollDiceButton.setBackground(new Color(100, 100, 120));
            }
        });
    }
    
    public void receiveChatMessage(Message msg) {
        chatPanel.receiveMessage(msg);
    }
    
    public void updateUserList(String[] users) {
        chatPanel.updateUserList(users);
    }
}

