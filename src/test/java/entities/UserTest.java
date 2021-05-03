package entities;

import bll.entities.*;
import bll.exceptions.*;
import bll.valueObjects.Email;
import bll.valueObjects.IEmail;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static bll.enumerators.ERole.PREMIUM;
import static bll.enumerators.ERole.SIMPLE;
import static bll.enumerators.EUserState.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    ICredential credential1 = new Credential("token", "password");
    IEmail email1 = new Email("localPart@domain.com");
    String name = "Name User´s";
    IUser obj1 = new User(name, credential1,
            Collections.singletonList(ACTIVE),
            Collections.singletonList(SIMPLE),
            email1);

    @Test
    public void shouldThrowExceptionByCreateWithNullName() {
        assertThrows(NullArgumentException.class, () -> new User(null,
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithShortName() {
        assertThrows(InvalidNameSizeException.class, () -> new User("Sm",
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));

        assertThrows(InvalidNameSizeException.class, () -> new User("Sm ",
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithLongName() {
        assertThrows(InvalidNameSizeException.class, () -> new User(
                "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName",
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithNullCredential() {
        assertThrows(NullArgumentException.class, () -> new User("Name",
                null,
                Collections.singletonList(ACTIVE),
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithNullUserStates() {
        assertThrows(NullArgumentException.class, () -> new User("Name",
                new Credential("token", "password"),
                null,
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithEmptyUserStates() {
        assertThrows(ForbiddenLeaveUserStatelessException.class, () -> new User("Name",
                new Credential("token", "password"),
                Collections.emptyList(),
                Collections.singletonList(SIMPLE),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithNullRoles() {
        assertThrows(NullArgumentException.class, () -> new User("Name",
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                null,
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithEmptyRoles() {
        assertThrows(ForbiddenLeaveUserWithoutFunctionsException.class, () -> new User("Name",
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                Collections.emptyList(),
                new Email("localPart@domain.com")));
    }

    @Test
    public void shouldThrowExceptionByCreateWithNullEmail() {
        assertThrows(NullArgumentException.class, () -> new User("Name",
                new Credential("token", "password"),
                Collections.singletonList(ACTIVE),
                Collections.singletonList(SIMPLE),
                null));
    }

    @Test
    public void shouldHaveTheEmailInAccessKeys() {
        assertTrue(obj1.getCredential().getAccessKeys().contains(email1.getEmail()));
    }

    @Test
    public void shouldUpdateEmailInAccessKeyWhenUpdateEmail() {
        IEmail email2 = new Email("turin@imb.com");
        obj1.updateEmail(email2);
        assertTrue(obj1.getCredential().getAccessKeys().contains(email2.getEmail()));
        assertFalse(obj1.getCredential().getAccessKeys().contains(email1.getEmail()));
    }

    @Test
    public void shouldHaveTheNameInAccessKey() {
        assertTrue(obj1.getCredential().getAccessKeys().contains(name));
    }

    @Test
    public void shouldUpdateNameInAccessKeyWhenUpdateName() {
        obj1.updateName("new name");
        assertTrue(obj1.getCredential().getAccessKeys().contains("new name"));
        assertFalse(obj1.getCredential().getAccessKeys().contains(name));
    }

    @Test
    public void shouldReturnTheUserName() {
        assertEquals(name, obj1.getName());
    }

    @Test
    public void shouldReturnTheRegistrationDate() {
        assertEquals(LocalDate.now(), obj1.getRegistrationDate());
    }

    @Test
    public void shouldReturnTheEmail() {
        assertEquals(email1, obj1.getEmail());
    }

    @Test
    public void shouldReturnTheCredential() {
        assertEquals(credential1, obj1.getCredential());
    }

    @Test
    public void shouldReturnTheWallets() {
        assertTrue(obj1.getWallets().isEmpty());
    }

    @Test
    public void shouldReturnTheRoles() {
        assertEquals(1, obj1.getRoles().size());
        assertEquals(SIMPLE, obj1.getRoles().get(0));
    }

    @Test
    public void shouldReturnTheUserStates() {
        assertEquals(1, obj1.getUserStates().size());
        assertEquals(ACTIVE, obj1.getUserStates().get(0));
    }

    @Test
    public void shouldThrowExceptionByAddNullUserState() {
        assertThrows(NullArgumentException.class, () -> obj1.addUserState(null));
    }

    @Test
    public void shouldAddUserState() {
        obj1.addUserState(WAITING_FOR_EMAIL_CONFIRMATION);
        assertEquals(2, obj1.getUserStates().size());
        assertTrue(obj1.getUserStates().contains(WAITING_FOR_EMAIL_CONFIRMATION));
        assertTrue(obj1.getUserStates().contains(ACTIVE));
    }

    @Test
    public void shouldThrowExceptionByRemoveNullUserState() {
        assertThrows(NullArgumentException.class, () -> obj1.removeUserState(null));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveNonExistentUserState() {
        assertThrows(NonExistentUserStateException.class, () -> obj1.removeUserState(INVALID_LOGIN_ATTEMPT));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveAllUserState() {
        assertThrows(ForbiddenLeaveUserStatelessException.class, () -> obj1.removeUserState(ACTIVE));
    }

    @Test
    public void shouldRemoveUserState() {
        obj1.addUserState(BLOCKED_BY_MANY_INVALID_LOGIN_ATTEMPT);
        obj1.removeUserState(ACTIVE);
        assertEquals(1, obj1.getUserStates().size());
        assertTrue(obj1.getUserStates().contains(BLOCKED_BY_MANY_INVALID_LOGIN_ATTEMPT));
    }

    @Test
    public void shouldAddWallet() {
        obj1.addWallet(new Wallet("Wallet", new FormOfPayment("MB Way"), new Payee("Wallet")));
        assertEquals(1, obj1.getWallets().size());
    }

    @Test
    public void shouldThrowExceptionByAddExistingWallet() {
        IWallet wallet = new Wallet("Wallet", new FormOfPayment("MB Way"), new Payee("Wallet"));
        obj1.addWallet(wallet);
        assertThrows(ExistingWalletException.class, () -> obj1.addWallet(wallet));
    }

    @Test
    public void shouldThrowExceptionByAddNullWallet() {
        assertThrows(NullArgumentException.class, () -> obj1.addWallet(null));
    }

    @Test
    public void shouldRemoveWallet() {
        IWallet wallet = new Wallet("Wallet", new FormOfPayment("MB Way"), new Payee("Wallet"));
        obj1.addWallet(wallet);
        assertTrue(obj1.getWallets().contains(wallet));
        obj1.removeWallet(wallet);
        assertTrue(obj1.getWallets().isEmpty());
    }


    @Test
    public void shouldThrowExceptionByTryRemoveNonExistentWallet() {
        IWallet wallet = new Wallet("Wallet", new FormOfPayment("MB Way"), new Payee("Wallet"));

        assertTrue(obj1.getWallets().isEmpty());
        assertThrows(NonExistentWalletException.class, () -> obj1.removeWallet(wallet));
    }

    @Test
    public void shouldThrowExceptionByRemoveNullWallet() {
        assertThrows(NullArgumentException.class, () -> obj1.removeWallet(null));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateWithNullWallet() {
        assertThrows(NullArgumentException.class, () -> obj1.updateWallet(null));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateWithNonExistingWallet() {
        assertThrows(NullArgumentException.class, () -> obj1.updateWallet(null));
    }

    @Test
    public void shouldUpdateWallet() {
        IWallet wallet = new Wallet("Wallet", new FormOfPayment("MB Way"), new Payee("Wallet"));
        obj1.addWallet(wallet);
        assertEquals(1, obj1.getWallets().size());
        assertTrue(obj1.getWallets().contains(wallet));
        assertTrue(wallet.isDeepEquals(new ArrayList<>(obj1.getWallets()).get(0)));

        wallet.updateName("New Wallet Name");
        assertFalse(wallet.isDeepEquals(new ArrayList<>(obj1.getWallets()).get(0)));

        obj1.updateWallet(wallet);
        assertEquals(1, obj1.getWallets().size());
        assertTrue(wallet.isDeepEquals(new ArrayList<>(obj1.getWallets()).get(0)));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveNullRole() {
        assertThrows(NullArgumentException.class, () -> obj1.removeRole(null));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveNonExistentRole() {
        assertThrows(NonExistentRoleException.class, () -> obj1.removeRole(PREMIUM));
    }

    @Test
    public void shouldRemoveRole() {
        obj1.addRole(PREMIUM);
        obj1.removeRole(SIMPLE);
        assertEquals(1, obj1.getRoles().size());
        assertTrue(obj1.getRoles().contains(PREMIUM));
    }

    @Test
    public void shouldThrowExceptionByTryAddNullRole() {
        assertThrows(NullArgumentException.class, () -> obj1.addRole(null));
    }

    @Test
    public void shouldAddRole() {
        obj1.addRole(PREMIUM);
        assertEquals(2, obj1.getRoles().size());
        assertTrue(obj1.getRoles().contains(PREMIUM));
        assertTrue(obj1.getRoles().contains(SIMPLE));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateWithNullName() {
        assertThrows(NullArgumentException.class, () -> obj1.updateName(null));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateWithShortName() {
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("Sm"));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName(" Sm"));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("Sm "));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName(" Sm "));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateWithLongName() {
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName(
                "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName" +
                        "shouldThrowExceptionByCreateWithLongName"));
    }

    @Test
    public void shouldUpdateName() {
        obj1.updateName("New name of user");

        assertEquals("New name of user", obj1.getName());
    }

    @Test
    public void shouldThrowExceptionByTryUpdateEmailWithNullEmail() {
        assertThrows(NullArgumentException.class, () -> obj1.updateEmail(null));
    }

    @Test
    public void shouldUpdateEmail() {
        IEmail email2 = new Email("newMail@domain.com");
        obj1.updateEmail(email2);
        assertEquals(email2, obj1.getEmail());
    }

    @Test
    public void shouldThrowExceptionByTryAddNullPayee() {
        assertThrows(NullArgumentException.class, () -> obj1.addPayee(null));
    }

    @Test
    public void shouldThrowExceptionByTryAddExistingPayee() {
        IPayee p1 = new Payee("Continent");
        obj1.addPayee(p1);
        assertThrows(ExistingPayeeException.class, () -> obj1.addPayee(p1));
    }

    @Test
    public void shouldReturnThePayees() {
        assertTrue(obj1.getPayee().isEmpty());
    }

    @Test
    public void shouldAddPayee() {
        IPayee p1 = new Payee("Continent");
        obj1.addPayee(p1);
        assertTrue(obj1.getPayee().contains(p1));
        assertEquals(1, obj1.getPayee().size());

        IPayee p2 = new Payee("Millennium", true);
        obj1.addPayee(p2);
        assertTrue(obj1.getPayee().contains(p2));
        assertEquals(2, obj1.getPayee().size());
    }

    @Test
    public void shouldThrowExceptionByTryRemoveNullPayee() {
        assertThrows(NullArgumentException.class, () -> obj1.removePayee(null));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveNonExistingPayee() {
        IPayee p1 = new Payee("Continent");
        assertThrows(NonExistingPayeeException.class, () -> obj1.removePayee(p1));
    }

    @Test
    public void shouldRemovePayee() {
        IPayee p1 = new Payee("Continent");
        assertTrue(obj1.getPayee().isEmpty());
        obj1.addPayee(p1);
        assertFalse(obj1.getPayee().isEmpty());
        assertTrue(obj1.getPayee().contains(p1));
        obj1.removePayee(p1);
        assertTrue(obj1.getPayee().isEmpty());
    }

    @Test
    public void shouldThrowExceptionByTryUpdatePayeeWithNullPayee() {
        assertThrows(NullArgumentException.class, () -> obj1.updatePayee(null));
    }

    @Test
    public void shouldThrowExceptionByTryUpdatePayeeWithNonExistingPayee() {
        IPayee p1 = new Payee("Continent");
        assertThrows(NonExistingPayeeException.class, () -> obj1.updatePayee(p1));
    }

    @Test
    public void shouldUpdatePayee() {
        IPayee p1 = new Payee("Continent");
        obj1.addPayee(p1);
        assertEquals(1, obj1.getPayee().size());
        assertTrue(obj1.getPayee().contains(p1));
        assertTrue(p1.isDeepEquals(new ArrayList<>(obj1.getPayee()).get(0)));

        p1.updateName("Revolution");
        assertFalse(p1.isDeepEquals(new ArrayList<>(obj1.getPayee()).get(0)));
        assertEquals(new ArrayList<>(obj1.getPayee()).get(0), p1);

        obj1.updatePayee(p1);
        assertTrue(p1.isDeepEquals(new ArrayList<>(obj1.getPayee()).get(0)));
        assertEquals(1, obj1.getPayee().size());
    }

    @Test
    public void shouldThrowExceptionByTryAddNullCategory() {
        assertThrows(NullArgumentException.class, () -> obj1.addCategory(null));
    }

    @Test
    public void shouldAddCategory() throws URISyntaxException {
        URI uri1 = new URI("/rest.png");
        IMovementCategory t1 = new MovementCategory("Education", uri1);
        assertTrue(obj1.getCategory().isEmpty());
        obj1.addCategory(t1);
        assertEquals(1, obj1.getCategory().size());
        assertTrue(obj1.getCategory().contains(t1));
        assertTrue(t1.isDeepEquals(new ArrayList<>(obj1.getCategory()).get(0)));
    }

    @Test
    public void shouldThrowExceptionByTryAddExistingCategory() throws URISyntaxException {
        URI uri1 = new URI("/rest.png");
        IMovementCategory t1 = new MovementCategory("Education", uri1);
        obj1.addCategory(t1);
        assertThrows(ExistingCategoryException.class, () -> obj1.addCategory(t1));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateCategoryWithNullCategory() {
        assertThrows(NullArgumentException.class, () -> obj1.updateCategory(null));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateANonExistingCategory() throws URISyntaxException {
        URI uri1 = new URI("/rest.png");
        IMovementCategory t1 = new MovementCategory("Education", uri1);
        assertThrows(NonExistingCategoryException.class, () -> obj1.updateCategory(t1));
    }

    @Test
    public void shouldUpdateCategory() throws URISyntaxException {
        URI uri1 = new URI("/rest.png");
        IMovementCategory t1 = new MovementCategory("Education", uri1);
        obj1.addCategory(t1);

        t1.updateName("Food");
        assertFalse(t1.isDeepEquals(new ArrayList<>(obj1.getCategory()).get(0)));

        obj1.updateCategory(t1);
        assertTrue(t1.isDeepEquals(new ArrayList<>(obj1.getCategory()).get(0)));
    }

    @Test
    public void shouldRemoveCategory() throws URISyntaxException {
        URI uri1 = new URI("/rest.png");
        IMovementCategory t1 = new MovementCategory("Education", uri1);
        IMovementCategory t2 = new MovementCategory("Food", null);
        obj1.addCategory(t1);
        obj1.addCategory(t2);
        assertEquals(2, obj1.getCategory().size());
        assertTrue(obj1.getCategory().contains(t1));
        assertTrue(obj1.getCategory().contains(t2));
        obj1.removeCategory(t1);
        assertEquals(1, obj1.getCategory().size());
        assertFalse(obj1.getCategory().contains(t1));
        assertTrue(obj1.getCategory().contains(t2));
        obj1.removeCategory(t2);
        assertTrue(obj1.getCategory().isEmpty());
    }

    @Test
    public void shouldThrowExceptionByTryRemoveCategoryWithNullCategory() {
        assertThrows(NullArgumentException.class, () -> obj1.removeCategory(null));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveANonExistingCategory() {
        IMovementCategory t1 = new MovementCategory("Education", null);
        assertThrows(NonExistingCategoryException.class, () -> obj1.removeCategory(t1));
    }

    @Test
    public void shouldToBeAClone() {
        IUser clone = obj1.clone();

        assertTrue(clone.isDeepEquals(obj1));
        assertEquals(clone, obj1);
    }

    @Test
    public void shouldToBeEquals() {
        IUser equalUser = obj1.clone();
        equalUser.updateName("New name");

        assertEquals(equalUser, obj1);
        assertEquals(obj1, equalUser);
        assertFalse(equalUser.isDeepEquals(obj1));
        assertFalse(obj1.isDeepEquals(equalUser));

        assertEquals(equalUser.hashCode(), obj1.hashCode());
    }

    @Test
    public void shouldToBeDeepEquals() {
        IUser obj2 = obj1.clone();
        IUser obj3 = obj2.clone();

        assertTrue(obj2.isDeepEquals(obj1));
        assertTrue(obj3.isDeepEquals(obj2));
        assertTrue(obj3.isDeepEquals(obj1));

        assertTrue(obj1.isDeepEquals(obj2));
        assertTrue(obj2.isDeepEquals(obj3));
        assertTrue(obj1.isDeepEquals(obj3));
    }
}
