package bll.enumerators;

import java.io.Serializable;

public enum EHandlingMode implements Serializable {
    ALL(1),
    JUST_THIS_ONE(2),
    THIS_AND_NEXT(3),
    NEXT(4),
    THIS_AND_PREVIOUS(5),
    PREVIOUS(6);

    private final Integer ID;

    EHandlingMode(int ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
