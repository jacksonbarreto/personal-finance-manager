package bll.services;

import bll.entities.IUser;
import bll.enumerators.EUserState;
import bll.exceptions.AccessDeniedBlockedUserException;
import bll.exceptions.AccessDeniedUnconfirmedEmailException;
import bll.exceptions.NullArgumentException;
import bll.repositories.UserRepository;

import static bll.enumerators.EUserState.*;

public class LoginService implements ILoginService {
    private static final int INVALID_ATTEMPTS_ALLOWED = 3;

    private LoginService() {
    }

    public static ILoginService LoginServiceDefault() {
        return new LoginService();
    }

    @Override
    public boolean logInto(IUser user, String password) {
        if (user == null || password == null)
            throw new NullArgumentException();

        if (user.getUserStates().contains(WAITING_FOR_EMAIL_CONFIRMATION))
            throw new AccessDeniedUnconfirmedEmailException();

        if (user.getUserStates().contains(BLOCKED_BY_MANY_INVALID_LOGIN_ATTEMPT))
            throw new AccessDeniedBlockedUserException();

        if (user.getUserStates().contains(INACTIVE))
            return false;

        if (user.getCredential().isPasswordValid(password)) {
            removeAllInvalidLoginAttempts(user);
            UserRepository.getInstance().update(user);
            SessionService.addUserInSession(user);
            return true;
        } else {
            user.addUserState(INVALID_LOGIN_ATTEMPT);

            int invalidAttempts = 0;
            for (EUserState userState : user.getUserStates()) {
                if (userState == INVALID_LOGIN_ATTEMPT)
                    invalidAttempts++;
            }
            if (invalidAttempts == INVALID_ATTEMPTS_ALLOWED) {
                removeAllInvalidLoginAttempts(user);
                user.addUserState(BLOCKED_BY_MANY_INVALID_LOGIN_ATTEMPT);
            }
            UserRepository.getInstance().update(user);
            return false;
        }
    }

    private void removeAllInvalidLoginAttempts(IUser user) {
        while (user.getUserStates().contains(INVALID_LOGIN_ATTEMPT))
            user.removeUserState(INVALID_LOGIN_ATTEMPT);
    }

}
