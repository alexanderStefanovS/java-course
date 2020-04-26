package bg.sofia.uni.fmi.mjt.battleshipsonline.server;

import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static bg.sofia.uni.fmi.mjt.battleshipsonline.server.constants.Constants.*;

/**
 * Main server class. Waits for players to connect
 * and executes thread for connected player.
 *
 */
public class Server {

    public static void main(String[] args) {

        Map<String, Game> games = new ConcurrentHashMap<>();
        Set<String> playerNames = Collections.newSetFromMap(new ConcurrentHashMap<>());

        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println(SERVER_LISTENING_MESSAGE);

            Socket clientSocket;
            while (true) {

                clientSocket = serverSocket.accept();

                System.out.println(CLIENT_SOCKET_MESSAGE + clientSocket);
                System.out.println(ACCEPTED_CONNECTION_MESSAGE + clientSocket.getInetAddress());

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, games, playerNames);
                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            System.out.println("Problem with creating server socket occurred.");
        }
    }
}
