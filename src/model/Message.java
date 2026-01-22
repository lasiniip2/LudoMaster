package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Message types
    public static final String JOIN = "JOIN";
    public static final String INFO = "INFO";
    public static final String STATE = "STATE";
    public static final String YOUR_TURN = "YOUR_TURN";
    public static final String MOVE = "MOVE";
    public static final String ROLL = "ROLL";
    public static final String PUBLIC_CHAT = "PUBLIC_CHAT";
    public static final String PRIVATE_CHAT = "PRIVATE_CHAT";
    public static final String USER_LIST = "USER_LIST";
    public static final String TYPING = "TYPING";
    
    private String type;
    private String content;
    private String playerName;
    private String recipient;  // For private chat messages
    private LocalDateTime timestamp;
    private String[] userList; // For user list updates
    
    // Constructor for game messages (backward compatible)
    public Message(String type, String content, String playerName) {
        this.type = type;
        this.content = content;
        this.playerName = playerName;
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor for private chat messages
    public Message(String type, String content, String playerName, String recipient) {
        this.type = type;
        this.content = content;
        this.playerName = playerName;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getType() { return type; }
    public String getContent() { return content; }
    public String getPlayerName() { return playerName; }
    public String getRecipient() { return recipient; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String[] getUserList() { return userList; }
    
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public void setUserList(String[] userList) { this.userList = userList; }
    
    public String getFormattedTimestamp() {
        if (timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return timestamp.format(formatter);
        }
        return "";
    }
    
    public String getFormattedDate() {
        if (timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return timestamp.format(formatter);
        }
        return "";
    }
}
