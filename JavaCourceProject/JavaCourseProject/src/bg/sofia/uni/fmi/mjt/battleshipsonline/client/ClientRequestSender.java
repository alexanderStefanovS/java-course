package bg.sofia.uni.fmi.mjt.battleshipsonline.client;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Sends messages to the server.
 */
public class ClientRequestSender implements Runnable {

    private PrintWriter writer;

    ClientRequestSender(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = null;
            if (scanner.hasNextLine()) {
                message = scanner.nextLine();
            }
            if (Thread.interrupted()) {
                break;
            }
            if (message != null) {
                writer.println(message);
            }
        }
    }

}
