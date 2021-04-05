package bll.enumerators;

import java.io.Serializable;

public enum EUserState implements Serializable {
    ACTIVE(1),
    WAITING_FOR_EMAIL_CONFIRMATION(2),
    BLOCKED_BY_INVALID_PASSWORD(3),
    INVALID_LOGIN_ATTEMPT(4),
    INACTIVE(5);

    private final Integer ID;

    EUserState(int ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
