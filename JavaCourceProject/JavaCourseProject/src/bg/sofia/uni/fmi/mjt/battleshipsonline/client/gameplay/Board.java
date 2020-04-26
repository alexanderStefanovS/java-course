package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay;

import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.validators.ShipValidator;

import static bg.sofia.uni.fmi.mjt.battleshipsonline.client.constants.Constants.*;

/**
 * Represents the gaming board of the battleship game.
 */
public class Board {

    private Field[][] board;
    private ShipValidator shipValidator;
    private int numberOfShipFields;

    public Board() {
        board = new Field[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
        shipValidator = new ShipValidator(board);
        numberOfShipFields = NUMBER_OF_SHIP_FIELDS;
    }

    private void initializeBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = new Field();
            }
        }
    }

    public void printBoard() {
        char letter = 'A';
        System.out.println("   0 1 2 3 4 5 6 7 8 9");
        System.out.println("   _ _ _ _ _ _ _ _ _ _");
        for (int i = 0; i < 10; i++) {
            System.out.print(letter + " ");
            for (int j = 0; j < 10; j++) {
                String symbol = board[i][j].getFieldStatus().getValue();
                System.out.print(ANSI_RED + "|" + ANSI_RESET);
                if (symbol.equals("*")) {
                    System.out.print(ANSI_RED + symbol + ANSI_RESET);
                } else {
                    System.out.print(symbol);
                }
            }
            System.out.println(ANSI_RED + "|" + ANSI_RESET);
            letter++;
        }
        System.out.println();
    }

    /**
     * Enters a ship if given coordinates are valid.
     */
    public boolean enterShip(String coordinatesLine) {
        String[] coordinates = coordinatesLine.split(SPLIT_SYMBOL);
        if (coordinates.length == 2) {
            if (isIncorrectCommand(coordinates[FIRST_COORDINATE]) || isIncorrectCommand(coordinates[1])) {
                return false;
            }

            int firstX = getX(coordinates[FIRST_COORDINATE]);
            int firstY = getY(coordinates[FIRST_COORDINATE]);
            int secondX = getX(coordinates[SECOND_COORDINATE]);
            int secondY = getY(coordinates[SECOND_COORDINATE]);

            if (!shipValidator.validateShipCoordinates(firstX, firstY, secondX, secondY)) {
                return false;
            }
            enterShipCoordinates(firstX, firstY, secondX, secondY);
        } else {
            return false;
        }
        return true;
    }

    private void enterShipCoordinates(int firstX, int firstY, int secondX, int secondY) {
        int first, second;
        if (firstX == secondX) {
            if (firstY < secondY) {
                first = firstY;
                second = secondY;
            } else {
                first = secondY;
                second = firstY;
            }
            for (int i = first; i <= second; i++) {
                board[firstX][i].setFieldStatus(FieldStatusEnum.SHIP_FIELD);
            }
        } else {
            if (firstX < secondX) {
                first = firstX;
                second = secondX;
            } else {
                first = secondX;
                second = firstX;
            }
            for (int i = first; i <= second; i++) {
                board[i][firstY].setFieldStatus(FieldStatusEnum.SHIP_FIELD);
            }
        }
    }

    public boolean isHitShip(String command) {
        int x = getX(command);
        int y = getY(command);
        return board[x][y].getFieldStatus() == FieldStatusEnum.HIT_SHIP_FIELD;
    }

    public void updateBoardIfEnemy(String command, boolean isShipHit) {
        int x = getX(command);
        int y = getY(command);
        if (isShipHit) {
            board[x][y].setFieldStatus(FieldStatusEnum.HIT_SHIP_FIELD);
        } else {
            board[x][y].setFieldStatus(FieldStatusEnum.HIT_EMPTY_FIELD);
        }
    }

    public void updateBoard(String command) {
        int x = getX(command);
        int y = getY(command);
        if (board[x][y].getFieldStatus() == FieldStatusEnum.EMPTY_FIELD) {
            board[x][y].setFieldStatus(FieldStatusEnum.HIT_EMPTY_FIELD);
        } else if (board[x][y].getFieldStatus() == FieldStatusEnum.SHIP_FIELD) {
            board[x][y].setFieldStatus(FieldStatusEnum.HIT_SHIP_FIELD);
            numberOfShipFields--;
        }
    }

    public boolean isGameLost() {
        return numberOfShipFields == 0;
    }

    private boolean isIncorrectCommand(String command) {
        return !command.matches(VALIDATION_REGEX);
    }

    private int getX(String coordinate) {
        return coordinate.charAt(0) - ASCII_VALUE;
    }

    private int getY(String coordinate) {
        return Integer.parseInt(Character.toString(coordinate.charAt(1)));
    }

}
