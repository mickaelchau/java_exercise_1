import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

public class Predict implements Command {
    public String name() {
        return "predict";
    }

    public int predictLimit = 20;

    public HashMap<String, Integer> getWordsAfter(List<String> fileContent, String word) {
        HashMap<String, Integer> wordsUsedAfter = new HashMap<String, Integer>();
        int fileContentLastElementIndex = fileContent.size() - 1;
        for (int index = 0; index < fileContentLastElementIndex; index++) {
            if (fileContent.get(index).compareTo(word) == 0) {
                String wordAfter = fileContent.get(index + 1);
                if (wordsUsedAfter.containsKey(wordAfter)) {
                    wordsUsedAfter.put(wordAfter, wordsUsedAfter.get(wordAfter) + 1);
                } else {
                    wordsUsedAfter.put(wordAfter, 1);
                }
            }
        }
        return wordsUsedAfter;
    }

    public String getBiggerKey(HashMap<String, Integer> wordsUsedAfter) {
        int max_value = 0;
        String max_key = "\0";
        for (HashMap.Entry<String, Integer> entry : wordsUsedAfter.entrySet()) {
            if (entry.getValue() > max_value) {
                max_value = entry.getValue();
                max_key = entry.getKey();
            }
        }
        return max_key;
    }

    public boolean run(Scanner stdin) {
        System.out.println("Give me the path to your file:");
        String pathToFile = stdin.nextLine();
        Path filePath = Paths.get(pathToFile);
        try {
            String fileContent = Files.readString(filePath);
            List<String> contentList = new ArrayList<String>(Arrays.asList(fileContent.split(" |\n")));
            System.out.println("Give a word to predict:");
            String wordToPredictAfter = stdin.nextLine();
            String wordAfter;
            HashMap<String, Integer> wordsAfter;
            System.out.print(wordToPredictAfter);
            int index = 0;
            for (; index < predictLimit; index++) {
                wordsAfter = getWordsAfter(contentList, wordToPredictAfter);
                wordAfter = getBiggerKey(wordsAfter);
                if (wordAfter.compareTo("") == 0) {
                    break;
                } 
                else {
                    wordToPredictAfter = wordAfter;
                    System.out.print(" "+ wordAfter);
                }
            }
            System.out.println();
            if (index == 0) {
                System.err.println("Word not found: You have tried to predict a non-existing word");
            }
        }
        catch(IOException error) {
            String error_message = String.format("Unreadable file: %s", error);
            System.err.println(error_message);
        }
        return false;
    }
}
