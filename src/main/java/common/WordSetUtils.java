package common;

import java.util.ArrayList;
import java.util.Comparator;

public class WordSetUtils {

    /*
    public static boolean containsWord(ArrayList<String> words, String word) {
        return containsWord(words, word, 0, words.size());
    }

    public static boolean containsWord(ArrayList<String> words, String word, int beginIndex, int endIndex) {
        if (beginIndex == endIndex) {
            return false;
        }

        if (word == null) {
            return false;
        }

        int checkIndex = (beginIndex + endIndex) / 2;
        int compareResult = words.get(checkIndex).compareTo(word);

        if (compareResult < 0) {
            return containsWord(words, word, checkIndex + 1, endIndex);
        } else if (compareResult > 0) {
            return containsWord(words, word, beginIndex, checkIndex - 1);
        } else {
            return true;
        }
    }
    public static void sortWords(ArrayList<String> words) {
        words.sort(String::compareTo);
    }
     */
}
