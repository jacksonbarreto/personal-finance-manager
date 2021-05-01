package bll.repositories;

import bll.entities.IMovementCategory;
import bll.exceptions.NullArgumentException;
import bll.exceptions.UserIsNotAuthorizedForActionException;
import bll.services.SessionService;
import dal.infra.IDAO;
import dal.infra.MovementCategoryDAO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static bll.enumerators.ERole.ADMIN;
import static bll.services.PermissionService.permissionServiceDefault;

public class MovementCategoryRepository implements IMovementCategoryRepository {
    private final IDAO<IMovementCategory> categoryDAO;

    public MovementCategoryRepository(IDAO<IMovementCategory> categoryDAO) {
        if (categoryDAO == null)
            throw new NullArgumentException();
        this.categoryDAO = categoryDAO;
    }

    private MovementCategoryRepository() {
        this(MovementCategoryDAO.getInstance());
    }

    public static IMovementCategoryRepository getInstance() {
        return new MovementCategoryRepository();
    }

    @Override
    public Set<IMovementCategory> getAll() {
        return new HashSet<>(categoryDAO.select("select m from MovementCategory m where active = 1 and publicCategory = 1 "));
    }

    /**
     * Returns all public movement categories.
     *
     * @return all public movement categories.
     */
    @Override
    public Set<IMovementCategory> getOnlyPublic() {
        return new HashSet<>(categoryDAO.select("select m from MovementCategory m where active = 1 and publicCategory = 1 "));
    }

    @Override
    public Set<IMovementCategory> get(Predicate<IMovementCategory> predicate) {
        if (predicate == null)
            throw new NullArgumentException();
        Set<IMovementCategory> categoryFound = new HashSet<>();
        for (IMovementCategory category : getAll())
            if (predicate.test(category))
                categoryFound.add(category);
        return categoryFound;
    }

    @Override
    public IMovementCategory get(UUID id) {
        if (id == null)
            throw new NullArgumentException();
        return categoryDAO.select(id);
    }

    @Override
    public void add(IMovementCategory element) {
        if (element == null)
            throw new NullArgumentException();
        Set<IMovementCategory> publicCategory = getOnlyPublic();
        if (permissionServiceDefault().hasRole(SessionService.getCurrentUser(), ADMIN) && element.isPublic()) {
            if (!publicCategory.contains(element))
                categoryDAO.create(element);
        } else throw new UserIsNotAuthorizedForActionException();
    }

    @Override
    public void update(IMovementCategory element) {
        if (element == null)
            throw new NullArgumentException();
        if (permissionServiceDefault().hasRole(SessionService.getCurrentUser(), ADMIN) && element.isPublic())
            categoryDAO.update(element);
        else
            throw new UserIsNotAuthorizedForActionException();
    }

    @Override
    public void remove(IMovementCategory element) {
        if (element == null)
            throw new NullArgumentException();
        if (permissionServiceDefault().hasRole(SessionService.getCurrentUser(), ADMIN) && element.isPublic()) {
            element.inactivate();
            categoryDAO.update(element);
        } else
            throw new UserIsNotAuthorizedForActionException();
    }
}
