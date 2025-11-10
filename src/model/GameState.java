package model;

import java.io.Serializable;
import java.util.*;

public class GameState implements Serializable {
    private Map<String, Player> players = new LinkedHashMap<>();
    private String currentTurn;

    public void addPlayer(Player p) {
        players.put(p.getName(), p);
        if (currentTurn == null) currentTurn = p.getName();
    }

    public synchronized void movePlayer(String playerName, int dice) {
        players.get(playerName).move(dice);
        nextTurn();
    }

    private void nextTurn() {
        List<String> keys = new ArrayList<>(players.keySet());
        int i = keys.indexOf(currentTurn);
        currentTurn = keys.get((i + 1) % keys.size());
    }

    public Map<String, Player> getPlayers() { return players; }
    public String getCurrentTurn() { return currentTurn; }
}
