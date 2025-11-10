package server;


import java.util.Random;

public class BotPlayer implements Runnable {
    private String name;
    private GameManager gameManager;
    private Random rand = new Random();

    public BotPlayer(String name, GameManager gameManager) {
        this.name = name;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
                if (gameManager.isBotTurn(name)) {
                    int dice = rand.nextInt(6) + 1;
                    gameManager.botMove(name, dice);
                }
            } catch (InterruptedException e) {
                break;
            } catch (Exception ignored) {}
        }
    }

    public String getName() {
        return name;
    }
}
