import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Collectors;

public class Launcher {

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

    public static Map.Entry<String, List<String>> getSmallerListSizeEntry(List<Map.Entry<String, List<String>>> entries) {
        Map.Entry<String, List<String>> smallerListSizeEntry = entries.get(0);
        int entriesSize = entries.size();
        Map.Entry<String, List<String>> loopEntry;
        for (int index = 1; index < entriesSize; index++) {
            loopEntry = entries.get(index);
            if (smallerListSizeEntry.getValue().size() > loopEntry.getValue().size()) {
                smallerListSizeEntry = loopEntry;
            }
        }
        return smallerListSizeEntry;
    }

    public static List<Map.Entry<String, List<String>>> getMostUsedEntries(List<String> listContent) {
        Map<String, List<String>> wordsMap = listContent.stream().collect(Collectors.groupingBy(string -> string));
        List<Map.Entry<String, List<String>>> mostUsedEntries = new ArrayList<Map.Entry<String, List<String>>>();
        Map.Entry<String, List<String>> smallerListSizeEntry;
        for (Map.Entry<String, List<String>> wordEntry : wordsMap.entrySet()) {
            if(mostUsedEntries.size() < 3) {
                mostUsedEntries.add(wordEntry);
            } else {
                smallerListSizeEntry = getSmallerListSizeEntry(mostUsedEntries);
                if (wordEntry.getValue().size() > smallerListSizeEntry.getValue().size()) {
                    mostUsedEntries.add(wordEntry);
                    mostUsedEntries.remove(smallerListSizeEntry);
                }
            }
        }
        return mostUsedEntries;
    }

    public static List<String> getMostUsedWords(List<String> contetList) {
        List<Map.Entry<String, List<String>>> mostUsedEntries = getMostUsedEntries(contetList);
        List<String> mostUsedWords = new ArrayList<String>();
        for (Map.Entry<String, List<String>> wordEntry : mostUsedEntries) {
            mostUsedWords.add(wordEntry.getKey());
        }
        return mostUsedWords;
    }
    public static void printWords(List<String> words) {
        System.out.print(words.get(0));
        int words_size = words.size();
        for (int index = 1; index < words_size; index++) {
            System.out.print(" " + words.get(index));
        }
        System.out.println();
    }

    public static void printMostUsedWords(Scanner stdin) {
        System.out.println("Give me the path to your file:");
        String pathToFile = stdin.nextLine();
        Path filePath = Paths.get(pathToFile);
        try {
            String fileContent = Files.readString(filePath);
            List<String> contentList = new ArrayList<String>(Arrays.asList(fileContent.split(" |\n")));
            List<String> mostUsedWords = getMostUsedWords(contentList);
            printWords(mostUsedWords);
        }
        catch(IOException error) {
            String error_message = String.format("Unreadable file: %s", error);
            System.err.println(error_message);
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to my brand new amazing program!");
        Scanner stdin = new Scanner(System.in);
        boolean shouldContinue = true;
        while (shouldContinue) {
            System.out.println("Enter a command:");
            String userInput = stdin.nextLine();
            switch(userInput) {
                case "quit":
                    shouldContinue = false;
                    break;
                case "fibo": 
                    printFibonacci(stdin);
                    break;
                case "freq": 
                    printMostUsedWords(stdin);
                    break;
                default: 
                    System.out.println("Unknown command");
                    break;
            }
        }
        stdin.close();
    }
}
