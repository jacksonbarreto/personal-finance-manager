package bll.services;

import bll.entities.IMovementCategory;
import bll.entities.IUser;
import bll.enumerators.ERole;
import bll.exceptions.UserIsNotAuthorizedForActionException;
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
            IUser user = SessionService.getCurrentUser();
        if (category.isPublic() && !PermissionService.permissionServiceDefault().hasRole(user, ERole.ADMIN))
            throw new UserIsNotAuthorizedForActionException();
        try {
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
            IUser user = SessionService.getCurrentUser();
        if (category.isPublic() && !PermissionService.permissionServiceDefault().hasRole(user, ERole.ADMIN))
            throw new UserIsNotAuthorizedForActionException();
        try {
            user.removeCategory(category);
            this.userRepository.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCategory(IMovementCategory category) {
            IUser user = SessionService.getCurrentUser();
        if (category.isPublic() && !PermissionService.permissionServiceDefault().hasRole(user, ERole.ADMIN))
            throw new UserIsNotAuthorizedForActionException();
        try {
            user.updateCategory(category);
            this.userRepository.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
