package server;

import model.*;
import java.io.IOException;
import java.util.*;

/**
 * The GameManager class manages all server-side game logic:
 * - Tracks all players and bots
 * - Manages game state and turns
 * - Broadcasts updates to clients
 * - Handles dice rolls and player moves
 */
public class GameManager {
    private GameState state = new GameState();
    private Map<String, ClientHandler> clients = new HashMap<>();
    private List<BotPlayer> bots = new ArrayList<>();

    /** Adds a player to the game and notifies everyone. */
    public synchronized void addPlayer(Player player, ClientHandler handler) throws IOException {
        state.addPlayer(player);
        clients.put(player.getName(), handler);
        broadcast(new Message("INFO", player.getName() + " joined!", "SERVER"));
    }

    /** Removes a player/client when disconnected. */
    public synchronized void removeClient(String playerName) {
        clients.remove(playerName);
        state.getPlayers().remove(playerName);
        System.out.println("[SERVER] Removed player: " + playerName);
    }

    /** Adds a bot player to the game. */
    public synchronized void addBot(BotPlayer bot) {
        Player p = new Player(bot.getName());
        state.addPlayer(p);
        bots.add(bot);
        System.out.println("[SERVER] Added bot: " + bot.getName());
    }

    /** Starts the game after all players have joined. */
    public synchronized void startGame() throws IOException {
        broadcast(new Message("INFO", "Game Started with " + state.getPlayers().size() + " players!", "SERVER"));
        updateAll();
        nextTurn();
    }

    /** Handles a message received from a client. */
    public synchronized void handleMessage(Message msg, Player p) throws IOException {
        if (msg.getType().equals("ROLL") && p.getName().equals(state.getCurrentTurn())) {
            int dice = (int) (Math.random() * 6 + 1);
            movePlayer(p.getName(), dice);
        }
    }

    /** Called when a bot makes a move. */
    public synchronized void botMove(String botName, int dice) throws IOException {
        if (botName.equals(state.getCurrentTurn())) {
            movePlayer(botName, dice);
        }
    }

    /** Moves a player according to their dice roll and updates the game state. */
    private void movePlayer(String name, int dice) throws IOException {
        state.movePlayer(name, dice);
        broadcast(new Message(
                "MOVE",
                name + " rolled " + dice + " (pos: " + state.getPlayers().get(name).getPosition() + ")",
                "SERVER"
        ));
        updateAll();
        nextTurn();
    }

    /** Moves to the next player's turn. */
    private void nextTurn() throws IOException {
        String current = state.getCurrentTurn();
        broadcast(new Message("INFO", "It's " + current + "'s turn!", "SERVER"));
        if (clients.containsKey(current)) {
            clients.get(current).sendMessage(new Message("YOUR_TURN", "", "SERVER"));
        }
    }

    /** Checks if it’s a bot’s turn. */
    public synchronized boolean isBotTurn(String name) {
        return name.equals(state.getCurrentTurn());
    }

    /** Sends updated player positions to all clients. */
    private void updateAll() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (var entry : state.getPlayers().entrySet()) {
            sb.append(entry.getKey()).append("=")
              .append(entry.getValue().getPosition()).append(",");
        }
        for (ClientHandler ch : clients.values()) {
            ch.sendMessage(new Message("STATE", sb.toString(), "SERVER"));
        }
    }

    /** Broadcasts a message to all connected clients. */
    public synchronized void broadcast(Message msg) {
        // Avoid sending to disconnected clients
        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            try {
                entry.getValue().sendMessage(msg);
            } catch (Exception e) {
                toRemove.add(entry.getKey());
            }
        }
        // Clean up any failed clients
        for (String name : toRemove) {
            removeClient(name);
        }
    }
}
