package server;

import java.io.*;
import java.net.*;
import model.*;

/**
 * Handles communication between the Ludo server and a single connected client.
 * Each client runs on its own thread. This class manages:
 * - Receiving player JOIN messages
 * - Listening for player actions (like dice rolls)
 * - Sending updates from the server
 * - Handling disconnections safely
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private GameManager gameManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Player player;
    private boolean connected = true;
    private boolean isDisconnecting = false;

    public ClientHandler(Socket socket, GameManager gameManager) {
        this.socket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            // Player joins
            Message joinMsg = (Message) in.readObject();
            player = new Player(joinMsg.getPlayerName());
            gameManager.addPlayer(player, this);

            System.out.println("[SERVER] Player joined: " + player.getName());

            // Continuous listening
            while (connected) {
                Message msg = (Message) in.readObject();
                gameManager.handleMessage(msg, player);
            }

        } catch (EOFException | SocketException e) {
            System.out.println("[SERVER] Player " +
                    (player != null ? player.getName() : "unknown") +
                    " disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    /** Sends a message from the server to this client. */
    public synchronized void sendMessage(Message msg) {
        if (out == null || isDisconnecting) return;
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            disconnect(); // safe now, won’t recurse
        }
    }

    /** Safely closes this client's connection and notifies others. */
    private void disconnect() {
        if (isDisconnecting) return;
        isDisconnecting = true;

        connected = false;
        try {
            if (player != null) {
                gameManager.removeClient(player.getName());
                gameManager.broadcast(
                        new Message("INFO", player.getName() + " has left the game.", "SERVER"));
            }
            socket.close();
        } catch (IOException ignored) {}
    }
}
