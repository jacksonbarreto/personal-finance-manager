package bll.enumerators;

import java.io.Serializable;

public enum ERole implements Serializable {

    SIMPLE(1),
    PREMIUM(2),
    ADMIN(3);

    private final Integer ID;

    ERole(int ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
