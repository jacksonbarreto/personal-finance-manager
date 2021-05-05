package bll.repositories;

import bll.entities.IUser;
import bll.exceptions.NullArgumentException;
import dal.infra.IDAO;
import dal.infra.UserDAO;
import org.hibernate.event.internal.DefaultResolveNaturalIdEventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserRepository implements IUserRepository {
    private final IDAO<IUser> userDAO;

    public UserRepository(IDAO<IUser> userDAO) {
        if (userDAO == null)
            throw new NullArgumentException();

        this.userDAO = userDAO;
    }

    private UserRepository() {
        this.userDAO = UserDAO.getInstance();
    }

    public static IUserRepository getInstance() {
        return new UserRepository();
    }

    @Override
    public Set<IUser> get(Predicate<IUser> predicate) {
        if (predicate == null)
            throw new NullArgumentException();
        return userDAO.select("select t from User t").stream().filter(predicate).collect(Collectors.toSet());
    }


    @Override
    public Set<IUser> getAll() {
        return new HashSet<>(userDAO.select("select t from User t"));
    }

    @Override
    public IUser getFirst(Predicate<IUser> predicate) {
        if (predicate == null)
            throw new NullArgumentException();
        return userDAO.select("select t from User t").stream().filter(predicate).findFirst().orElse(null);
    }

    @Override
    public IUser get(UUID id) {
        if (id == null)
            throw new NullArgumentException();

        return userDAO.select(id);
    }

    @Override
    public void add(IUser element) {
        if (element == null)
            throw new NullArgumentException();

        userDAO.create(element);
    }

    @Override
    public void update(IUser element) {
        if (element == null)
            throw new NullArgumentException();
        userDAO.update(element);
    }

    @Override
    public void remove(IUser element) {
        if (element == null)
            throw new NullArgumentException();
        userDAO.delete(element);
    }
}
