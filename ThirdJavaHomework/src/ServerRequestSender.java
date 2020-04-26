import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerRequestSender implements Runnable {

    private Socket socket;

    ServerRequestSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            while (true) {

                Scanner scanner;

                scanner = new Scanner(System.in);

                if (Thread.interrupted()) {
                    socket.close();
                    break;
                }

                String message = null;

                if (scanner.hasNextLine()) {
                    message = scanner.nextLine(); // read a line from the console
                }

                if (message != null) {
                    System.out.println("Sending message <" + message + "> to the server...");
                    writer.println(message);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
