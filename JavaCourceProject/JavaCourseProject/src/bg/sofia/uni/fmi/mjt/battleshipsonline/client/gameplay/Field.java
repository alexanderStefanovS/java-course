package bg.sofia.uni.fmi.mjt.battleshipsonline.client.gameplay;

/**
 * Represents one field of the gaming board.
 */
public class Field {

    private FieldStatusEnum fieldStatus;

    public Field() {
        fieldStatus = FieldStatusEnum.EMPTY_FIELD;
    }

    public FieldStatusEnum getFieldStatus() {
        return fieldStatus;
    }

    public void setFieldStatus(FieldStatusEnum fieldStatus) {
        this.fieldStatus = fieldStatus;
    }
}
