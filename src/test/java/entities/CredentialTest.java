package entities;

import bll.entities.Credential;
import bll.entities.ICredential;
import bll.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialTest {

    ICredential obj1 = new Credential("localPart@domain.com", "secure password");

    @Test
    public void shouldThrowExceptionByCreationWithNullAccessKey() {
        assertThrows(NullArgumentException.class, () -> new Credential(null, "secure password"));
    }

    @Test
    public void shouldThrowExceptionByCreationWithNullPassword() {
        assertThrows(NullArgumentException.class, () -> new Credential("localPart@domain.com", null));
    }

    @Test
    public void shouldThrowExceptionByCreationWithShortPassword() {
        assertThrows(InvalidPasswordSizeException.class, () -> new Credential("localPart@domain.com", "1234567"));
    }

    @Test
    public void shouldNotThrowExceptionByCreationWithShortPassword() {
        assertDoesNotThrow(() -> new Credential("localPart@domain.com", "1234 567"));
    }

    @Test
    public void shouldThrowExceptionByCreationWithShortPasswordWithBlankSpaceAtTheBeginningOrEnd() {
        assertThrows(InvalidPasswordSizeException.class, () -> new Credential("localPart@domain.com", "1234567 "));
        assertThrows(InvalidPasswordSizeException.class, () -> new Credential("localPart@domain.com", " 1234567"));
        assertThrows(InvalidPasswordSizeException.class, () -> new Credential("localPart@domain.com", " 1234567 "));
    }

    @Test
    public void shouldThrowExceptionByCreationWithLongAccessKey() {
        assertThrows(InvalidAccessKeySizeException.class, () ->
                new Credential(
                        "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey",
                        "12345678"));
    }

    @Test
    public void shouldThrowExceptionByCreationWithLongAccessKeyWithBlankSpaceAtTheBeginningOrEnd() {
        assertDoesNotThrow(() ->
                new Credential(
                        "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowException ",
                        "12345678"));
        assertDoesNotThrow(() ->
                new Credential(
                        " shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowException",
                        "12345678"));
        assertDoesNotThrow(() ->
                new Credential(
                        " shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowExceptionByCreationWithLongAccessKey" +
                                "shouldThrowException ",
                        "12345678"));
    }

    @Test
    public void shouldHaveTheCreationAccessKey() {
        assertEquals(1, obj1.getAccessKeys().size());
        assertEquals("localPart@domain.com", obj1.getAccessKeys().get(0));
    }

    @Test
    public void shouldThrowExceptionByAddingNullAccessKey() {
        assertThrows(NullArgumentException.class, () -> obj1.addAccessKey(null));
    }

    @Test
    public void shouldThrowExceptionByAddingLongAccessKey() {
        assertThrows(InvalidAccessKeySizeException.class, () -> obj1.addAccessKey(
                "shouldThrowExceptionByCreationWithLongAccessKey" +
                        "shouldThrowExceptionByCreationWithLongAccessKey" +
                        "shouldThrowExceptionByCreationWithLongAccessKey" +
                        "shouldThrowExceptionByCreationWithLongAccessKey" +
                        "shouldThrowExceptionByCreationWithLongAccessKey" +
                        "shouldThrowExceptionByCreationWithLongAccessKey"));
    }

    @Test
    public void shouldThrowExceptionByAddingAccessKeyThatAlreadyExists() {
        assertThrows(AccessKeyAlreadyExistsException.class, () -> obj1.addAccessKey("localPart@domain.com"));
    }

    @Test
    public void shouldAddANewAccessKey() {
        obj1.addAccessKey("New Access Key");
        assertTrue(obj1.getAccessKeys().contains("New Access Key"));
        assertTrue(obj1.getAccessKeys().contains("localPart@domain.com"));
        assertEquals(2, obj1.getAccessKeys().size());
    }

    @Test
    public void shouldThrowExceptionByTryToRemoveANonexistentAccessKey() {
        assertThrows(AccessKeyDoesNotExistException.class, () -> obj1.removeAccessKey("nonexistent access key"));
    }

    @Test
    public void shouldThrowExceptionByTryToRemoveAAccessKeyWithANullAccessKey() {
        assertThrows(NullArgumentException.class, () -> obj1.removeAccessKey(null));
    }

    @Test
    public void shouldRemoveAccessKey() {
        obj1.addAccessKey("New Access Key");
        assertEquals(2, obj1.getAccessKeys().size());
        obj1.removeAccessKey("localPart@domain.com");
        assertEquals(1, obj1.getAccessKeys().size());
        assertTrue(obj1.getAccessKeys().contains("New Access Key"));
        assertFalse(obj1.getAccessKeys().contains("localPart@domain.com"));
    }

    @Test
    public void shouldThrowAnExceptionForTestingANullPassword() {
        assertThrows(NullArgumentException.class, () -> obj1.isPasswordValid(null));
    }

    @Test
    public void shouldVerifyThePasswordCorrectly() {
        assertTrue(obj1.isPasswordValid("secure password"));
        assertTrue(obj1.isPasswordValid(" secure password"));
        assertTrue(obj1.isPasswordValid("secure password "));
        assertTrue(obj1.isPasswordValid(" secure password "));
        assertFalse(obj1.isPasswordValid("secure  password"));
        assertFalse(obj1.isPasswordValid("super secure password"));
    }

    @Test
    public void shouldThrowExceptionForUpdatingThePasswordWithANullPassword() {
        assertThrows(NullArgumentException.class, () -> obj1.updatePassword(null));
    }

    @Test
    public void shouldThrowExceptionForUpdatingThePasswordWithAShortPassword() {
        assertThrows(InvalidPasswordSizeException.class, () -> obj1.updatePassword("1234567"));
        assertThrows(InvalidPasswordSizeException.class, () -> obj1.updatePassword(" 1234567"));
        assertThrows(InvalidPasswordSizeException.class, () -> obj1.updatePassword("1234567 "));
        assertThrows(InvalidPasswordSizeException.class, () -> obj1.updatePassword(" 1234567 "));
    }

    @Test
    public void shouldNotThrowExceptionForUpdatingThePasswordWithShortPassword() {
        assertDoesNotThrow(() -> obj1.updatePassword("1234 567"));
    }

    @Test
    public void shouldUpdateThePassword() {
        obj1.updatePassword("new Password");
        assertTrue(obj1.isPasswordValid("new Password"));
        assertFalse(obj1.isPasswordValid("secure password"));
    }
}
