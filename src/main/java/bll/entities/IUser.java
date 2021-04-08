package bll.entities;

import bll.enumerators.ERole;
import bll.enumerators.EUserState;
import bll.exceptions.*;
import bll.valueObjects.IEmail;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IUser extends Serializable {
    int MINIMUM_NAME_SIZE = 3;
    int MAXIMUM_NAME_SIZE = 255;
    Predicate<String> INCORRECT_NAME_SIZE = (s) -> (s.length() < MINIMUM_NAME_SIZE || s.length() > MAXIMUM_NAME_SIZE);

    /**
     * Returns the unique identifier of the User.
     *
     * @return the unique identifier of the User.
     */
    UUID getID();

    /**
     * Returns the name of the User.
     *
     * @return the name of the User.
     */
    String getName();

    /**
     * Updates the user name.
     *
     * @param newName for user.
     * @throws NullArgumentException    if the parameter is null.
     * @throws InvalidNameSizeException if the user name does not respect the name size policies.
     */
    void updateName(String newName);

    /**
     * Returns the user's email.
     *
     * @return the user's email.
     */
    IEmail getEmail();

    /**
     * Updates the user e-mail.
     *
     * @param newEmail for user.
     * @throws NullArgumentException if the parameter is null.
     */
    void updateEmail(IEmail newEmail);

    /**
     * Returns the user's registration date.
     *
     * @return the user's registration date.
     */
    LocalDate getRegistrationDate();

    /**
     * Returns the user's credential.
     *
     * @return the user's credential.
     */
    ICredential getCredential();

    /**
     * Returns the user's states.
     *
     * @return the user's states.
     */
    List<EUserState> getUserStates();

    /**
     * Returns the user's roles.
     *
     * @return the user's roles.
     */
    List<ERole> getRoles();

    /**
     * Returns the user's wallets.
     *
     * @return the user's wallets.
     */
    Set<IWallet> getWallets();

    /**
     * Adds a user state.
     *
     * @param userState to be added.
     * @throws NullArgumentException if the parameter is null.
     */
    void addUserState(EUserState userState);

    /**
     * Removes a user state.
     *
     * @param userState to be removed.
     * @throws NullArgumentException                if the parameter is null.
     * @throws NonExistentUserStateException        if you try to remove a user state that does not exist.
     * @throws ForbiddenLeaveUserStatelessException if you try to leave the user stateless.
     */
    void removeUserState(EUserState userState);

    /**
     * Adds a user role.
     *
     * @param role to be added.
     * @throws NullArgumentException if the parameter is null.
     */
    void addRole(ERole role);

    /**
     * Removes a role from the user.
     *
     * @param role to be removed.
     * @throws NullArgumentException                       if the parameter is null.
     * @throws NonExistentRoleException                    if you try to remove a role that does not exist.
     * @throws ForbiddenLeaveUserWithoutFunctionsException if you try to remove all user roles.
     */
    void removeRole(ERole role);

    /**
     * Adds a user wallet.
     *
     * @param wallet a user wallet.
     * @throws NullArgumentException   if the parameter is null.
     * @throws ExistingWalletException if you try to add a wallet that already exists.
     */
    void addWallet(IWallet wallet);

    /**
     * Removes a user wallet.
     *
     * @param wallet to be removed.
     * @throws NullArgumentException      if the parameter is null.
     * @throws NonExistentWalletException if the wallet to be removed does not exist in the user's portfolio.
     */
    void removeWallet(IWallet wallet);

    /**
     * Update a wallet of the user portfolio.
     *
     * @param wallet to be updated.
     * @throws NullArgumentException      if the parameter is null.
     * @throws NonExistentWalletException if the wallet to be updated does not exist in the user's portfolio.
     */
    void updateWallet(IWallet wallet);

    /**
     * Returns a collection with the user's payees.
     *
     * @return a collection with the user's payees.
     */
    Set<IPayee> getPayee();

    /**
     * Adds a payee to the user's list of payees.
     *
     * @param payee to be added.
     * @throws NullArgumentException  if the parameter is null.
     * @throws ExistingPayeeException if you try to add a payee that already exists.
     */
    void addPayee(IPayee payee);

    /**
     * Removes a payee to the user´s list of payees.
     *
     * @param payee to be removed.
     * @throws NullArgumentException     if the parameter is null.
     * @throws NonExistingPayeeException if you try to remove a payee that doesn't exist.
     */
    void removePayee(IPayee payee);

    /**
     * Updates a payee.
     *
     * @param payee to be updated.
     * @throws NullArgumentException     if the parameter is null.
     * @throws NonExistingPayeeException if you try to update a payee that doesn't exist.
     */
    void updatePayee(IPayee payee);

    /**
     * Returns a collection with the user's categories of operations.
     *
     * @return a collection with the user's categories of operations.
     */
    Set<IMovementCategory> getCategory();

    /**
     * Adds an operation category to the user's personal list of categories.
     *
     * @param category to be added.
     * @throws NullArgumentException     if the parameter is null.
     * @throws ExistingCategoryException if you try to add a category that already exists.
     */
    void addCategory(IMovementCategory category);

    /**
     * Updates a category.
     *
     * @param category to be updated.
     * @throws NullArgumentException        if the parameter is null.
     * @throws NonExistingCategoryException if you try to update a category that doesn't exist.
     */
    void updateCategory(IMovementCategory category);

    /**
     * Indicates whether some other User is "equal to" this one.
     * <p>
     * It is only the same when both objects have ID equal.
     *
     * @param o an instance of Object.
     * @return {@code true} only if both objects have the same ID.
     */
    boolean equals(Object o);

    /**
     * Indicates whether some other User is "equal to" this one.
     * <p>
     * It is only the same when both User have all their attributes equal.
     *
     * @param o an object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    boolean isDeepEquals(Object o);

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    IUser clone();
}
