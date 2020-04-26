package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.models;

/**
 * Represents the model of the game
 * which is received by the server
 * when client to list the games.
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

    public String getGameName() {
        return gameName;
    }

    public String getCreator() {
        return creator;
    }

    public String getStatus() {
        return status;
    }

    public String getNumOfPlayers() {
        return numOfPlayers;
    }

    @Override
    public String toString() {
        return "{ gameName = " + gameName + "; creator = " + creator + "; status = " + status + "; numOfPlayers = " + numOfPlayers + " }";
    }
}
