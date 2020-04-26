import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 10;

    public static void main(String[] args) {

        Map<String, Socket> clientNamesAndSocketsMap = new ConcurrentHashMap<>();
        Map<Socket, String> clientSocketAndNamesMap = new ConcurrentHashMap<>();

        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            System.out.println("Server started and listening for connect requests");

            Socket clientSocket;

            while (true) {

                clientSocket = serverSocket.accept();

                System.out.println("Client socket: " + clientSocket);
                System.out.println("Accepted connection request from client " + clientSocket.getInetAddress());

                ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket,
                        clientNamesAndSocketsMap, clientSocketAndNamesMap);

                executor.execute(clientHandler);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
