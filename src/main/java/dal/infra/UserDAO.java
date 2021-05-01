package dal.infra;

import bll.entities.IUser;
import bll.entities.User;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dal.infra.EntityManagerSingleton.getEntityManager;
import static dal.infra.IDAO.executeInsideTransaction;

public class UserDAO implements IDAO<IUser> {

    public List<IUser> selectAll() {
        return new ArrayList<>(getEntityManager().createQuery("select t from User t", User.class).getResultList());
    }

    @Override
    public List<IUser> select(String query) {
        List<IUser> users;
        TypedQuery<IUser> typedQuery = getEntityManager().createQuery(query, IUser.class);
        users = typedQuery.getResultList();
        return users;
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
