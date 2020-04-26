package bg.sofia.uni.fmi.mjt.battleshipsonline.server.game;

import bg.sofia.uni.fmi.mjt.battleshipsonline.server.game.models.GameModel;

import static bg.sofia.uni.fmi.mjt.battleshipsonline.server.constants.Constants.*;

/**
 * Represents game for the server.
 */
public class Game {

    private String name;
    private Player host;
    private Player guest;
    private GameStatus gameStatus;
    private GameModel gameModel;
    private boolean isHostReady;
    private boolean isGuestReady;
    private boolean isHostPositionShips;
    private boolean isGuestPositionShips;

    public Game(String name, Player host, GameStatus gameStatus) {
        this.name = name;
        this.host = host;
        this.gameStatus = gameStatus;
        this.guest = null;
        this.isGuestReady = false;
        this.isHostReady = false;
        this.isHostPositionShips = false;
        this.isGuestPositionShips = false;
        String numberOfPlayers = getNumberOfPlayers() + ALL_PLAYERS;
        String username = host == null ? null : host.getUsername();
        this.gameModel = new GameModel(name, username, gameStatus.toString(), numberOfPlayers);
    }

    public int getNumberOfPlayers() {
        return guest == null ? ONE_PLAYER : TWO_PLAYERS;
    }

    public void setGuest(Player guest) {
        this.guest = guest;
        String numberOfPlayers = (getNumberOfPlayers() + ONE_PLAYER) + ALL_PLAYERS;
        this.gameModel.setNumOfPlayers(numberOfPlayers);
    }

    public String getName() {
        return name;
    }

    public Player getHost() {
        return host;
    }

    public Player getGuest() {
        return guest;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.gameModel.setStatus(gameStatus.toString());
    }

    public boolean isHostReady() {
        return isHostReady;
    }

    public boolean isGuestReady() {
        return isGuestReady;
    }

    public void setHostReady(boolean hostReady) {
        isHostReady = hostReady;
    }

    public void setGuestReady(boolean guestReady) {
        isGuestReady = guestReady;
    }

    public boolean isHostPositionShips() {
        return isHostPositionShips;
    }

    public boolean isGuestPositionShips() {
        return isGuestPositionShips;
    }

    public void setHostPositionShips(boolean hostPositionShips) {
        isHostPositionShips = hostPositionShips;
    }

    public void setGuestPositionShips(boolean guestPositionShips) {
        isGuestPositionShips = guestPositionShips;
    }

}
