package bll.enumerators;

import java.io.Serializable;

public enum ERole implements Serializable {

    SIMPLE(1),
    PREMIUM(2),
    ADMIN(3);

    private final int roleID;

    ERole(int roleID) {
        this.roleID = roleID;
    }

    public int getRoleID() {
        return roleID;
    }
}
