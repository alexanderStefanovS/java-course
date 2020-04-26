package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static bg.sofia.uni.fmi.mjt.battleshipsonline.client.constants.Constants.*;

/**
 * Represents the business logic of the battleship game.
 */
public class GameMode implements Runnable {

    private BufferedReader reader;
    private PrintWriter writer;

    public GameMode(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Enters the ship in the beginning of the game.
     */
    public void enterShips(Board board) {
        int correctShips = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.println(START_GAME_MESSAGE);
        board.printBoard();

        String coordinates;
        do {
            System.out.print(ENTER_SHIP_COORDINATES_MESSAGE);
            coordinates = scanner.nextLine();
            if (board.enterShip(coordinates)) {
                correctShips++;
                board.printBoard();
            } else {
                System.out.println(ENTER_CORRECT_COORDINATES_MESSAGE);
            }
            System.out.println();
        } while (correctShips < NUMBER_OF_SHIPS);
    }

    private void updateYourBoardAfterTurn(Board board, String command) {
        board.updateBoard(command);
    }

    private void updateEnemyBoardAfterTurn(Board enemyBoard, String replay, String command) {
        if (replay != null) {
            boolean isShipHit = replay.equals(TRUE_COMMAND);
            enemyBoard.updateBoardIfEnemy(command, isShipHit);
        }
    }

    private boolean isShipHitAfterOpponentsTurn(Board board, String opponentTurn) {
        return board.isHitShip(opponentTurn);
    }

    /**
     * The actual gameplay of the battleship game.
     */
    public void gameplay(Board yourBoard) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String reply;
        Board enemyBoard = new Board();

        while (true) {
            System.out.println(YOUR_BOARD_MESSAGE);
            yourBoard.printBoard();
            System.out.println(ENEMY_BOARD_MESSAGE);
            enemyBoard.printBoard();

            boolean isValidCommand = false;

            String command;
            do {
                System.out.print(ENTER_YOUR_TURN_MESSAGE);
                command = scanner.nextLine();
                if (command.matches(VALIDATION_REGEX)) {
                    writer.println(command);
                    isValidCommand = true;
                } else {
                    System.out.println(GAME_IN_PROGRESS_MESSAGE);
                }
            } while (!isValidCommand);

            System.out.print(YOUR_OPPONENT_TURN_MESSAGE);
            for (int i = 0; i < 2; i++) {
                reply = reader.readLine();
                if (reply.equals(TRUE_COMMAND) || reply.equals(FALSE_COMMAND)) {
                    updateEnemyBoardAfterTurn(enemyBoard, reply, command);
                } else {
                    updateYourBoardAfterTurn(yourBoard, reply);
                    String response = isShipHitAfterOpponentsTurn(yourBoard, reply) ? TRUE_COMMAND : FALSE_COMMAND;
                    writer.println(response);
                    System.out.println(reply + "\n");
                }
            }

            if (yourBoard.isGameLost()) {
                System.out.println(LOST_THE_GAME_MESSAGE);
                writer.println(END_GAME_COMMAND);
                break;
            } else {
                writer.println(GAME_IN_PROGRESS_COMMAND);
            }

            String gameStatus = reader.readLine();
            if (gameStatus.equals(END_GAME_COMMAND)) {
                writer.println(WON_GAME_COMMAND);
                System.out.println(WON_THE_GAME_MESSAGE);
                break;
            }
        }
    }

    @Override
    public void run() {
        Board yourBoard = new Board();
        enterShips(yourBoard);
        System.out.println(WAIT_OPPONENT_SHIPS_MESSAGE);
        writer.println(READY_COMMAND);
        try {
            reader.readLine();
            gameplay(yourBoard);
        } catch (IOException e) {
            System.out.println("A problem with the connection occurred. Please restart the program.");
        }
    }
}
