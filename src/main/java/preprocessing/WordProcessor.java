package preprocessing;

import common.FileHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WordProcessor {

    private final FileHandler fileHandler;

    public WordProcessor() {
        this.fileHandler = new FileHandler();
    }

    public void addNewWord() {
        try {
            System.out.println("Please enter the new word or enter 0 to exit.");
            String newWord = System.console().readLine();

            if (newWord.equals("0")) {
                return;
            }

            if(hasNotMoreThanSevenDistinctLetters(newWord)) {
                return;
            }

            addWordToFile(newWord, FileHandler.ALL_WORDS_FILE_NAME);
            if (isSevenLetterWord(newWord)) {
                addWordToFile(newWord, FileHandler.SEVEN_LETTER_WORDS_FILE_NAME);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewWordsFromFile() {
        try {
            System.out.println("Please enter the name of the file containing new words (must be in same directory!) or enter 0 to exit.");
            String newWordsFileName = System.console().readLine();

            if (newWordsFileName.equals("0")) {
                return;
            }

            List<String> newWords = fileHandler.loadWordsIntoList(
                    fileHandler.getFile(newWordsFileName));

            newWords = newWords.stream()
                    .filter(WordProcessor::hasOnlyLetters)
                    .filter(WordProcessor::hasMoreThanThreeLetters)
                    .filter(WordProcessor::hasNotMoreThanSevenDistinctLetters)
                    .collect(Collectors.toList());

            List<String> newSevenLetterWords = filterSevenLetterWords(newWords);

            addWordsToFile(newWords, FileHandler.ALL_WORDS_FILE_NAME);
            addWordsToFile(newSevenLetterWords, FileHandler.SEVEN_LETTER_WORDS_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> filterSevenLetterWords(Collection<String> words) {
        return words.stream()
                .filter(WordProcessor::isSevenLetterWord)
                .collect(Collectors.toList());
    }

    private void addWordToFile(String newWord, String fileName) throws IOException {
        addWordsToFile(List.of(newWord), fileName);
    }

    private void addWordsToFile(List<String> newWords, String fileName) throws IOException {
        Set<String> words = fileHandler.loadWordsIntoSet(
                fileHandler.getOrCreateFile(fileName));
        words.addAll(newWords);
        fileHandler.saveWords(words, fileHandler.createFile(fileName));
    }

    public static boolean isSevenLetterWord(String word) {
        if (word == null) {
            return false;
        }
        return word.length() == 7 && word.chars().distinct().count() == 7;
    }

    public static boolean hasNotMoreThanSevenDistinctLetters(String word) {
        return word.chars().distinct().count() <= 7;
    }

    public static boolean eachLetterOnlyOnce(String word) {
        return word.length() == word.chars().distinct().count();
    }

    public static boolean hasOnlyLetters(String word) {
        return word.matches("[a-zA-Z]*");
    }

    public static boolean hasMoreThanThreeLetters(String word) {
        return word.length() > 3;
    }
}
