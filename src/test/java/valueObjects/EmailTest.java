package valueObjects;

import bll.exceptions.InvalidEmailFormatException;
import bll.exceptions.NullArgumentException;
import bll.valueObjects.Email;
import bll.valueObjects.IEmail;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {
    IEmail email1 = new Email("jacksonjunior@ipvc.pt");
    IEmail email2 = new Email("jacksonjunior@ipvc.pt");
    IEmail email3 = new Email("jacksonjunior@ipvc.pt", true);
    IEmail email4 = new Email("jacksonjunior@ipvc.pt", false);
    IEmail email5 = new Email("leonadolopez@gmail.com");

    @Test
    public void ShouldThrowExceptionWithInvalidEmail() {
        assertThrows(InvalidEmailFormatException.class, () -> new Email("jackson@gmail"));
    }

    @Test
    public void ShouldThrowExceptionWithEmptyEmail() {
        assertThrows(InvalidEmailFormatException.class, () -> new Email(""));
    }

    @Test
    public void ShouldThrowExceptionWithNullEmail() {
        assertThrows(NullArgumentException.class, () -> new Email(null));
    }

    @Test
    public void shouldBeTheSame() {
        assertEquals(email1, email2);
        assertEquals(email2, email3);
        assertEquals(email1, email3);
    }

    @Test
    public void shouldBeDifferent() {
        assertNotEquals(email1, email4);
        assertNotEquals(email2, email4);
    }

    @Test
    public void localPartShouldBeCorrect() {
        assertEquals("jacksonjunior", email1.getLocalPart());
        assertEquals("leonadolopez", email5.getLocalPart());
    }

    @Test
    public void domainNameShouldBeCorrect() {
        assertEquals("ipvc.pt", email1.getDomainName());
        assertEquals("gmail.com", email5.getDomainName());
    }

    @Test
    public void emailShouldBeCorrect() {
        assertEquals("jacksonjunior@ipvc.pt", email1.getEmail());
        assertEquals("jacksonjunior@ipvc.pt", email2.getEmail());
        assertEquals("jacksonjunior@ipvc.pt", email4.getEmail());
        assertEquals("leonadolopez@gmail.com", email5.getEmail());
    }

    @Test
    public void AdvertisingContactShouldBeCorrect() {
        assertTrue(email1.isAuthorizedAdvertisingContact());
        assertTrue(email3.isAuthorizedAdvertisingContact());
        assertFalse(email4.isAuthorizedAdvertisingContact());
    }
}
