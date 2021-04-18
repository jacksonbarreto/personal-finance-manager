package bll.services;

import bll.entities.IUser;
import bll.enumerators.ERole;

public interface IPermissionService {

    /**
     * Returns {@code true} if the user has the indicated permission.
     *
     * @param user to be validated.
     * @param role expected.
     * @return  {@code true} if the user has the indicated permission.
     */
    boolean hasRole(IUser user, ERole role);
}
