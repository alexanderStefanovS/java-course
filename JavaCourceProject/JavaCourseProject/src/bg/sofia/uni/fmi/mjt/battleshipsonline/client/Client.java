package bg.sofia.uni.fmi.mjt.battleshipsonline.client;

/**
 * Main class of the client.
 * Starts the client listener for server responses.
 */
public class Client {
    public static void main(String[] args) {
        ClientResponseListener clientResponseListener = new ClientResponseListener();
        Thread thread = new Thread(clientResponseListener);
        thread.start();
    }
}
