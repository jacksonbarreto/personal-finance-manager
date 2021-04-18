package bll.services;

import bll.entities.IUser;
import bll.enumerators.ERole;

public class PermissionService implements IPermissionService {

    @Override
    public boolean hasRole(IUser user, ERole role) {
        return user.getRoles().contains(role);
    }

    private PermissionService(){}
    public static IPermissionService permissionServiceDefault(){
        return new PermissionService();
    }
}
