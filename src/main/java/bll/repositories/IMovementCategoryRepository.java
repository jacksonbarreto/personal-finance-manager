package bll.repositories;

import bll.entities.IMovementCategory;

import java.util.Set;

public interface IMovementCategoryRepository extends IRepository<IMovementCategory> {

    /**
     * Returns all movement categories, that is, public and user categories.
     *
     * @return all movement categories, that is, public and user categories.
     */
    Set<IMovementCategory> getAll();

    /**
     * Returns all public movement categories.
     *
     * @return all public movement categories.
     */
    Set<IMovementCategory> getOnlyPublic();
}
