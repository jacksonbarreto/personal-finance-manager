package bll.enumerators;

import java.io.Serializable;

public enum EOperationType implements Serializable {
    DEBIT(1),
    CREDIT(2);

    private final int ID;

    EOperationType(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
