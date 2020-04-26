import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class ClientRequestHandler implements Runnable {

    private Socket socket;
    private Map<String, Socket> clientNamesAndSocketsMap;
    private Map<Socket, String> clientSocketsAndNamesMap;

    public ClientRequestHandler(Socket socket, Map<String, Socket> clientNamesAndSocketsMap,
                                Map<Socket, String> clientSocketsAndNamesMap) {
        this.socket = socket;
        this.clientNamesAndSocketsMap = clientNamesAndSocketsMap;
        this.clientSocketsAndNamesMap = clientSocketsAndNamesMap;
    }

    public String getDateAndTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String dateAndTime = "[" + date + " " + time.getHour() + ":" +  time.getMinute() + "]";
        return dateAndTime;
    }

    public void setNick(PrintWriter out, String[] commandParts) {
        clientNamesAndSocketsMap.put(commandParts[1], socket);
        clientSocketsAndNamesMap.put(socket, commandParts[1]);
        out.println("The nick " + commandParts[1] + " is set.");
    }

    public void send(String time, PrintWriter out, String[] commandParts) throws IOException {
        if (clientNamesAndSocketsMap.containsKey(commandParts[1])) {
            Socket receiverSocket = clientNamesAndSocketsMap.get(commandParts[1]);
            String senderName = clientSocketsAndNamesMap.get(socket);
            PrintWriter send_out = new PrintWriter(receiverSocket.getOutputStream(), true);
            send_out.println(time + " " + senderName + ": " + commandParts[2]);
        } else {
            out.println("User " + commandParts[1] + " seem to be offline.");
        }
    }

    public void disconnect(PrintWriter out) {
        String name = clientSocketsAndNamesMap.remove(socket);
        if (name != null) {
            clientNamesAndSocketsMap.remove(name);
        }
        out.println("Quit");
    }

    public void listUsers(PrintWriter out) {
        StringBuilder userNames = new StringBuilder();
        for (String userName: clientNamesAndSocketsMap.keySet()) {
            userNames.append(userName).append(", ");
        }
        String output = userNames.toString();
        output = output.substring(0, output.length() - 2);
        out.println(output);
    }

    public void sendAll(String time, String[] commandParts) throws IOException {
        String senderName = clientSocketsAndNamesMap.get(socket);
        for (Socket socket : clientSocketsAndNamesMap.keySet()) {
            PrintWriter send_out = new PrintWriter(socket.getOutputStream(), true);
            send_out.println(time + " " + senderName + ": " + commandParts[1]);
        }
    }

    private void executeCommand(String[] commandParts) throws IOException {

        String command = commandParts[0];

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        if (clientSocketsAndNamesMap.get(socket) == null && !command.equals("nick") && !command.equals("disconnect")) {
            out.println("Enter your nick first");
            return;
        }

        String time = getDateAndTime();

        switch (command) {
            case "nick": {
                setNick(out, commandParts);
                break;
            }
            case "send": {
                send(time, out, commandParts);
                break;
            }
            case "disconnect": {
                disconnect(out);
                break;
            }
            case "list-users": {
                listUsers(out);
                break;
            }
            case "send-all": {
                sendAll(time, commandParts);
                break;
            }
            default: {
                out.println("Incorrect command!");
            }
        }

    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) { // read the message from the client

                String[] commandParts = inputLine.split(" ", 3);

                executeCommand(commandParts);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
