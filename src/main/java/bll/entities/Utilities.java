package bll.entities;

import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;


public class Utilities {

    public static void isNullArgument(Object obj) {
        if (obj == null)
            throw new NullArgumentException();
    }

    public static void isInvalidStringSize(String s, int minimumSize, int maximumSize) {
        if (s.length() < minimumSize || s.length() > maximumSize)
            throw new InvalidNameSizeException();
    }

    public static void isInvalidName(String name, int minimumSize, int maximumSize) {
        isNullArgument(name);
        isInvalidStringSize(name, minimumSize, maximumSize);
    }
}
