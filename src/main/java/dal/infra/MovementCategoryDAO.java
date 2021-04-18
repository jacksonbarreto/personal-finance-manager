package dal.infra;

import bll.entities.IMovementCategory;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class MovementCategoryDAO implements IDAO<IMovementCategory> {

    private MovementCategoryDAO() {
    }

    public static IDAO<IMovementCategory> getInstance() {
        return new MovementCategoryDAO();
    }


    @Override
    public Set<IMovementCategory> select(Predicate<IMovementCategory> predicate) {
        return null;
    }

    @Override
    public IMovementCategory select(UUID id) {
        return null;
    }

    @Override
    public void create(IMovementCategory element) {

    }

    @Override
    public void update(IMovementCategory element) {

    }

    @Override
    public void delete(IMovementCategory element) {

    }
}
