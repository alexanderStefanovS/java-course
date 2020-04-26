package bg.sofia.uni.fmi.mjt.battleshipsonline.server;

import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.Game;
import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.models.GameModel;
import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.GameStatus;
import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.Player;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.battleshipsonline.server.constants.Constants.*;

/**
 * Represents server business logic of the server.
 * Running for every player. Executes player commands.
 * Uses general collections for games and players.
 */
public class ClientRequestHandler implements Runnable {

    private Socket socket;
    private Player player;
    private Map<String, Game> games;
    private Set<String> playerNames;
    private boolean isGamePending;
    private boolean isGameStarted;
    private Game currentGame;
    private boolean isDisconnected;

    public ClientRequestHandler(Socket socket, Map<String, Game> games, Set<String> playerNames) {
        this.socket = socket;
        this.player = null;
        this.games = games;
        this.playerNames = playerNames;
        this.currentGame = null;
        this.isDisconnected = false;
    }

    /**
     * Sets username of the player if it's empty,
     * and if the command is correct.
     */
    public void setUsername(PrintWriter out, String[] commandParts) {
        if (commandParts.length > 1) {
            String username = commandParts[1];
            if (playerNames.contains(username)) {
                out.println(getEnterAnotherUsernameMessage(commandParts[1]));
            } else {
                player = new Player(username, socket);
                playerNames.add(player.getUsername());
                out.println(getSetUsernameMessage(commandParts[1]));
            }
        } else {
            out.println(ENTER_NAME_MESSAGE);
        }
    }

    /**
     * Creates new game if the command is correct.
     */
    public void createGame(PrintWriter out, String[] commandParts) {
        if (commandParts.length > 1) {
            String gameName = commandParts[1];
            currentGame = new Game(gameName, player, GameStatus.PENDING);
            games.put(gameName, currentGame);
            out.println(getCreateGameMessage(gameName, currentGame.getNumberOfPlayers()));
            out.println(WAIT_ANOTHER_PLAYER_MESSAGE);
            isGamePending = true;
        } else {
            out.println(ENTER_GAME_NAME_MESSAGE);
        }
    }

    /**
     * Sends List<GameModel> as json to the client.
     */
    public void listGames(PrintWriter out) {
        Gson gson = new Gson();
        List<GameModel> gamesModels = new ArrayList<>();
        for (Game game : games.values()) {
            gamesModels.add(game.getGameModel());
        }
        System.out.println(gamesModels);
        String json = gson.toJson(gamesModels);
        out.println(json);
    }

    /**
     * Gets random game from the games collection.
     */
    private Game getRandomPendingGame() {
        List<Game> randomGames = games.values().stream()
                .filter( game -> game.getGameStatus() == GameStatus.PENDING )
                .collect(Collectors.toList());
        Random rand = new Random();
        int gamesSize = randomGames.size();
        int randomGameIndex = rand.nextInt(gamesSize);
        return randomGames.get(randomGameIndex);
    }

    /**
     * Joins player to a certain game or to a random one
     * according to the arguments of the command.
     */
    private void joinGame(PrintWriter out, String[] commandParts) throws IOException {
        if (commandParts.length > 1) {
            String gameName = commandParts[1];
            if (games.containsKey(gameName)) {
                currentGame = games.get(gameName);
            } else {
                out.println(NO_GAME_NAME_MESSAGE);
                return;
            }
        } else if (commandParts.length == 1) {
            currentGame = getRandomPendingGame();
        }

        if (currentGame.getGameStatus() == GameStatus.PENDING) {
            currentGame.setGuest(player);
            currentGame.setGameStatus(GameStatus.IN_PROGRESS);
            games.put(currentGame.getName(), currentGame);
            out.println(START_GAME_MESSAGE);
            Socket hostSocket = currentGame.getHost().getSocket();
            PrintWriter hostOut = new PrintWriter(hostSocket.getOutputStream(), true);
            hostOut.println(player.getUsername() + PLAYER_JOINED_MESSAGE);
        } else if (currentGame.getGameStatus() == GameStatus.IN_PROGRESS) {
            out.println(GAME_HAS_STARTED_MESSAGE);
        } else {
            out.println(GAME_IS_FINISHED_MESSAGE);
        }
    }

    /**
     * Starts game and notifies the players.
     * Makes players to wait for each other to start.
     */
    private void startGame(PrintWriter out) throws IOException {
        Socket opponentSocket;
        boolean isHost;

        if (currentGame.getHost().getUsername().equals(player.getUsername())) {
            opponentSocket = currentGame.getGuest().getSocket();
            isHost = true;
        } else {
            opponentSocket = currentGame.getHost().getSocket();
            isHost = false;
        }
        PrintWriter opponentOut = new PrintWriter(opponentSocket.getOutputStream(), true);

        if (isHost) {
            currentGame.setHostReady(true);
        } else {
            currentGame.setGuestReady(true);
        }

        boolean isOpponentReady = isHost ? currentGame.isGuestReady() : currentGame.isHostReady();

        opponentOut.println(OPPONENT_READY_MESSAGE);
        if (isOpponentReady) {
            opponentOut.println(STARTED_GAME_MESSAGE);
            out.println(STARTED_GAME_MESSAGE);
        } else {
            out.println(WAIT_FOR_OPPONENT_MESSAGE);
        }

        isGamePending = false;
        isGameStarted = true;
    }

    /**
     * Ends game and changes its status.
     *
     */
    private void endGame() {
        isGameStarted = false;
        currentGame.setGameStatus(GameStatus.FINISHED);
        games.put(currentGame.getName(), currentGame);
        for (Game value : games.values()) {
            System.out.println(value.toString());
        }
    }

    /**
     * Sends command to opponent while the game is in progress.
     */
    private void sendCommandToOpponent(String command) throws IOException {
        Socket opponentSocket;
        if (currentGame.getHost().getUsername().equals(player.getUsername())) {
            opponentSocket = currentGame.getGuest().getSocket();
        } else {
            opponentSocket = currentGame.getHost().getSocket();
        }
        PrintWriter opponentOut = new PrintWriter(opponentSocket.getOutputStream(), true);
        opponentOut.println(command);
    }

    /**
     * Notifies the players that the ships are positioned
     * and the game can start.
     */
    private void readyToStart(PrintWriter out, String command) throws IOException {
        boolean isHost = currentGame.getHost().getUsername().equals(player.getUsername());
        if (isHost) {
            currentGame.setHostPositionShips(true);
            if (currentGame.isGuestPositionShips()) {
                sendCommandToOpponent(command);
                out.println(command);
            }
        } else {
            currentGame.setGuestPositionShips(true);
            if (currentGame.isHostPositionShips()) {
                sendCommandToOpponent(command);
                out.println(command);
            }
        }
    }

    /**
     * Disconnects player from the server.
     */
    private void disconnect(PrintWriter out) {
        out.println(QUIT_MESSAGE);
        playerNames.remove(player.getUsername());
        isDisconnected = true;
    }

    /**
     * Validates commands and sends messages to the player when
     * the command is invalid.
     */
    private boolean isValidCommand(PrintWriter out, String command) {
        boolean isValid = true;
        if (player == null && !command.equals(SET_USERNAME_COMMAND) && !command.equals(DISCONNECT_COMMAND)) {
            out.println(FIRST_ENTER_USERNAME_MESSAGE);
            isValid = false;
        } else if (isGameStarted && !command.equals(DISCONNECT_COMMAND) && !command.equals(READY_COMMAND)
                && !isGameplayCommand(command)) {
            out.println(GAME_IN_PROGRESS_MESSAGE);
            isValid = false;
        } else if (isGamePending && !command.equals(DISCONNECT_COMMAND) && !command.equals(START_COMMAND)) {
            out.println(WAITING_TO_START_MESSAGE);
            isValid = false;
        }
        return isValid;
    }

    /**
     * Validates and executes the command.
     */
    private void executeCommand(String[] commandParts) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String command = commandParts[0];
        if (!isValidCommand(out, command)) {
            return;
        }

        if (isGameplayCommand(command)) {
            if (command.equals(WON_GAME_COMMAND) || command.equals(END_GAME_COMMAND)) {
                endGame();
            }
            sendCommandToOpponent(command);
            return;
        }

        switch (command) {
            case SET_USERNAME_COMMAND: {
                if (player != null) {
                    out.println(ALREADY_SET_USERNAME_MESSAGE);
                } else {
                    setUsername(out, commandParts);
                }
                break;
            }
            case CREATE_GAME_COMMAND: {
                createGame(out, commandParts);
                break;
            }
            case DISCONNECT_COMMAND: {
                disconnect(out);
                break;
            }
            case LIST_GAMES_COMMAND: {
                listGames(out);
                break;
            }
            case JOIN_GAME_COMMAND: {
                joinGame(out, commandParts);
                break;
            }
            case START_COMMAND: {
                startGame(out);
                break;
            }
            case READY_COMMAND: {
                readyToStart(out, command);
                break;
            }
            default: {
                System.out.println(command);
                out.println(INCORRECT_COMMAND_MESSAGE);
            }
        }

    }

    private boolean isGameplayCommand(String command) {
        return command.matches(VALIDATION_REGEX) || command.equals(TRUE_COMMAND)
                || command.equals(FALSE_COMMAND) || command.equals(END_GAME_COMMAND)
                || command.equals(GAME_IN_PROGRESS_COMMAND) || command.equals(WON_GAME_COMMAND);
    }

    /**
     * Reads commands from the player and executes them.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] commandParts = inputLine.split(SPLIT_SYMBOL, SPLIT_LIMIT);
                executeCommand(commandParts);
                if (isDisconnected) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Problem with connection with this socket " + socket + " occurred.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
