import java.util.*;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Welcome to all IN1 Students");
        Scanner stdin = new Scanner(System.in);
        String user_input = stdin.next();
        if (user_input.compareTo("quit") != 0)
            System.out.println("Unknown command");
        stdin.close();
    }
}