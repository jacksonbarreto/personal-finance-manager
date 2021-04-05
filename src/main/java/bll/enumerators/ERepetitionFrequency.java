package bll.enumerators;

import java.io.Serializable;

public enum ERepetitionFrequency implements Serializable {
    WEEKLY(1),
    FORTNIGHTLY(2),
    MONTHLY(3),
    QUARTERLY(4),
    YEARLY(5),
    NONE(6);

    private final Integer ID;

    ERepetitionFrequency(int ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }
}
