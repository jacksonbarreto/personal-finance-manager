package bll.entities;

import bll.exceptions.NullArgumentException;
import bll.exceptions.AccessKeysCannotBeEmptyException;
import bll.exceptions.AccessKeyDoesNotExistException;
import bll.exceptions.InvalidAccessKeySizeException;
import bll.exceptions.InvalidPasswordSizeException;
import bll.exceptions.AccessKeyAlreadyExistsException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface ICredential extends Serializable {

    int MAXIMUM_ACCESS_KEY_SIZE = 255;
    int MINIMUM_PASSWORD_SIZE = 8;
    int SALT_SIZE = 16;
    int KEY_LENGTH = 128;
    int ITERATION_COUNT = 65536;
    String FORMAT_HEXADECIMAL_FOR_HASH = "%02X";
    String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";
    Predicate<String> INCORRECT_ACCESS_KEY_SIZE = (s) -> s.length() > MAXIMUM_ACCESS_KEY_SIZE;
    Predicate<String> INCORRECT_PASSWORD_SIZE = (s) -> s.length() < MINIMUM_PASSWORD_SIZE;

    /**
     * Returns the unique identifier of the Credential.
     *
     * @return the unique identifier of the Credential.
     */
    UUID getID();

    /**
     * Returns a collection of access keys.
     *
     * @return a collection of access keys.
     */
    List<String> getAccessKeys();

    /**
     * adds a new access key to the credential.
     *
     * @param accessKey new key to access the credential.
     * @throws NullArgumentException           if the argument is null.
     * @throws InvalidAccessKeySizeException   if the access key does not respect the size policy.
     * @throws AccessKeyAlreadyExistsException if the access key already exists in the credential.
     */
    void addAccessKey(String accessKey);

    /**
     * Removes an access key from the credential.
     *
     * @param accessKey to be removed from the credential.
     * @throws NullArgumentException            if the argument is null.
     * @throws AccessKeysCannotBeEmptyException if you try to remove the last passkey.
     * @throws AccessKeyDoesNotExistException   if you try to remove a key that does not exist in the credential.
     */
    void removeAccessKey(String accessKey);


    /**
     * Checks whether the password provided is the same as the credential.
     *
     * @param passwordInPlainText password be verified.
     * @return {@code true} if the password provided is the same as the credential.
     * @throws NullArgumentException if the argument is null.
     */
    boolean isPasswordValid(String passwordInPlainText);

    /**
     * Updates the credential password.
     *
     * @param newPasswordInPlainText new password for the credential.
     * @throws NullArgumentException        if the argument is null.
     * @throws InvalidPasswordSizeException if the password does not have the minimum length.
     */
    void updatePassword(String newPasswordInPlainText);

    /**
     * Returns a graphical representation of the Credential,
     * in which it displays only its access keys.
     *
     * @return a graphical representation of the Credential.
     */
    String toString();

    /**
     * Returns an array with a random salt.
     *
     * @return an array with a random salt.
     */
    static byte[] getSaltRandom() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Returns a hash of the given password, in a 32-character string.
     *
     * @param passwordInPlainText to be hashed.
     * @param salt                a random salt.
     * @return a hash of the given password, in a 32-character string.
     */
    static String createHashedPassword(String passwordInPlainText, byte[] salt) {
        StringBuilder hexHashedPassword = new StringBuilder();
        KeySpec spec = new PBEKeySpec(passwordInPlainText.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory;

        try {
            factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] byteHashPassword = factory.generateSecret(spec).getEncoded();
            for (byte b : byteHashPassword) {
                hexHashedPassword.append(String.format(FORMAT_HEXADECIMAL_FOR_HASH, 0xFF & b));
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return hexHashedPassword.toString();
    }


}
