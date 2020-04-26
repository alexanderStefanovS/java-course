package bg.sofia.uni.fmi.mjt.battleshipsonline.server.constants;

/**
 * Class of constants needed by the server.
 */
public class Constants {

    /**
     * Server constants.
     */
    public static final int SERVER_PORT = 4444;
    public static final int MAX_EXECUTOR_THREADS = 20;

    /**
     * Constants for commands.
     */
    public static final String SET_USERNAME_COMMAND = "set-username";
    public static final String CREATE_GAME_COMMAND = "create-game";
    public static final String DISCONNECT_COMMAND = "disconnect";
    public static final String LIST_GAMES_COMMAND = "list-games";
    public static final String JOIN_GAME_COMMAND = "join-game";
    public static final String START_COMMAND = "start";
    public static final String READY_COMMAND = "ready";
    public static final String END_GAME_COMMAND = "end-game";
    public static final String WON_GAME_COMMAND = "won-game";
    public static final String GAME_IN_PROGRESS_COMMAND = "game-in-progress";
    public static final String TRUE_COMMAND = "true";
    public static final String FALSE_COMMAND = "false";

    /**
     * Constants for messages.
     */
    public static final String INCORRECT_COMMAND_MESSAGE = "Incorrect command";
    public static final String ALREADY_SET_USERNAME_MESSAGE = "You've already set your username.";
    public static final String WAITING_TO_START_MESSAGE = "You are waiting to start a game. " +
                                                            "Please type \"start\" to start the game!";
    public static final String GAME_IN_PROGRESS_MESSAGE = "You are in a game! Please type game commands.";
    public static final String FIRST_ENTER_USERNAME_MESSAGE = "Enter your username first.";
    public static final String WAIT_FOR_OPPONENT_MESSAGE = "Wait for your opponent to get ready.";
    public static final String STARTED_GAME_MESSAGE = "The game is started!";
    public static final String OPPONENT_READY_MESSAGE = "Your opponent is ready.";
    public static final String GAME_HAS_STARTED_MESSAGE = "This game has started!";
    public static final String GAME_IS_FINISHED_MESSAGE = "This game is finished!";
    public static final String START_GAME_MESSAGE = "PLAYERS: 2/2, type \"start\" to start the game";
    public static final String PLAYER_JOINED_MESSAGE = " joined your game.\nPLAYERS: 2/2, type \"start\" to start the game";
    public static final String NO_GAME_NAME_MESSAGE = "There is no game with such name!";
    public static final String ENTER_GAME_NAME_MESSAGE = "Please enter the name of the game when using " +
                                                                "\"create-game\" command.";
    public static final String WAIT_ANOTHER_PLAYER_MESSAGE = "Please wait for another player to join.";
    public static final String QUIT_MESSAGE = "Quit";
    public static final String ENTER_NAME_MESSAGE = "Please enter the name of the user when using \"set-username\" command.";
    public static final String SERVER_LISTENING_MESSAGE = "Server started and listening for connect requests";
    public static final String CLIENT_SOCKET_MESSAGE = "Client socket: ";
    public static final String ACCEPTED_CONNECTION_MESSAGE = "Accepted connection request from client ";

    public static String getCreateGameMessage(String gameName, int numberOfPlayers) {
        return "Created game " + gameName + ", players "
                + numberOfPlayers + "/2";
    }

    public static String getSetUsernameMessage(String username) {
        return "The username " + username + " is set.";
    }

    public static String getEnterAnotherUsernameMessage(String username) {
        return "There is another player with username " + username
                + ". Please enter another username.";
    }

    /**
     * Other constants.
     */
    public static final String VALIDATION_REGEX = "[a-j][0-9]";
    public static final String SPLIT_SYMBOL = " ";
    public static final int SPLIT_LIMIT = 2;

    /**
     * Game constants
     */
    public static final String ALL_PLAYERS = "/2";
    public static final int ONE_PLAYER = 1;
    public static final int TWO_PLAYERS = 2;


}
