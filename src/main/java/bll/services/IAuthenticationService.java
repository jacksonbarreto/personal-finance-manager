package bll.services;

import bll.entities.IUser;
import bll.enumerators.ERole;
import bll.exceptions.AccessDeniedException;
import bll.exceptions.NullArgumentException;

import java.util.Set;

public interface IAuthenticationService {

    /**
     * Check if there is a user with the data informed.
     * If successful, the user returns.
     *
     * @param accessKey from user.
     * @param password  from user.
     * @return {@code true} if the login was successful.
     * @throws NullArgumentException if any of the parameters are null.
     */
    boolean authenticate(String accessKey, String password);

    /**
     * Check if there is a user with the data informed.
     * If successful, the user returns.
     *
     * @param accessKey from user.
     * @param password  from user.
     * @param role      from user.
     * @return {@code true} if the login was successful.
     * @throws NullArgumentException if any of the parameters are null.
     */
    boolean authenticate(String accessKey, String password, ERole role);

    /**
     * Returns {@code true} if the user is a valid user on the system and has the necessary permissions.
     *
     * @param user  to be validated.
     * @param roles required.
     * @return @code true} if the user is a valid user on the system and has the necessary permissions.
     * @throws NullArgumentException if any of the parameters are null.
     */
    boolean isAuthenticated(IUser user, Set<ERole> roles);

    /**
     * Returns {@code true} if the user is a valid user on the system.
     *
     * @param user to be validated.
     * @return {@code true} if the user is a valid user on the system.
     * @throws NullArgumentException if any of the parameters are null.
     */
    boolean isAuthenticated(IUser user);

}
