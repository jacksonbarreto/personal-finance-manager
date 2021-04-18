package bll.repositories;

import bll.entities.IUser;
import bll.exceptions.NullArgumentException;
import dal.infra.IDAO;
import dal.infra.UserDAO;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

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

        return userDAO.select(predicate);
    }

    @Override
    public IUser getFirst(Predicate<IUser> predicate) {
        if (predicate == null)
            throw new NullArgumentException();
        return (IUser) userDAO.select(predicate).toArray()[0];
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
