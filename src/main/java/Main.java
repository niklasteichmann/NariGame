import common.ConsoleUtils;
import game.Game;
import preprocessing.WordProcessor;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        int mode = main.getMode();

        if (mode == 1) {
            Game game = new Game();
            game.start();
        } else if (mode == 2) {
            WordProcessor wordProcessor = new WordProcessor();
            wordProcessor.addNewWord();
        } else if (mode == 3) {
            WordProcessor wordProcessor = new WordProcessor();
            wordProcessor.addNewWordsFromFile();
        }
        // if 0, just quit
        //ConsoleUtils.clearConsole();
    }

    public int getMode() {
        System.out.println("Enter what you want to do:");
        System.out.println("0 - nothing, just exit");
        System.out.println("1 - play the game!");
        System.out.println("2 - enter a new word into the word list");
        System.out.println("3 - enter a list of words into the word list");

        int mode;
        while (true) {
            String input = System.console().readLine();
            try {
                mode = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                mode = -1;
            }

            if (mode >= 0 && mode <= 3) {
                break;
            } else {
                System.out.println(input + " is not a valid option.");
            }
        }
        return mode;
    }
}
