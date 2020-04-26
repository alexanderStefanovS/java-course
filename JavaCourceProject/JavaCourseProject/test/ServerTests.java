import bg.sofia.uni.fmi.mjt.battleshipsonline.server.ClientRequestHandler;
import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.Game;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the server.
 */
public class ServerTests {

    private ClientRequestHandler clientRequestHandler;
    private Map<String, Game> games;
    private Set<String> playerNames;

    @Before
    public void setUp() throws IOException {
        games = new HashMap<>();
        playerNames = new HashSet<>();
        clientRequestHandler = new ClientRequestHandler(null, games, playerNames);
    }

    @Test
    public void testCreateGame() {
        String[] commandParts = new String[] {"create-game", "game"};
        PrintWriter writer = new PrintWriter(System.out);
        clientRequestHandler.createGame(writer, commandParts);
        assertEquals(games.size(), 1);
    }

    @Test
    public void testListGames() {
        PrintWriter writer = new PrintWriter(System.out);
        String[] commandParts = new String[] {"create-game", "game1"};
        clientRequestHandler.createGame(writer, commandParts);
        commandParts = new String[]{"create-game", "game2"};
        clientRequestHandler.createGame(writer, commandParts);
        clientRequestHandler.listGames(writer);
    }

    @Test
    public void testSetUsername() {
        PrintWriter writer = new PrintWriter(System.out);
        String[] commandParts = new String[] {"set-username", "name"};
        clientRequestHandler.setUsername(writer, commandParts);
        assertEquals(playerNames.size(), 1);
    }

}
