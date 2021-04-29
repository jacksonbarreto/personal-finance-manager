package bll.services;

import bll.entities.IMovementCategory;
import bll.entities.IUser;
import bll.repositories.IUserRepository;
import bll.repositories.UserRepository;

public class CategoryService implements ICategoryService {
    private final IUserRepository userRepository;

    public CategoryService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static ICategoryService defaultCategoryService() {
        return new CategoryService(UserRepository.getInstance());
    }

    @Override
    public boolean registerCategory(IMovementCategory category) {
        try {
            IUser user = SessionService.getCurrentUser();
            user.addCategory(category);
            this.userRepository.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCategory(IMovementCategory category) {
        try {
            IUser user = SessionService.getCurrentUser();
            user.removeCategory(category);
            this.userRepository.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
