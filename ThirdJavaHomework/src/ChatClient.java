import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private static final int SERVER_PORT = 4444;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to the server.");

            ServerRequestSender serverRequestSender = new ServerRequestSender(socket);
            Thread thread = new Thread(serverRequestSender);
            thread.start();

            while (true) {
                String reply = reader.readLine();

                if (reply.equals("Quit")) {
                    System.out.println("Press any key to quit.");
                    thread.interrupt();
                    break;
                }

                System.out.println(reply);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
