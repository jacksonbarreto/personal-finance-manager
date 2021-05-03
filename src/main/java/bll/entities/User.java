package bll.entities;

import bll.enumerators.ERole;
import bll.enumerators.EUserState;
import bll.exceptions.*;
import bll.valueObjects.IEmail;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "systemUser", uniqueConstraints = @UniqueConstraint(name = "unique_email", columnNames = {"email"}))
public class User implements IUser {
    @Id
    private UUID ID;

    @Column(nullable = false, length = MAXIMUM_NAME_SIZE)
    private String name;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @OneToOne(targetEntity = Credential.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false)
    private ICredential credential;

    @ElementCollection(targetClass = EUserState.class, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "systemUserState", joinColumns = @JoinColumn(name = "userState", nullable = false))
    @Column(name = "systemUser", nullable = false)
    @OrderColumn
    private List<EUserState> userStates;

    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "systemUserRole", joinColumns = @JoinColumn(name = "role", nullable = false))
    @Column(name = "systemUser", nullable = false)
    @OrderColumn
    private List<ERole> roles;

    @Column(nullable = false, unique = true)
    private IEmail email;

    @OneToMany(targetEntity = Wallet.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "systemUser")
    private Set<IWallet> wallets;

    @OneToMany(targetEntity = Payee.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "systemUser")
    private Set<IPayee> payees;

    @OneToMany(targetEntity = MovementCategory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "systemUser")
    private Set<IMovementCategory> categories;

    public User(String name, ICredential credential, List<EUserState> userStates, List<ERole> roles, IEmail email) {
        if (name == null || credential == null || userStates == null || roles == null || email == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        if (userStates.isEmpty())
            throw new ForbiddenLeaveUserStatelessException();
        if (roles.isEmpty())
            throw new ForbiddenLeaveUserWithoutFunctionsException();

        this.ID = UUID.randomUUID();
        this.name = name.trim();
        this.registrationDate = LocalDate.now();
        this.credential = credential;
        this.userStates = new ArrayList<>();
        this.userStates.addAll(userStates);
        this.roles = new ArrayList<>();
        this.roles.addAll(roles);
        this.email = email;
        this.wallets = new HashSet<>();
        this.payees = new HashSet<>();
        this.categories = new HashSet<>();
        this.credential.addAccessKey(this.email.getEmail());
        this.credential.addAccessKey(this.name);
    }

    private User(IUser user) {
        this.ID = user.getID();
        this.name = user.getName();
        this.registrationDate = user.getRegistrationDate();
        this.credential = user.getCredential();
        this.userStates = user.getUserStates();
        this.roles = user.getRoles();
        this.email = user.getEmail();
        this.wallets = user.getWallets();
        this.payees = user.getPayee();
        this.categories = user.getCategory();
    }

    /**
     * Returns the unique identifier of the User.
     *
     * @return the unique identifier of the User.
     */
    @Override
    public UUID getID() {
        return this.ID;
    }

    /**
     * Returns the name of the User.
     *
     * @return the name of the User.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Updates the user name.
     *
     * @param newName for user.
     * @throws NullArgumentException    if the parameter is null.
     * @throws InvalidNameSizeException if the user name does not respect the name size policies.
     */
    @Override
    public void updateName(String newName) {
        if (newName == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(newName.trim()))
            throw new InvalidNameSizeException();
        this.credential.removeAccessKey(this.name);
        this.name = newName.trim();
        this.credential.addAccessKey(this.name);
    }

    /**
     * Returns the user's email.
     *
     * @return the user's email.
     */
    @Override
    public IEmail getEmail() {
        return this.email;
    }

    /**
     * Updates the user e-mail.
     *
     * @param newEmail for user.
     * @throws NullArgumentException if the parameter is null.
     */
    @Override
    public void updateEmail(IEmail newEmail) {
        if (newEmail == null)
            throw new NullArgumentException();

        this.credential.removeAccessKey(this.email.getEmail());
        this.email = newEmail;
        this.credential.addAccessKey(this.email.getEmail());
    }

    /**
     * Returns the user's registration date.
     *
     * @return the user's registration date.
     */
    @Override
    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    /**
     * Returns the user's credential.
     *
     * @return the user's credential.
     */
    @Override
    public ICredential getCredential() {
        return this.credential;
    }

    /**
     * Returns the user's states.
     *
     * @return the user's states.
     */
    @Override
    public List<EUserState> getUserStates() {
        return new ArrayList<>(this.userStates);
    }

    /**
     * Returns the user's roles.
     *
     * @return the user's roles.
     */
    @Override
    public List<ERole> getRoles() {
        return new ArrayList<>(this.roles);
    }

    /**
     * Returns the user's wallets.
     *
     * @return the user's wallets.
     */
    @Override
    public Set<IWallet> getWallets() {
        Set<IWallet> walletSet = new HashSet<>();
        for (IWallet w : this.wallets)
            walletSet.add(w.clone());
        return walletSet;
    }

    /**
     * Adds a user state.
     *
     * @param userState to be added.
     * @throws NullArgumentException if the parameter is null.
     */
    @Override
    public void addUserState(EUserState userState) {
        if (userState == null)
            throw new NullArgumentException();
        this.userStates.add(userState);
    }

    /**
     * Removes a user state.
     *
     * @param userState to be removed.
     * @throws NullArgumentException                if the parameter is null.
     * @throws NonExistentUserStateException        if you try to remove a user state that does not exist.
     * @throws ForbiddenLeaveUserStatelessException if you try to leave the user stateless.
     */
    @Override
    public void removeUserState(EUserState userState) {
        if (userState == null)
            throw new NullArgumentException();
        if (!this.userStates.contains(userState))
            throw new NonExistentUserStateException();
        if (this.userStates.size() == 1)
            throw new ForbiddenLeaveUserStatelessException();

        this.userStates.remove(userState);
    }

    /**
     * Adds a user role.
     *
     * @param role to be added.
     * @throws NullArgumentException if the parameter is null.
     */
    @Override
    public void addRole(ERole role) {
        if (role == null)
            throw new NullArgumentException();
        this.roles.add(role);
    }

    /**
     * Removes a role from the user.
     *
     * @param role to be removed.
     * @throws NullArgumentException                       if the parameter is null.
     * @throws NonExistentRoleException                    if you try to remove a role that does not exist.
     * @throws ForbiddenLeaveUserWithoutFunctionsException if you try to remove all user roles.
     */
    @Override
    public void removeRole(ERole role) {
        if (role == null)
            throw new NullArgumentException();
        if (!this.roles.contains(role))
            throw new NonExistentRoleException();
        if (this.roles.size() == 1)
            throw new ForbiddenLeaveUserWithoutFunctionsException();

        this.roles.remove(role);
    }

    /**
     * Adds a user wallet.
     *
     * @param wallet a user wallet.
     * @throws NullArgumentException   if the parameter is null.
     * @throws ExistingWalletException if you try to add a wallet that already exists.
     */
    @Override
    public void addWallet(IWallet wallet) {
        if (wallet == null)
            throw new NullArgumentException();
        if (this.wallets.contains(wallet))
            throw new ExistingWalletException();
        this.wallets.add(wallet.clone());
    }

    /**
     * Update a wallet of the user portfolio.
     *
     * @param wallet to be updated.
     * @throws NullArgumentException      if the parameter is null.
     * @throws NonExistentWalletException if the wallet to be updated does not exist in the user's portfolio.
     */
    @Override
    public void updateWallet(IWallet wallet) {
        if (wallet == null)
            throw new NullArgumentException();
        if (!this.wallets.contains(wallet))
            throw new NonExistentWalletException();
        IWallet walletToUpdate = fetchWallet(wallet);
        walletToUpdate.autoUpdate(wallet);
    }

    /**
     * Removes a user wallet.
     *
     * @param wallet to be removed.
     * @throws NullArgumentException      if the parameter is null.
     * @throws NonExistentWalletException if the wallet to be removed does not exist in the user's portfolio.
     */
    @Override
    public void removeWallet(IWallet wallet) {
        if (wallet == null)
            throw new NullArgumentException();

        if (!this.wallets.contains(wallet))
            throw new NonExistentWalletException();

        this.wallets.remove(wallet);
    }

    /**
     * Returns a collection with the user's payees.
     *
     * @return a collection with the user's payees.
     */
    @Override
    public Set<IPayee> getPayee() {
        Set<IPayee> payeeSet = new HashSet<>();
        for (IPayee p : this.payees)
            payeeSet.add(p.clone());
        return payeeSet;
    }

    /**
     * Adds a payee to the user's list of payees.
     *
     * @param payee to be added.
     * @throws NullArgumentException  if the parameter is null.
     * @throws ExistingPayeeException if you try to add a payee that already exists.
     */
    @Override
    public void addPayee(IPayee payee) {
        if (payee == null)
            throw new NullArgumentException();
        if (this.payees.contains(payee))
            throw new ExistingPayeeException();
        this.payees.add(payee.clone());
    }

    /**
     * Removes a payee to the user´s list of payees.
     *
     * @param payee to be removed.
     * @throws NullArgumentException     if the parameter is null.
     * @throws NonExistingPayeeException if you try to remove a payee that doesn't exist.
     */
    @Override
    public void removePayee(IPayee payee) {
        if (payee == null)
            throw new NullArgumentException();
        if (!this.payees.contains(payee))
            throw new NonExistingPayeeException();
        this.payees.remove(payee);
    }

    /**
     * Updates a payee.
     *
     * @param payee to be updated.
     * @throws NullArgumentException     if the parameter is null.
     * @throws NonExistingPayeeException if you try to update a payee that doesn't exist.
     */
    @Override
    public void updatePayee(IPayee payee) {
        if (payee == null)
            throw new NullArgumentException();
        if (!this.payees.contains(payee))
            throw new NonExistingPayeeException();
        this.payees.remove(payee);
        this.payees.add(payee.clone());
    }

    /**
     * Returns a collection with the user's categories of operations.
     *
     * @return a collection with the user's categories of operations.
     */
    @Override
    public Set<IMovementCategory> getCategory() {
        Set<IMovementCategory> categorySet = new HashSet<>();
        for (IMovementCategory c : this.categories)
            if (c.isActive())
                categorySet.add(c.clone());
        return categorySet;
    }

    /**
     * Adds an operation category to the user's personal list of categories.
     *
     * @param category to be added.
     * @throws NullArgumentException     if the parameter is null.
     * @throws ExistingCategoryException if you try to add a category that already exists.
     */
    @Override
    public void addCategory(IMovementCategory category) {
        if (category == null)
            throw new NullArgumentException();
        if (this.categories.contains(category))
            throw new ExistingCategoryException();

        this.categories.add(category.clone());
    }

    /**
     * Updates a category.
     *
     * @param category to be updated.
     * @throws NullArgumentException        if the parameter is null.
     * @throws NonExistingCategoryException if you try to update a category that doesn't exist.
     */
    @Override
    public void updateCategory(IMovementCategory category) {
        if (category == null)
            throw new NullArgumentException();
        if (!this.categories.contains(category))
            throw new NonExistingCategoryException();
        IMovementCategory categoryToUpdate = fetchCategory(category);
        categoryToUpdate.autoUpdate(category);
    }

    /**
     * Removes the indicated category.
     *
     * @param category to be removed.
     * @throws NullArgumentException        if the parameter is null.
     * @throws NonExistingCategoryException if you try to update a category that doesn't exist.
     */
    @Override
    public void removeCategory(IMovementCategory category) {
        if (category == null)
            throw new NullArgumentException();
        if (!this.categories.contains(category))
            throw new NonExistingCategoryException();
        IMovementCategory categoryToRemove = fetchCategory(category);
        categoryToRemove.inactivate();
    }

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    @Override
    public IUser clone() {
        return new User(this);
    }

    /**
     * Indicates whether some other User is "equal to" this one.
     * <p>
     * It is only the same when both User have all their attributes equal.
     *
     * @param o an object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    @Override
    public boolean isDeepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID.equals(user.ID) && name.equals(user.name) && registrationDate.equals(user.registrationDate) && credential.equals(user.credential) && userStates.equals(user.userStates) && roles.equals(user.roles) && email.equals(user.email) && wallets.equals(user.wallets) && payees.equals(user.payees) && categories.equals(user.categories);
    }

    /**
     * Indicates whether some other User is "equal to" this one.
     * <p>
     * It is only the same when both objects have ID equal.
     *
     * @param o an instance of Object.
     * @return {@code true} only if both objects have the same ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID.equals(user.ID);
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", registrationDate=" + registrationDate +
                ", credential=" + credential +
                ", userStates=" + userStates +
                ", roles=" + roles +
                ", email=" + email +
                ", wallets=" + wallets +
                ", payees=" + payees +
                ", categories=" + categories +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }


    private IMovementCategory fetchCategory(IMovementCategory category) {
        IMovementCategory foundCategory = null;
        for (IMovementCategory c : this.categories)
            if (c.equals(category)) {
                foundCategory = c;
                break;
            }
        return foundCategory;
    }

    private IWallet fetchWallet(IWallet wallet) {
        IWallet foundWallet = null;
        for (IWallet w : this.wallets)
            if (w.equals(wallet)) {
                foundWallet = w;
                break;
            }
        return foundWallet;
    }

    protected User() {
    }

    @SuppressWarnings("unused")
    private void setID(UUID ID) {
        this.ID = ID;
    }

    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    private void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @SuppressWarnings("unused")
    private void setCredential(ICredential credential) {
        this.credential = credential;
    }

    @SuppressWarnings("unused")
    private void setUserStates(List<EUserState> userStates) {
        this.userStates = userStates;
    }

    @SuppressWarnings("unused")
    private void setRoles(List<ERole> roles) {
        this.roles = roles;
    }

    @SuppressWarnings("unused")
    private void setEmail(IEmail email) {
        this.email = email;
    }

    @SuppressWarnings("unused")
    private void setWallets(Set<IWallet> wallets) {
        this.wallets = wallets;
    }

    @SuppressWarnings("unused")
    private Set<IPayee> getPayees() {
        return payees;
    }

    @SuppressWarnings("unused")
    private void setPayees(Set<IPayee> payees) {
        this.payees = payees;
    }

    @SuppressWarnings("unused")
    private Set<IMovementCategory> getCategories() {
        return categories;
    }

    @SuppressWarnings("unused")
    private void setCategories(Set<IMovementCategory> categories) {
        this.categories = categories;
    }
}
