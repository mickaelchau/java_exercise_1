import java.util.*;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Welcome to my brand new amazing program!");
        Scanner stdin = new Scanner(System.in);
        boolean shouldStop = false;
        List<Command> commands = Arrays.asList(new Fibo(), new Quit(), new Freq(), new Predict());        
        while (!shouldStop) {
            //System.out.println("Enter a command:");
            String userInput = stdin.nextLine();
            boolean commandRun = false;
            for (Command command : commands) {
                if (command.name().compareTo(userInput) == 0) {
                    shouldStop = command.run(stdin);
                    commandRun = true;
                    break;
                }
            }
            if (!commandRun) {
                System.out.println("Unknown command");
            }
        }
        stdin.close();
    }
}
