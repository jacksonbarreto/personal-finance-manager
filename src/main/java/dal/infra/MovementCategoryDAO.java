package dal.infra;

import bll.entities.IMovementCategory;
import bll.entities.MovementCategory;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dal.infra.EntityManagerSingleton.getEntityManager;
import static dal.infra.IDAO.executeInsideTransaction;

public class MovementCategoryDAO implements IDAO<IMovementCategory> {

    @Override
    public List<IMovementCategory> select(String query) {
        List<IMovementCategory> movementCategories;
        TypedQuery<MovementCategory> typedQuery = getEntityManager().createQuery(query, MovementCategory.class);
        movementCategories = new ArrayList<>(typedQuery.getResultList());
        return movementCategories;
    }

    @Override
    public IMovementCategory select(UUID id) {
        return getEntityManager().find(MovementCategory.class, id);
    }

    @Override
    public void create(IMovementCategory element) {
        executeInsideTransaction(entityManager -> entityManager.persist(element));
    }

    @Override
    public void update(IMovementCategory element) {
        executeInsideTransaction(entityManager -> entityManager.merge(element));
    }

    @Override
    public void delete(IMovementCategory element) {
        executeInsideTransaction(entityManager -> entityManager.remove(element));
    }

    private MovementCategoryDAO() {
    }

    public static IDAO<IMovementCategory> getInstance() {
        return new MovementCategoryDAO();
    }
}
