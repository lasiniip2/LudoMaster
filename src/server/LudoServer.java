package server;

import util.Constants;
import java.net.*;
import java.util.*;

public class LudoServer {
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final GameManager gameManager = new GameManager();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            System.out.println("=== LUDO SERVER STARTED ===");
            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter number of players (1–4): ");
            int playerCount = Math.max(1, Math.min(4, sc.nextInt()));

            System.out.println("Waiting for " + playerCount + " player(s) to connect...");

            while (clients.size() < playerCount) {
                Socket socket = serverSocket.accept();
                System.out.println("Player connected from: " + socket.getInetAddress());
                ClientHandler handler = new ClientHandler(socket, gameManager);
                clients.add(handler);
                new Thread(handler).start();
            }

            // Add bots if needed
            int botsToAdd = 4 - playerCount;
            for (int i = 1; i <= botsToAdd; i++) {
                BotPlayer bot = new BotPlayer("BOT-" + i, gameManager);
                gameManager.addBot(bot);
                new Thread(bot).start();
            }

            // Wait 2 seconds to ensure clients finish JOIN handshake
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }

            gameManager.startGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
