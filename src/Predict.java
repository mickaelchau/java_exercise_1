import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.io.IOException;

public class Predict implements Command {
    public String name() {
        return "predict";
    }

    public int predictLimit = 20;

    public List<Entry<String, Integer>> getEntriesAfter(List<String> fileContent, String word) {
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
        List<Entry<String, Integer>> wordsEntryList = new ArrayList<>(wordsUsedAfter.entrySet());
		wordsEntryList.sort(Entry.comparingByValue());
        Collections.reverse(wordsEntryList);
        return wordsEntryList;
    }
/*
    public List<Entry<String, Integer>> sortEntriesByKey(List<Entry<String, Integer>> wordsEntries, List<String> fileContent) {
        List<Entry<String, Integer>> wordsWithSameNumber = new ArrayList<String>();
        List<String> finalWordsWithSameNumber =  new ArrayList<String>();
        Integer occurences = -1;
        for (Entry entry : wordsEntries) {
            if (occurences == -1 && entry.getValue() != -1) {
                wordsWithSameNumber.add(entry.getKey());
                occurences = entry.getValue();
            } else if (occurences == entry.getValue()){
                wordsWithSameNumber.add(entry);
            }
            entry.setValue(-1);
        }
            
            for (String word : fileContent) {
                if (wordsWithSameNumber.contains(word)) {
                    finalWordsWithSameNumber.Add(word);
                }
            }
    }*/

    public static List<String> getMaxWordsAfter(List<Entry<String, Integer>> entriesPredicted, Integer max) {
        List<String> maxWordsAfter = new ArrayList<String>();
        for (Entry<String, Integer> entry : entriesPredicted) {
            if (entry.getValue() == max) {
                maxWordsAfter.add(entry.getKey());
            }
        }
        return maxWordsAfter;
    }

    public List<String> sortByFirstOccurence(List<String> words, List<String>contentList) {
        List<String> sortedWords = new ArrayList<String>();
        for (String contentWord : contentList) {
            if (words.contains(contentWord) && !sortedWords.contains(contentWord)) {
                sortedWords.add(contentWord);
            }
        }
        return sortedWords;
    }

    public String findNextWord(List<String> allMatchingWords, List<String> resultString) {
        String wordChoosen = "";
        for (String word : allMatchingWords) {
            if (!resultString.contains(word)) {
                wordChoosen = word;
                break;
            }
        }
        return wordChoosen;
    }

    public void print_prediction(List<String> prediction) {
        int lastElementIndex = prediction.size() - 1;
        System.out.print(prediction.get(0));
        for (int index = 1; index < lastElementIndex; index++) {
            System.out.print(" " + prediction.get(index));
        }
        System.out.println();
    }

    public boolean run(Scanner stdin) {
        System.out.println("Give me the path to your file:");
        String pathToFile = stdin.nextLine();
        Path filePath = Paths.get(pathToFile);
        try {
            String fileContent = Files.readString(filePath);
            List<String> contentList = new ArrayList<String>(Arrays.asList(fileContent.split(" |\n")));
            contentList.forEach(word -> word.toLowerCase());
            System.out.println("Give a word to predict:");
            String wordToPredictAfter = stdin.nextLine();
            List<String> resultString = new ArrayList<String>();
            resultString.add(wordToPredictAfter);
            int index = 0;
            for (; index < predictLimit; index++) {
                List<Entry<String, Integer>> entriesPredicted = getEntriesAfter(contentList, wordToPredictAfter);
                Optional<Entry<String, Integer>> maxEntryOptional = entriesPredicted.stream().max(Comparator.comparing(entry -> entry.getValue()));
                if (maxEntryOptional.isEmpty()) {
                    break;
                }
                Entry<String, Integer> maxValueEntry = maxEntryOptional.get(); 
                Integer maxWordOccurence = maxValueEntry.getValue();
                List<String> allMatchingWords = getMaxWordsAfter(entriesPredicted, maxWordOccurence);
                entriesPredicted.removeIf(s -> s.getValue().equals(maxWordOccurence));
                List<String> sortedMatchingWords = sortByFirstOccurence(allMatchingWords, contentList);
                String findWord = findNextWord(sortedMatchingWords, resultString);
                resultString.add(findWord);
                wordToPredictAfter = findWord;
            }
            if (index == 0) {
                System.err.println("Word not found: You have tried to predict a non-existing word");
            } 
            else {
                print_prediction(resultString);
            }
        }
        catch(IOException error) {
            String error_message = String.format("Unreadable file: %s", error);
            System.err.println(error_message);
        }
        return false;
    }
}
