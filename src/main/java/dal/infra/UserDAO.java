package dal.infra;

import bll.entities.IUser;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class UserDAO implements IDAO<IUser> {
    @Override
    public Set<IUser> select(Predicate<IUser> predicate) {
        return null;
    }

    @Override
    public IUser select(UUID id) {
        return null;
    }

    @Override
    public void create(IUser element) {

    }

    @Override
    public void update(IUser element) {

    }

    @Override
    public void delete(IUser element) {

    }

    private UserDAO() {
    }

    public static IDAO<IUser> getInstance() {
        return new UserDAO();
    }
}
