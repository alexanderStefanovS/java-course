import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServerTest {

    private Socket senderSocket;
    private Socket receiverSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ClientRequestHandler clientRequestHandler;
    private Map<String, Socket> clientNamesAndSocketsMap;
    private Map<Socket, String> clientSocketsAndNamesMap;

    @Before
    public void setUp() {
        receiverSocket = new Socket();
        senderSocket = new Socket();
        out = new PrintWriter(System.out);
        clientNamesAndSocketsMap = new HashMap<>();
        clientNamesAndSocketsMap.put("user", receiverSocket);
        clientSocketsAndNamesMap = new HashMap<>();
        clientSocketsAndNamesMap.put(receiverSocket, "user");
        clientRequestHandler = new ClientRequestHandler(senderSocket, clientNamesAndSocketsMap, clientSocketsAndNamesMap);
    }

    @Test
    public void testSend() {
        String sendCommand = "send user hello";
        String[] commandParts = sendCommand.split(" ");
        String time = clientRequestHandler.getDateAndTime();

        if (clientNamesAndSocketsMap.containsKey(commandParts[1])) {
            Socket receiverSocket = clientNamesAndSocketsMap.get(commandParts[1]);
            String senderName = clientSocketsAndNamesMap.get(receiverSocket);
            System.out.println(time + " " + senderName + ": " + commandParts[2]);
        } else {
            System.out.println("User " + commandParts[1] + " seem to be offline.");
        }
    }

    @Test
    public void testSetNick() {
        String sendCommand = "send user hello";
        String[] commandParts = sendCommand.split(" ");
        clientRequestHandler.setNick(out, commandParts);
    }




}
