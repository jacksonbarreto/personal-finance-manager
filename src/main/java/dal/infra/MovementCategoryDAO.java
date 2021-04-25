package dal.infra;

import bll.entities.IMovementCategory;
import bll.entities.MovementCategory;

import java.util.List;
import java.util.UUID;

import static dal.infra.IDAO.*;
import static dal.infra.IDAO.getEntityManager;

public class MovementCategoryDAO implements IDAO<IMovementCategory> {

    @Override
    public List<IMovementCategory> selectAll() {
        return getEntityManager().createQuery("select t from " + MovementCategory.class.getSimpleName() + " t", IMovementCategory.class).getResultList();
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
