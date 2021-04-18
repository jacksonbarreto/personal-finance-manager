package bll.services;

import bll.entities.IUser;
import bll.enumerators.ERole;
import bll.exceptions.NullArgumentException;

import java.util.HashSet;
import java.util.Set;

import static bll.services.LoginService.LoginServiceDefault;
import static bll.services.PermissionService.permissionServiceDefault;
import static bll.services.SessionService.getCurrentUser;
import static bll.services.IdentificationService.identificationServiceDefault;

public class AuthenticationService implements IAuthenticationService {

    private final IIdentificationService identificationService;
    private final IPermissionService permissionService;
    private final ILoginService loginService;

    public AuthenticationService(IIdentificationService identificationService, IPermissionService permissionService, ILoginService loginService) {
        if (identificationService == null || permissionService == null || loginService == null)
            throw new NullArgumentException();

        this.identificationService = identificationService;
        this.permissionService = permissionService;
        this.loginService = loginService;
    }

    @Override
    public boolean authenticate(String accessKey, String password) {
        if (accessKey == null || password == null)
            throw new NullArgumentException();
        IUser user = identificationService.identifyUser(accessKey);
        if (user == null)
            return false;

        return loginService.logInto(user, password);
    }

    @Override
    public boolean authenticate(String accessKey, String password, ERole role) {
        if (accessKey == null || password == null || role == null)
            throw new NullArgumentException();
        IUser user = identificationService.identifyUser(accessKey);
        if (user == null)
            return false;
        if (!permissionService.hasRole(user, role))
            return false;
        return loginService.logInto(user, password);
    }


    @Override
    public boolean isAuthenticated(IUser user, Set<ERole> roles) {
        if (user == null || roles == null)
            throw new NullArgumentException();
        if (identificationService.isValid(user)) {
            for (ERole role : roles) {
                if (!user.getRoles().contains(role))
                    return false;
            }
            return getCurrentUser().equals(user) && getCurrentUser().getCredential().equals(user.getCredential());
        }
        return false;
    }

    @Override
    public boolean isAuthenticated(IUser user) {
        return isAuthenticated(user, new HashSet<>());
    }

    static IAuthenticationService authenticationServiceDefault() {
        return new AuthenticationService(identificationServiceDefault(), permissionServiceDefault(), LoginServiceDefault());
    }
}
