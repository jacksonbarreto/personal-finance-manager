package bll.services;

import bll.entities.IUser;
import bll.exceptions.NullArgumentException;

public interface IIdentificationService {

    /**
     * Returns the identified or null user if not found.
     *
     * @param accessKey of the user to be found.
     * @return the identified or null user if not found.
     * @throws NullArgumentException if any of the parameters are null.
     */
    IUser identifyUser(String accessKey);

    /**
     * Returns {@code true} if the informed user exists in the system and if the credentials are the same.
     *
     * @param user to be validated.
     * @return {@code true} if the informed user exists in the system and if the credentials are the same.
     * @throws NullArgumentException if any of the parameters are null.
     */
    boolean isValid(IUser user);
}
