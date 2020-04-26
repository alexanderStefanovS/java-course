package bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.models;

/**
 * Represents the model of the game
 * which is send to the client as json
 * when it wants to list the games.
 */
public class GameModel {
    private String gameName;
    private String creator;
    private String status;
    private String numOfPlayers;

    public GameModel(String gameName, String creator, String status, String numOfPlayers) {
        this.gameName = gameName;
        this.creator = creator;
        this.status = status;
        this.numOfPlayers = numOfPlayers;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumOfPlayers(String numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}
