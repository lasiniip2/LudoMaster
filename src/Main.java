import java.util.Scanner;
import server.LudoServer;
import client.LudoClient;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ludo Network Game ===");
        System.out.println("1. Start Server");
        System.out.println("2. Start Client");
        System.out.print("Enter choice: ");
         
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        try {
            if (choice == 1) {
                // Run server
                LudoServer.main(new String[]{});
            } else if (choice == 2) {
                // Run client
                LudoClient.main(new String[]{});
            } else {
                System.out.println("Invalid choice!");
            }
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
