package bll.enumerators;

import java.io.Serializable;

public enum ERemovalMode implements Serializable {
    ALL(1),
    JUST_THIS_ONE(2),
    THIS_AND_NEXT(3),
    NEXT(4),
    THIS_AND_PREVIOUS(5),
    PREVIOUS(6);

    private final int ID;

    ERemovalMode(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
