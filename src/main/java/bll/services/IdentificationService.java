package bll.services;

import bll.entities.IUser;
import bll.exceptions.NullArgumentException;
import bll.repositories.IUserRepository;
import bll.repositories.UserRepository;

import java.util.function.Predicate;

public class IdentificationService implements IIdentificationService {

    private final IUserRepository userRepository;

    public IdentificationService(IUserRepository userRepository) {
        if (userRepository == null)
            throw new NullArgumentException();
        this.userRepository = userRepository;
    }

    @Override
    public IUser identifyUser(String accessKey) {
        if (accessKey == null)
            throw new NullArgumentException();

        Predicate<IUser> predicate = user -> user.getCredential().getAccessKeys().contains(accessKey);
        return this.userRepository.getFirst(predicate);
    }

    @Override
    public boolean isValid(IUser user) {
        if (user == null)
            throw new NullArgumentException();

        IUser userFound = this.userRepository.get(user.getID());
        if (userFound == null)
            return false;
        return userFound.getCredential().equals(user.getCredential());
    }

    public static IIdentificationService identificationServiceDefault() {
        return new IdentificationService();
    }

    private IdentificationService() {
        this.userRepository = UserRepository.getInstance();
    }
}
