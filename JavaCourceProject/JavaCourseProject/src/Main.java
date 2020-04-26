
import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.Board;
import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.GameMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Main {

    private static HashSet<String> initializeCommandsSet() {
        List<String> gameCommandsList = new ArrayList<>();
        for (char letter = 'a'; letter <= 'j'; letter++) {
            for (int i = 1; i <= 10; i++) {
                String command = Character.toString(letter) + Integer.toString(i);
                gameCommandsList.add(command);
            }
        }
        return new HashSet<>(gameCommandsList);
    }

    private static boolean isPlayingCommand(String command) {
        return command.equals("end-game") || command.matches("[a-j][0-9]");
    }

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GameMode gameMode = new GameMode(null, null);
        Board board = new Board();
        gameMode.enterShips(board);
    }
}