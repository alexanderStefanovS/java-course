package bg.sofia.uni.fmi.mjt.battleshipsonline.client.constants;

/**
 * Class of constants needed by the client.
 */
public class Constants {

    /**
     * Constants for commands.
     */
    public static final String END_GAME_COMMAND = "end-game";
    public static final String WON_GAME_COMMAND = "won-game";
    public static final String GAME_IN_PROGRESS_COMMAND = "game-in-progress";
    public static final String TRUE_COMMAND = "true";
    public static final String FALSE_COMMAND = "false";
    public static final String READY_COMMAND = "ready";

    /**
     * Constants for messages.
     */
    public static final String PRESS_TO_START_MESSAGE = "Press any key to start.";
    public static final String PRESS_TO_QUIT_MESSAGE = "Press any key to quit.";
    public static final String STARTED_GAME_MESSAGE = "The game is started!";
    public static final String CONNECTED_TO_SERVER_MESSAGE = "Connected to the server.";
    public static final String QUIT_MESSAGE = "Quit";
    public static final String WAIT_OPPONENT_SHIPS_MESSAGE = "Please wait for your opponent to get ready with ships.\n";
    public static final String WON_THE_GAME_MESSAGE = "Congratulations, you won the game!";
    public static final String LOST_THE_GAME_MESSAGE = "You lost the game!";
    public static final String YOUR_OPPONENT_TURN_MESSAGE = "Your opponents turn: ";
    public static final String ENTER_YOUR_TURN_MESSAGE = "Enter your turn: ";
    public static final String YOUR_BOARD_MESSAGE = "\t\tYOUR BOARD";
    public static final String ENEMY_BOARD_MESSAGE = "\t\tENEMY BOARD";
    public static final String ENTER_CORRECT_COORDINATES_MESSAGE = "Please enter correct coordinates.";
    public static final String ENTER_SHIP_COORDINATES_MESSAGE = "Enter coordinates of a ship: ";
    public static final String GAME_IN_PROGRESS_MESSAGE = "You are in a game! Please type game commands.";
    public static final String START_GAME_MESSAGE = "\nYou have to enter the coordinates of your ships.\n" +
            "You have 1 ship with size 5, 2 ships with size 4,\n" +
            "3 ships with size 3 and 4 ships with size 2.\n" +
            "You have to enter first and last coordinate of the ship.\n";
    public static final String ENTER_GAME_MESSAGE = "Available commands:\n" +
                                            "\tcreate-game <game-name>\n" +
                                            "\tjoin-game [<game-name>]\n" +
                                            "\tdisconnect\n" +
                                            "\tlist-games.";

    /**
     * Server constants.
     */
    public static final String LOCALHOST = "localhost";
    public static final int SERVER_PORT = 4444;

    public static final int SMALL_GAP = 8;
    public static final int BIG_GAP = 12;
    public static final int MID_GAP = 9;
    public static final int NUMBER_OF_SHIPS = 1;

    /**
     * Board constants.
     */
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String VALIDATION_REGEX = "[a-j][0-9]";
    public static final int BOARD_SIZE = 10;
    public static final int NUMBER_OF_SHIP_FIELDS = 2;
    public static final int FIRST_COORDINATE = 0;
    public static final int SECOND_COORDINATE = 1;
    public static final String SPLIT_SYMBOL = " ";
    public static final int ASCII_VALUE = 97;
}
