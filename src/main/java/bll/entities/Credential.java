package bll.entities;

import bll.exceptions.InvalidAccessKeySizeException;
import bll.exceptions.InvalidPasswordSizeException;
import bll.exceptions.AccessKeyDoesNotExistException;
import bll.exceptions.AccessKeysCannotBeEmptyException;
import bll.exceptions.AccessKeyAlreadyExistsException;
import bll.exceptions.NullArgumentException;

import javax.persistence.*;
import java.util.*;

import static bll.entities.ICredential.createHashedPassword;
import static bll.entities.ICredential.getSaltRandom;

@Entity
final public class Credential implements ICredential {
    @Id
    private UUID ID;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private Set<String> accessKeys;
    @Column(nullable = false)
    private byte[] salt;
    @Column(nullable = false)
    private String hashedPassword;


    public Credential(String accessKey, String password) {
        if (accessKey == null || password == null)
            throw new NullArgumentException();
        if (INCORRECT_ACCESS_KEY_SIZE.test(accessKey.trim()))
            throw new InvalidAccessKeySizeException();
        if (INCORRECT_PASSWORD_SIZE.test(password.trim()))
            throw new InvalidPasswordSizeException();
        this.ID = UUID.randomUUID();
        this.salt = getSaltRandom();
        this.hashedPassword = createHashedPassword(password.trim(), this.salt);
        this.accessKeys = new HashSet<>();
        this.accessKeys.add(accessKey.trim());
    }

    /**
     * Returns the unique identifier of the Credential.
     *
     * @return the unique identifier of the Credential.
     */
    @Override
    public UUID getID() {
        return this.ID;
    }

    /**
     * Returns a collection of access keys.
     *
     * @return a collection of access keys.
     */
    @Override
    public List<String> getAccessKeys() {
        return new ArrayList<>(accessKeys);
    }

    /**
     * adds a new access key to the credential.
     *
     * @param accessKey new key to access the credential.
     * @throws NullArgumentException           if the argument is null.
     * @throws InvalidAccessKeySizeException   if the access key does not respect the size policy.
     * @throws AccessKeyAlreadyExistsException if the access key already exists in the credential.
     */
    @Override
    public void addAccessKey(String accessKey) {
        if (accessKey == null)
            throw new NullArgumentException();
        if (INCORRECT_ACCESS_KEY_SIZE.test(accessKey.trim()))
            throw new InvalidAccessKeySizeException();
        if (accessKeys.contains(accessKey.trim()))
            throw new AccessKeyAlreadyExistsException();

        this.accessKeys.add(accessKey.trim());
    }

    /**
     * Removes an access key from the credential.
     *
     * @param accessKey to be removed from the credential.
     * @throws NullArgumentException            if the argument is null.
     * @throws AccessKeysCannotBeEmptyException if you try to remove the last passkey.
     * @throws AccessKeyDoesNotExistException   if you try to remove a key that does not exist in the credential.
     */
    @Override
    public void removeAccessKey(String accessKey) {
        if (accessKey == null)
            throw new NullArgumentException();
        if (!accessKeys.contains(accessKey.trim()))
            throw new AccessKeyDoesNotExistException();
        if (accessKeys.size() == 1)
            throw new AccessKeysCannotBeEmptyException();
        accessKeys.remove(accessKey.trim());
    }

    /**
     * Checks whether the password provided is the same as the credential.
     *
     * @param passwordInPlainText password be verified.
     * @return {@code true} if the password provided is the same as the credential.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public boolean isPasswordValid(String passwordInPlainText) {
        if (passwordInPlainText == null)
            throw new NullArgumentException();
        return this.hashedPassword.equals(createHashedPassword(passwordInPlainText.trim(), this.salt));
    }

    /**
     * Updates the credential password.
     *
     * @param newPasswordInPlainText new password for the credential.
     * @throws NullArgumentException        if the argument is null.
     * @throws InvalidPasswordSizeException if the password does not have the minimum length.
     */
    @Override
    public void updatePassword(String newPasswordInPlainText) {
        if (newPasswordInPlainText == null)
            throw new NullArgumentException();
        if (INCORRECT_PASSWORD_SIZE.test(newPasswordInPlainText.trim()))
            throw new InvalidPasswordSizeException();

        this.hashedPassword = createHashedPassword(newPasswordInPlainText.trim(), this.salt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credential that = (Credential) o;
        return ID.equals(that.ID) && accessKeys.equals(that.accessKeys) && Arrays.equals(salt, that.salt) && hashedPassword.equals(that.hashedPassword);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ID, accessKeys, hashedPassword);
        result = 31 * result + Arrays.hashCode(salt);
        return result;
    }

    /**
     * Returns a graphical representation of the Credential,
     * in which it displays only its access keys.
     *
     * @return a graphical representation of the Credential.
     */
    @Override
    public String toString() {
        return "Credential{" +
                "accessKeys=" + accessKeys +
                '}';
    }

    protected Credential() {
    }

    @SuppressWarnings("unused")
    private void setID(UUID ID) {
        this.ID = ID;
    }

    @SuppressWarnings("unused")
    private void setAccessKeys(Set<String> accessKeys) {
        this.accessKeys = accessKeys;
    }

    @SuppressWarnings("unused")
    private byte[] getSalt() {
        return salt;
    }

    @SuppressWarnings("unused")
    private void setSalt(byte[] salt) {
        this.salt = salt;
    }

    @SuppressWarnings("unused")
    private String getHashedPassword() {
        return hashedPassword;
    }

    @SuppressWarnings("unused")
    private void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
