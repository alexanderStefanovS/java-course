package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay.models;

public class GameResponseModel {

    private String turn;
    private boolean isHitShip;
    private boolean isShipSunk;

    public GameResponseModel(String turn, boolean isHitShip, boolean isShipSunk) {
        this.turn = turn;
        this.isHitShip = isHitShip;
        this.isShipSunk = isShipSunk;
    }

    public String getTurn() {
        return turn;
    }

    public boolean isHitShip() {
        return isHitShip;
    }

    public boolean isShipSunk() {
        return isShipSunk;
    }
}
