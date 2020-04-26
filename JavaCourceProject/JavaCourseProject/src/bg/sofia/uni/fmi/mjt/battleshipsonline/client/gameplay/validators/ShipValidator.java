package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.validators;

import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.Field;
import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.FieldStatusEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Validates the coordinates of the ship
 * when it is being entered.
 */
public class ShipValidator {

    private Field[][] board;
    private Map<Integer, Integer> shipsMap;

    public ShipValidator(Field[][] board) {
        this.board = board;
        this.shipsMap = new HashMap<>();
        initializeShipsMap();
    }

    private void initializeShipsMap() {
        shipsMap.put(5, 1);
        shipsMap.put(4,2);
        shipsMap.put(3,3);
        shipsMap.put(2,4);
    }

    public boolean validateShipCoordinates(int firstX, int firstY, int secondX, int secondY) {

        if (firstX != secondX && firstY != secondY) {
            return false;
        }
        if (firstX == secondX) {
            if (isInvalidShipsSize(firstY, secondY)) {
                return false;
            }
            if (firstY < secondY) {
                return checkShipPosition(false, firstX, firstY, secondY);
            } else {
                return checkShipPosition(false, firstX, secondY, firstY);
            }
        } else {
            if (isInvalidShipsSize(firstX, secondX)) {
                return false;
            }
            if (firstX < secondX) {
                return checkShipPosition(true, firstY, firstX, secondX);
            } else {
                return checkShipPosition(true, firstY, secondX, firstX);
            }
        }
    }

    private boolean checkShipPosition(boolean isVertical, int equal, int first, int second) {

        if (isVertical) {
            for (int i = first; i <= second; i++) {
                if (board[i][equal].getFieldStatus() != FieldStatusEnum.EMPTY_FIELD) {
                    return false;
                }
            }
        } else {
            for (int i = first; i <= second; i++) {
                if (board[equal][i].getFieldStatus() != FieldStatusEnum.EMPTY_FIELD) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isInvalidShipsSize(int a, int b) {
        int length = Math.abs(a - b) + 1;
        if (shipsMap.containsKey(length)) {
            if (shipsMap.get(length) == 0) {
                return true;
            } else {
                int count = shipsMap.get(length) - 1;
                shipsMap.put(length, count);
            }
        } else {
            return true;
        }
        return false;
    }

}
