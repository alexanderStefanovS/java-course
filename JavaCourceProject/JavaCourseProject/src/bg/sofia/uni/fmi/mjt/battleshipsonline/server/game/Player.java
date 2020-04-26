package bg.sofia.uni.fmi.mjt.battleshipsonline.server.game;

import java.net.Socket;

/**
 * Represents the player for the server.
 */
public class Player {

    private String username;
    private Socket socket;

    public Player(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }
}
