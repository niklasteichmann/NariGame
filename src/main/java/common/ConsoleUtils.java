package common;

import java.io.IOException;

public class ConsoleUtils {
    public static void clearConsole() {
        try {

            if (System.getProperty("os.name").contains("Windows"))

                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else

                Runtime.getRuntime().exec("clear");

        } catch (IOException | InterruptedException ex) {}
    }

    public static void deleteNumberOfCharactersFromConsole(int number) {
        System.out.print("\b".repeat(number));
        System.out.print(" ".repeat(number));
        System.out.print("\b".repeat(number));
    }
}
