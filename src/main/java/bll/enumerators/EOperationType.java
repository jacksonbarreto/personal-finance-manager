package bll.enumerators;

import java.io.Serializable;

public enum EOperationType implements Serializable {
    DEBIT(1),
    CREDIT(2);

    private final Integer ID;

    EOperationType(int ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
