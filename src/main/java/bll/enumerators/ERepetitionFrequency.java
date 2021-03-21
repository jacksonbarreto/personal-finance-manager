package bll.enumerators;

import java.io.Serializable;

public enum ERepetitionFrequency implements Serializable {
    WEEKLY(1),
    FORTNIGHTLY(2),
    MONTHLY(3),
    QUARTERLY(4),
    YEARLY(5),
    NONE(6);

    private final int ID;

    ERepetitionFrequency(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
