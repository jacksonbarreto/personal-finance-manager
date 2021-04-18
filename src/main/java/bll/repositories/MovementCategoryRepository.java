package bll.repositories;

import bll.entities.IMovementCategory;
import bll.exceptions.NullArgumentException;
import bll.services.SessionService;
import dal.infra.IDAO;
import dal.infra.MovementCategoryDAO;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class MovementCategoryRepository implements IMovementCategoryRepository {
    private final IDAO<IMovementCategory> categoryDAO;

    public MovementCategoryRepository(IDAO<IMovementCategory> categoryDAO) {
        if (categoryDAO == null)
            throw new NullArgumentException();
        this.categoryDAO = categoryDAO;
    }

    private MovementCategoryRepository() {
        this.categoryDAO = MovementCategoryDAO.getInstance();
    }

    public static IMovementCategoryRepository getInstance() {
        return new MovementCategoryRepository();
    }

    @Override
    public Set<IMovementCategory> getAll() {
        Set<IMovementCategory> allCategories = new HashSet<>();
        Predicate<IMovementCategory> predicate = category -> category.isActive() && category.isPublic();
        allCategories.addAll(categoryDAO.select(predicate));
        allCategories.addAll(SessionService.getCurrentUser().getCategory());
        return allCategories;
    }

    @Override
    public Set<IMovementCategory> get(Predicate<IMovementCategory> predicate) {
        if (predicate == null)
            throw new NullArgumentException();
        return categoryDAO.select(predicate);
    }

    @Override
    public IMovementCategory get(UUID id) {
        return categoryDAO.select(id);
    }

    @Override
    public void add(IMovementCategory element) {
        if (element == null)
            throw new NullArgumentException();
        categoryDAO.create(element);
    }

    @Override
    public void update(IMovementCategory element) {
        if (element == null)
            throw new NullArgumentException();
        categoryDAO.update(element);
    }

    @Override
    public void remove(IMovementCategory element) {
        if (element == null)
            throw new NullArgumentException();
        categoryDAO.delete(element);
    }
}
