import java.util.HashMap;
import java.util.Scanner;

public class AuthSystem {

    // Mock user database
    private static final HashMap<String, String> users = new HashMap<>();

    // Initialize users
    static {
        users.put("admin", "admin123");
        users.put("user", "user123");
        users.put("guest", "guest");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to the Java Authentication System ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (authenticate(username, password)) {
            System.out.println("✅ Login successful! Welcome, " + username + "!");
        } else {
            System.out.println("❌ Invalid username or password. Access denied.");
        }

        scanner.close();
    }

    // Auth check
    private static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
