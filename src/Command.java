import java.util.Scanner;

public interface Command {
    public abstract String name();
    public abstract boolean run(Scanner stdin);
}
