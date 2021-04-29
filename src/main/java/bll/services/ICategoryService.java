package bll.services;

import bll.entities.IMovementCategory;

public interface ICategoryService {

    boolean registerCategory(IMovementCategory category);

    boolean removeCategory(IMovementCategory category);

    boolean updateCategory(IMovementCategory category);
}
