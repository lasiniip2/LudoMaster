package client;

import model.Message;
import util.Constants;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LudoClient {
    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        Scanner sc = null;

        try {
            socket = new Socket(Constants.HOST, Constants.SERVER_PORT);

            // Create streams in correct order
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            sc = new Scanner(System.in);

            System.out.print("Enter your player name: ");
            String name = sc.nextLine();

            // Send join message
            out.writeObject(new Message("JOIN", "", name));
            out.flush();

            // Launch UI
            LudoBoardUI ui = new LudoBoardUI(name, out);

            // Thread to listen to server
            ObjectInputStream finalIn = in;
            new Thread(() -> {
                try {
                    while (true) {
                        Message msg = (Message) finalIn.readObject();
                        switch (msg.getType()) {
                            case "INFO":
                                ui.showMessage(msg.getContent());
                                break;
                            case "STATE":
                                ui.updatePositionsFromState(msg.getContent());
                                break;
                            case "YOUR_TURN":
                                ui.showMessage("Your turn! Click 🎲 Roll Dice");
                                ui.enableRoll(true);
                                break;
                            case "MOVE":
                                ui.showMessage(msg.getContent());
                                break;
                        }
                    }
                } catch (Exception e) {
                    ui.showMessage("Disconnected from server.");
                }
            }).start();

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
