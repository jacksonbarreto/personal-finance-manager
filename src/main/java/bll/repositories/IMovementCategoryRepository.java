package bll.repositories;

import bll.entities.IMovementCategory;
import bll.exceptions.NullArgumentException;

import java.util.Set;

public interface IMovementCategoryRepository extends IRepository<IMovementCategory> {

    /**
     * Returns all movement categories, that is, public and user categories.
     *
     * @return all movement categories, that is, public and user categories.
     * @throws NullArgumentException if any of the parameters are null.
     */
    Set<IMovementCategory> getAll();
}
