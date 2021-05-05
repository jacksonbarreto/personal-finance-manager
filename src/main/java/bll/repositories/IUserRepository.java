package bll.repositories;

import bll.entities.IUser;
import bll.exceptions.NullArgumentException;

import java.util.Set;
import java.util.function.Predicate;

public interface IUserRepository extends IRepository<IUser> {
    /**
     * Returns the first user that meets the predicate or null if none are found.
     *
     * @param predicate to be tested.
     * @return the first user that meets the predicate or null if none are found.
     * @throws NullArgumentException if any of the parameters are null.
     */
    IUser getFirst(Predicate<IUser> predicate);

    Set<IUser> getAll();

}
