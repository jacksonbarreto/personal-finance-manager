package bll.services;

import bll.entities.IUser;

public interface ILoginService {

    /**
     * Returns {@code true} if the login is successful.
     *
     * @param user to be logged in.
     * @param password to be verified.
     * @return {@code true} if the login is successful.
     */
    boolean logInto(IUser user, String password);
}
