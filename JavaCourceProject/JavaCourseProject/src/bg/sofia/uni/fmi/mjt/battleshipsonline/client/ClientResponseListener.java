package bg.sofia.uni.fmi.mjt.battleshipsonline.client;

import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.GameMode;
import bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.models.GameModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.battleshipsonline.client.constants.Constants.*;

/**
 * Listens for server responses and runs GameMode and ClientsRequestSender
 * threads.
 */
public class ClientResponseListener implements Runnable{

    private boolean isJson(String json) {
        return json.charAt(0) == '[' || json.charAt(0) == '{';
    }

    /**
     * Lists all games.
     */
    private static void listGames(List<GameModel> gamesInfo) {
        System.out.println();
        System.out.println("| NAME     | CREATOR | STATUS      | PLAYERS |");
        System.out.println("|----------+---------+-------------+---------|");
        for (GameModel gameModel : gamesInfo) {

            String name = gameModel.getGameName();
            String creator = gameModel.getCreator();
            String status = gameModel.getStatus();
            String players = gameModel.getNumOfPlayers();

            int gap1 = MID_GAP - name.length();
            int gap2 = SMALL_GAP - creator.length();
            int gap3 = BIG_GAP - status.length();
            int gap4 = SMALL_GAP - players.length();

            System.out.printf("| %s" + "%" + gap1 + "s" + "| %s" + "%" + gap2 + "s" + "| %s" + "%" + gap3 + "s" + "| %s"
                    + "%" + gap4 + "s" + "|", name, " ", creator, " ", status, " ", players, " ");
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints server response, and runs needed threads.
     */
    @Override
    public void run() {
        try (Socket socket = new Socket(LOCALHOST, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println(CONNECTED_TO_SERVER_MESSAGE);
            System.out.println(ENTER_GAME_MESSAGE);

            ClientRequestSender serverRequestSender = new ClientRequestSender(writer);
            Thread senderThread = new Thread(serverRequestSender);
            senderThread.start();

            while (true) {
                String reply = reader.readLine();

                if (isJson(reply)) {
                    Gson gson = new Gson();
                    List<GameModel> gamesInfo;
                    Type type = new TypeToken<List<GameModel>>(){}.getType();
                    gamesInfo = gson.fromJson(reply, type);
                    listGames(gamesInfo);

                } else if (reply.equals(QUIT_MESSAGE)) {
                    System.out.println(PRESS_TO_QUIT_MESSAGE);
                    senderThread.interrupt();
                    break;
                } else if (reply.equals(STARTED_GAME_MESSAGE)) {

                    System.out.println(PRESS_TO_START_MESSAGE);
                    senderThread.interrupt();
                    senderThread.join();

                    GameMode gameMode = new GameMode(reader, writer);
                    Thread gameThread = new Thread(gameMode);
                    gameThread.start();
                    gameThread.join();

                    senderThread = new Thread(serverRequestSender);
                    senderThread.start();

                } else {
                    if (!reply.equals(WON_GAME_COMMAND) && !reply.equals(GAME_IN_PROGRESS_COMMAND)) {
                        System.out.println(reply);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("A problem with the connection occurred. Please restart the program");
        }
    }
}
