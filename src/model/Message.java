package model;

import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private String content;
    private String playerName;

    public Message(String type, String content, String playerName) {
        this.type = type;
        this.content = content;
        this.playerName = playerName;
    }

    public String getType() { return type; }
    public String getContent() { return content; }
    public String getPlayerName() { return playerName; }
}
