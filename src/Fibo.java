import java.util.Scanner;

public class Fibo implements Command {
    public static int fibonacci(int n) {
        if (n == 0) {
            return 0;
        }
        int f0 = 0;
        int f1 = 1;
        int transi = 0;
        for (int i = 1; i < n; i++) {
            transi = f1;
            f1 = f0 + f1;
            f0 = transi;
        }
        return f1;
    }

    public static void printFibonacci(Scanner stdin) {
        int n = -1;
        while (n < 0) {
            System.out.println("Enter a positive integer:");
            n = stdin.nextInt(); 
            stdin.nextLine();
        }
        int fibo_n = fibonacci(n);
        String fibo_result = String.format("fibo(%d) = %d",n, fibo_n);
        System.out.println(fibo_result);
    }

    public String name() {
        return "fibo";
    }

    public boolean run(Scanner stdin) {
        printFibonacci(stdin);
        return false;
    }
}
