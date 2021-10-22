package game;

import common.ConsoleUtils;
import common.FileHandler;

import java.io.IOException;
import java.util.*;

public class Game {

    private final FileHandler fileHandler;
    private final Set<String> allWords;
    private final Set<String> sevenLetterWords;
    private final Set<String> alreadyUsedWords;
    private String sevenLetterWord;

    public Game() {
        this.fileHandler = new FileHandler();

        Set<String> allWords;
        Set<String> sevenLetterWords;

        try {
            allWords = fileHandler.loadWordsIntoSet(
                    fileHandler.getFile(FileHandler.ALL_WORDS_FILE_NAME));
        } catch (IOException e) {
            System.out.println("A required word list (" + FileHandler.ALL_WORDS_FILE_NAME + ") was not found. " +
                               "Please ensure these files are in the same folder as the game!");
            allWords = new HashSet<>();
        }

        try {
            sevenLetterWords = fileHandler.loadWordsIntoSet(
                    fileHandler.getFile(FileHandler.SEVEN_LETTER_WORDS_FILE_NAME));
        } catch (IOException e) {
            System.out.println("A required word list (" + FileHandler.SEVEN_LETTER_WORDS_FILE_NAME + ") was not found. " +
                               "Please ensure these files are in the same folder as the game!");
            sevenLetterWords = new HashSet<>();
        }

        this.allWords = allWords;
        this.sevenLetterWords = sevenLetterWords;
        this.alreadyUsedWords = new HashSet<>();
        this.sevenLetterWord = "";
    }

    public void start() {
        if (wordListsAreEmpty()) {
            System.out.println("At least one of the word lists is empty. Please ensure that they actually contain words!");
        }
        pickSevenLetterWord();

        List<Character> shuffledCharacters = getShuffledLetters(sevenLetterWord);
        Character requiredCharcter = shuffledCharacters.get(0);
        String characterRegex = getCharacterRegex(shuffledCharacters);
        int matchingWordCount = getMatchingWordCount(characterRegex, requiredCharcter);

        printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);

        int foundWords = 0;
        int lastMessageLength = 0;

        String nextWord = "";
        while (foundWords < matchingWordCount && !nextWord.equals("0")) {
            nextWord = System.console().readLine();


            if (nextWord.equals("1")) {
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println("Words you found so far: " + String.join(",", alreadyUsedWords) + "\n");
            }

            if (nextWord.length() == 0) {
                //deleteLastTwoLinesFromConsole(lastMessageLength, nextWord);
                String message = "Please enter a word.\n";
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println(message);
                lastMessageLength = message.length();
                continue;
            }

            if (nextWord.length() < 4) {
                //deleteLastTwoLinesFromConsole(lastMessageLength, nextWord);
                String message = "Only words longer than 3 letters are allowed.\n";
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println(message);
                lastMessageLength = message.length();
                continue;
            }

            if (!nextWord.contains(String.valueOf(requiredCharcter))) {
                //deleteLastTwoLinesFromConsole(lastMessageLength, nextWord);
                String message = "The word must contain the letter " + requiredCharcter + "\n";
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println(message);
                lastMessageLength = message.length();
                continue;
            }

            if (!nextWord.matches(characterRegex)) {
                //deleteLastTwoLinesFromConsole(lastMessageLength, nextWord);
                String message = nextWord + " contains letters that are not allowed.\n";
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println(message);
                lastMessageLength = message.length();
                continue;
            }

            if (!allWords.contains(nextWord)) {
                //deleteLastTwoLinesFromConsole(lastMessageLength, nextWord);
                String message = "I don't know the word " + nextWord + ". If it's really a word, add it to the word list next time!\n";
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println(message);
                lastMessageLength = message.length();
                continue;
            }

            if (alreadyUsedWords.contains(nextWord)) {
                //deleteLastTwoLinesFromConsole(lastMessageLength, nextWord);
                String message = "You already said " + nextWord + ".\n";
                printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);
                System.out.println(message);
                lastMessageLength = message.length();
                continue;
            }

            foundWords++;
            alreadyUsedWords.add(nextWord);

            printGameRules(shuffledCharacters, matchingWordCount, requiredCharcter);

            if (nextWord.equals(sevenLetterWord)) {
                System.out.println("SUPER NICE, you found the seven letter word! You are soooooooo smart!\n");
            } else if (isDistinctSevenLetterWord(nextWord)) {
                System.out.println("""
                        Huh, interesting, that's not the seven letter word I was thinking of.
                        You are a tricky one, you know that?
                                                
                        """);
            }
            System.out.println("Nice! The word " + nextWord + " counts! Only "
                               + (matchingWordCount - foundWords) + " to go!\n");
        }

        System.out.println("Congratulations! You found all words!\n" +
                           "Holy shit that must have taken some time. You deserve a cookie :>\n" +
                           "Enter anything to quit.");

        System.console().readLine();
    }

    private void deleteLastTwoLinesFromConsole(int lastPrintedLineLength, String nextWord) {
        ConsoleUtils.deleteNumberOfCharactersFromConsole(lastPrintedLineLength);
        ConsoleUtils.deleteNumberOfCharactersFromConsole(nextWord.length() + 1);
    }

    private void printGameRules(List<Character> characters, int matchingWordCount, Character requiredCharacter) {
        ConsoleUtils.clearConsole();

        StringBuilder gameRuleBuilder = new StringBuilder("Welcome to the game!\n" +
                                                          "The letters this time are ");

        gameRuleBuilder.append(getLettersFormattedString(characters))
                .append("\n");

        gameRuleBuilder.append("The letter ")
                .append(requiredCharacter)
                .append(" must be in every word.\n");

        gameRuleBuilder.append("You have found ")
                .append(alreadyUsedWords.size())
                .append(" out of ")
                .append(matchingWordCount)
                .append(" so far.\n")
                .append("Good luck finding all of them!\n")
                .append("If you want to quit, just enter 0.\n")
                .append("If you want to show all words you already found, enter 1.\n\n");

        System.out.println(gameRuleBuilder);
    }

    private String getLettersFormattedString(List<Character> characters) {
        StringBuilder formattedLetters = new StringBuilder();
        int characterCount = characters.size();
        for (int i = 0; i < characterCount - 2; i++) {
            formattedLetters.append(characters.get(i)).append(", ");
        }
        formattedLetters.append(characters.get(characterCount - 2))
                .append(" and ")
                .append(characters.get(characterCount - 1));
        return formattedLetters.toString();
    }

    private List<Character> getShuffledLetters(String sevenLetterWord) {
        char[] sevenCharArray = sevenLetterWord.toCharArray();
        List<Character> sevenCharsList = new ArrayList<>(7);
        for (char c : sevenCharArray) {
            sevenCharsList.add(c);
        }
        while (!isShuffled(sevenCharsList, sevenLetterWord)) {
            Collections.shuffle(sevenCharsList);
        }
        return sevenCharsList;
    }

    private boolean wordListsAreEmpty() {
        return allWords.size() == 0 || sevenLetterWords.size() == 0;
    }

    private void pickSevenLetterWord() {

        int index = new Random().nextInt(sevenLetterWords.size());

        for (String sevenLetterWord : sevenLetterWords) {
            if (index == 0) {
                this.sevenLetterWord = sevenLetterWord;
                return;
            }
            index--;
        }
    }

    private boolean isShuffled(List<Character> charList, String word) {
        for (int i = 0; i < charList.size(); i++) {
            if (!charList.get(i).equals(word.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    private int getMatchingWordCount(String characterRegex, Character requiredCharacter) {
        int matchingWordCount = 0;
        String requiredCharacterString = String.valueOf(requiredCharacter);
        for (String word : allWords) {
            if (word.matches(characterRegex) && word.contains(requiredCharacterString)) {
                matchingWordCount++;
            }
        }
        return matchingWordCount;
    }

    private String getCharacterRegex(List<Character> characters) {
        StringBuilder regexBuilder = new StringBuilder("[");
        for (Character character : characters) {
            regexBuilder.append(character);
        }
        regexBuilder.append("]*");
        return regexBuilder.toString();
    }

    private boolean isDistinctSevenLetterWord(String word) {
        return word.length() == 7 && word.chars().distinct().count() == 7;
    }
}
