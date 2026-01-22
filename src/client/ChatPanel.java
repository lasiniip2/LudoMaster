package client;

import model.Message;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * Modern Chat Panel for Ludo Game
 * Supports both group chat and private messaging between players
 */
public class ChatPanel extends JPanel {
    
    private String username;
    private ObjectOutputStream out;
    private Map<String, List<ChatMessage>> chatHistory;
    private String currentChat = "Everyone"; // Default to group chat
    
    // UI Components
    private JTabbedPane tabbedPane;
    private JPanel groupChatPanel;
    private Map<String, JPanel> privateChatPanels;
    private JPanel groupMessagesPanel;
    private Map<String, JPanel> privateMessagesPanels;
    private JTextArea messageInput;
    private JButton sendButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    
    // Modern Blue Theme Colors
    private static final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private static final Color PRIMARY_HOVER = new Color(0, 105, 217);
    private static final Color DARK_BG = new Color(18, 18, 28);
    private static final Color PANEL_BG = new Color(25, 30, 48);
    private static final Color CHAT_BG = new Color(30, 35, 55);
    private static final Color SENT_BUBBLE = new Color(0, 123, 255);
    private static final Color RECEIVED_BUBBLE = new Color(45, 52, 75);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT = new Color(160, 170, 190);
    private static final Color DIVIDER_COLOR = new Color(40, 45, 65);
    
    public ChatPanel(String username, ObjectOutputStream out) {
        this.username = username;
        this.out = out;
        this.chatHistory = new HashMap<>();
        this.privateChatPanels = new HashMap<>();
        this.privateMessagesPanels = new HashMap<>();
        
        chatHistory.put("Everyone", new ArrayList<>());
        
        setLayout(new BorderLayout());
        setBackground(PANEL_BG);
        
        initComponents();
    }
    
    private void initComponents() {
        // Main split: Left (user list) + Right (chat area)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(180);
        splitPane.setDividerSize(2);
        splitPane.setBorder(null);
        splitPane.setBackground(PANEL_BG);
        
        // Left Panel - User List
        JPanel leftPanel = createUserListPanel();
        splitPane.setLeftComponent(leftPanel);
        
        // Right Panel - Chat with tabs
        JPanel rightPanel = createChatPanel();
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createUserListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header
        JLabel header = new JLabel("Players");
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setForeground(TEXT_COLOR);
        header.setBorder(new EmptyBorder(5, 5, 10, 5));
        panel.add(header, BorderLayout.NORTH);
        
        // User list
        userListModel = new DefaultListModel<>();
        userListModel.addElement("Everyone");
        
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setBackground(PANEL_BG);
        userList.setForeground(TEXT_COLOR);
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userList.setBorder(new EmptyBorder(5, 5, 5, 5));
        userList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(8, 10, 8, 10));
                label.setOpaque(true);
                
                if (isSelected) {
                    label.setBackground(PRIMARY_COLOR);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(PANEL_BG);
                    label.setForeground(TEXT_COLOR);
                }
                
                if (value.equals("Everyone")) {
                    label.setText("👥 " + value);
                } else {
                    label.setText("👤 " + value);
                }
                
                return label;
            }
        });
        
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = userList.getSelectedValue();
                if (selected != null) {
                    switchChat(selected);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_BG);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CHAT_BG);
        
        // Messages area (will hold different chat panels)
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(PANEL_BG);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Create group chat panel
        groupChatPanel = createMessagePanel();
        groupMessagesPanel = (JPanel) ((JScrollPane) groupChatPanel.getComponent(1)).getViewport().getView();
        tabbedPane.addTab("👥 Group Chat", groupChatPanel);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        // Input area
        JPanel inputPanel = createInputPanel();
        panel.add(inputPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CHAT_BG);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PANEL_BG);
        headerPanel.setBorder(new EmptyBorder(12, 15, 12, 15));
        
        JLabel chatLabel = new JLabel(currentChat);
        chatLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chatLabel.setForeground(TEXT_COLOR);
        headerPanel.add(chatLabel, BorderLayout.WEST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Messages panel
        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(CHAT_BG);
        messagesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CHAT_BG);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(PANEL_BG);
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));
        
        // Message input
        messageInput = new JTextArea(2, 30);
        messageInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageInput.setLineWrap(true);
        messageInput.setWrapStyleWord(true);
        messageInput.setBackground(DARK_BG);
        messageInput.setForeground(TEXT_COLOR);
        messageInput.setCaretColor(TEXT_COLOR);
        messageInput.setBorder(new EmptyBorder(10, 12, 10, 12));
        
        // Placeholder
        String placeholder = "Type a message...";
        messageInput.setText(placeholder);
        messageInput.setForeground(SECONDARY_TEXT);
        
        messageInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageInput.getText().equals(placeholder)) {
                    messageInput.setText("");
                    messageInput.setForeground(TEXT_COLOR);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (messageInput.getText().trim().isEmpty()) {
                    messageInput.setText(placeholder);
                    messageInput.setForeground(SECONDARY_TEXT);
                }
            }
        });
        
        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendMessage();
                }
            }
        });
        
        JScrollPane inputScroll = new JScrollPane(messageInput);
        inputScroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DIVIDER_COLOR, 2),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        panel.add(inputScroll, BorderLayout.CENTER);
        
        // Send button
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(PRIMARY_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setPreferredSize(new Dimension(80, 50));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        sendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(PRIMARY_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        sendButton.addActionListener(e -> sendMessage());
        panel.add(sendButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void sendMessage() {
        String text = messageInput.getText().trim();
        if (text.isEmpty() || text.equals("Type a message...")) {
            return;
        }
        
        try {
            Message msg;
            if (currentChat.equals("Everyone")) {
                msg = new Message(Message.PUBLIC_CHAT, text, username);
            } else {
                msg = new Message(Message.PRIVATE_CHAT, text, username, currentChat);
            }
            
            out.writeObject(msg);
            out.flush();
            
            // Don't add locally - server will echo back to avoid duplicates
            
            messageInput.setText("");
            messageInput.setForeground(TEXT_COLOR);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error sending message: " + e.getMessage());
        }
    }
    
    public void receiveMessage(Message msg) {
        SwingUtilities.invokeLater(() -> {
            String sender = msg.getPlayerName();
            String content = msg.getContent();
            
            if (msg.getType().equals(Message.PUBLIC_CHAT)) {
                addMessageToChat("Everyone", sender, content, sender.equals(username));
            } else if (msg.getType().equals(Message.PRIVATE_CHAT)) {
                String chatKey = sender.equals(username) ? msg.getRecipient() : sender;
                addMessageToChat(chatKey, sender, content, sender.equals(username));
            }
        });
    }
    
    private void addMessageToChat(String chatKey, String sender, String content, boolean isSent) {
        if (!chatHistory.containsKey(chatKey)) {
            chatHistory.put(chatKey, new ArrayList<>());
        }
        
        ChatMessage chatMsg = new ChatMessage(sender, content, LocalDateTime.now(), isSent);
        chatHistory.get(chatKey).add(chatMsg);
        
        // Update UI
        JPanel messagesPanel = chatKey.equals("Everyone") ? groupMessagesPanel : privateMessagesPanels.get(chatKey);
        if (messagesPanel != null) {
            addMessageBubble(messagesPanel, chatMsg);
        }
    }
    
    private void addMessageBubble(JPanel messagesPanel, ChatMessage msg) {
        JPanel bubblePanel = new JPanel();
        bubblePanel.setLayout(new BoxLayout(bubblePanel, BoxLayout.Y_AXIS));
        bubblePanel.setOpaque(false);
        bubblePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Message bubble
        JPanel bubble = new JPanel(new BorderLayout(8, 2));
        bubble.setBackground(msg.isSent ? SENT_BUBBLE : RECEIVED_BUBBLE);
        bubble.setBorder(new EmptyBorder(10, 14, 10, 14));
        
        // Sender name (if not sent by current user)
        if (!msg.isSent) {
            JLabel senderLabel = new JLabel(msg.sender);
            senderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            senderLabel.setForeground(PRIMARY_COLOR);
            bubble.add(senderLabel, BorderLayout.NORTH);
        }
        
        // Message content
        JTextArea contentArea = new JTextArea(msg.content);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setForeground(TEXT_COLOR);
        contentArea.setBackground(msg.isSent ? SENT_BUBBLE : RECEIVED_BUBBLE);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setFocusable(false);
        contentArea.setBorder(null);
        bubble.add(contentArea, BorderLayout.CENTER);
        
        // Timestamp
        JLabel timeLabel = new JLabel(msg.getFormattedTime());
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(SECONDARY_TEXT);
        bubble.add(timeLabel, BorderLayout.SOUTH);
        
        // Wrapper for alignment
        JPanel wrapper = new JPanel(new FlowLayout(msg.isSent ? FlowLayout.RIGHT : FlowLayout.LEFT, 5, 0));
        wrapper.setOpaque(false);
        wrapper.add(bubble);
        
        bubblePanel.add(wrapper);
        messagesPanel.add(bubblePanel);
        messagesPanel.revalidate();
        
        // Auto scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    private void switchChat(String chatName) {
        currentChat = chatName;
        
        if (chatName.equals("Everyone")) {
            tabbedPane.setSelectedIndex(0);
        } else {
            // Check if private chat tab exists, if not create it
            if (!privateChatPanels.containsKey(chatName)) {
                createPrivateChatTab(chatName);
            }
            
            // Switch to that tab
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (tabbedPane.getTitleAt(i).contains(chatName)) {
                    tabbedPane.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    private void createPrivateChatTab(String playerName) {
        JPanel chatPanel = createMessagePanel();
        JPanel messagesPanel = (JPanel) ((JScrollPane) chatPanel.getComponent(1)).getViewport().getView();
        
        privateChatPanels.put(playerName, chatPanel);
        privateMessagesPanels.put(playerName, messagesPanel);
        chatHistory.put(playerName, new ArrayList<>());
        
        tabbedPane.addTab("👤 " + playerName, chatPanel);
    }
    
    public void updateUserList(String[] users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            userListModel.addElement("Everyone");
            
            for (String user : users) {
                if (!user.equals(username)) {
                    userListModel.addElement(user);
                }
            }
        });
    }
    
    // Inner class for chat message
    private static class ChatMessage {
        String sender;
        String content;
        LocalDateTime timestamp;
        boolean isSent;
        
        ChatMessage(String sender, String content, LocalDateTime timestamp, boolean isSent) {
            this.sender = sender;
            this.content = content;
            this.timestamp = timestamp;
            this.isSent = isSent;
        }
        
        String getFormattedTime() {
            int hour = timestamp.getHour();
            int minute = timestamp.getMinute();
            return String.format("%02d:%02d", hour, minute);
        }
    }
}
