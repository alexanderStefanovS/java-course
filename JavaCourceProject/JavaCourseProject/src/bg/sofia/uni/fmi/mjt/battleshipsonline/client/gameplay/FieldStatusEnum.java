package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay;

/**
 * Enum for the status of board fields.
 */
public enum FieldStatusEnum {

    EMPTY_FIELD("_"), SHIP_FIELD("X"), HIT_EMPTY_FIELD("-"), HIT_SHIP_FIELD("*");

    private String value;

    FieldStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
