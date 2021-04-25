package dal.infra;

import bll.entities.IUser;
import bll.entities.User;

import java.util.List;
import java.util.UUID;

import static dal.infra.IDAO.executeInsideTransaction;
import static dal.infra.IDAO.getEntityManager;

public class UserDAO implements IDAO<IUser> {


    @Override
    public List<IUser> selectAll() {
        return getEntityManager().createQuery("select t from " + User.class.getSimpleName() + " t", IUser.class).getResultList();
    }

    @Override
    public IUser select(UUID id) {
        return getEntityManager().find(User.class, id);
    }

    @Override
    public void create(IUser element) {
        executeInsideTransaction(entityManager -> entityManager.persist(element));
    }

    @Override
    public void update(IUser element) {
        executeInsideTransaction(entityManager -> entityManager.merge(element));
    }

    @Override
    public void delete(IUser element) {

        executeInsideTransaction(entityManager -> entityManager.remove(element));
    }

    public static IDAO<IUser> getInstance() {
        return new UserDAO();
    }

    private UserDAO() {
    }
}
