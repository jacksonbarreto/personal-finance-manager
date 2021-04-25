package bll.services;

import bll.entities.IUser;
import bll.exceptions.NullArgumentException;
import bll.exceptions.SessionAlreadyHasUserException;

import java.time.Duration;
import java.time.Instant;

public class SessionService {
    private static final int INACTIVITY_ALLOWED_IN_MINUTES = 3;
    private static final int SECONDS = 60;
    private static SessionService INSTANCE;
    private Instant lastInteraction;
    private IUser currentUser;

    private SessionService() {
    }

    private static SessionService getInstance() {
        if (INSTANCE == null) {
            synchronized (SessionService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionService();
                }
            }
        }
        return INSTANCE;
    }

    public static boolean isValid() {
        if (getInstance().currentUser == null)
            return false;
        Duration interval = Duration.between(getInstance().lastInteraction, Instant.now());
        getInstance().lastInteraction = Instant.now();
        return interval.getSeconds() <= (INACTIVITY_ALLOWED_IN_MINUTES * SECONDS);
    }

    public static IUser getCurrentUser() {
        return getInstance().currentUser == null ? null : getInstance().currentUser.clone();
    }

    public static void addUserInSession(IUser user) {
        if (user == null)
            throw new NullArgumentException();
        if (getInstance().currentUser != null)
            throw new SessionAlreadyHasUserException();
        getInstance().currentUser = user.clone();
    }


}
