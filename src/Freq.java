import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Collectors;

public class Freq implements Command {
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

    public String name() {
        return "freq";
    }
    public boolean run(Scanner stdin) {
        printMostUsedWords(stdin);
        return false;
    }
}
