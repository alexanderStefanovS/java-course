import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.Board;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for the Board class.
 */
public class BoardTests {

    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testPrintBoard() {
        board.printBoard();
    }

    @Test
    public void testEnterShip() {
        String coordinates = "a0 a3";
        boolean isValid = board.enterShip(coordinates);
        assertTrue(isValid);
    }

    @Test
    public void testEnterIncorrectShip() {
        String coordinates = "a0b1";
        boolean isValid = board.enterShip(coordinates);
        assertFalse(isValid);
    }

    @Test
    public void testIsShipHit() {
        String coordinates = "a0 a3";
        board.enterShip(coordinates);
        String command = "a1";
        board.updateBoard(command);
        boolean isHit = board.isHitShip(command);
        assertTrue(isHit);
    }

}
