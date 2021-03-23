package entities;

import bll.entities.IPayee;
import bll.entities.Payee;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PayeeTest {

    IPayee p1 = new Payee("Continente");
    IPayee p2 = new Payee("Pingo Doce", true);
    IPayee p3 = new Payee(p1);
    IPayee p4 = new Payee(p3);


    @Test
    public void exceptionShouldBeRaisedForHavingANameLessThanTheMinimumSize() {
        assertThrows(InvalidNameSizeException.class, () -> new Payee("Xz"));
        assertThrows(InvalidNameSizeException.class, () -> new Payee(""));
        assertThrows(InvalidNameSizeException.class, () -> p1.updateName("fV"));
        assertThrows(InvalidNameSizeException.class, () -> p4.updateName(""));

    }

    @Test
    public void exceptionShouldBeRaisedForHavingANameLessThanTheMaximumSize() {
        assertThrows(InvalidNameSizeException.class, () -> new Payee("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        assertThrows(InvalidNameSizeException.class, () -> p1.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        assertThrows(InvalidNameSizeException.class, () -> p4.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
    }

    @Test
    public void shouldThrowExceptionByNullName() {
        String nullValue = null;
        assertThrows(NullArgumentException.class, () -> new Payee(nullValue));
        assertThrows(NullArgumentException.class, () -> p1.updateName(nullValue));
        assertThrows(NullArgumentException.class, () -> p4.updateName(nullValue));
    }

    @Test
    public void shouldThrowExceptionByNullPayee() {
        IPayee nullValue = null;
        assertThrows(NullArgumentException.class, () -> new Payee(nullValue));
    }

    @Test
    public void shouldBeEquals() {
        p4.inactivate();
        p1.updateName("Millenium");
        assertEquals(p1, p3);
        assertEquals(p3, p4);
        assertEquals(p1, p4);
    }

    @Test
    public void shouldBeDeepEquals() {
        assertTrue(p1.isDeepEquals(p3));
        assertTrue(p3.isDeepEquals(p4));
        assertTrue(p1.isDeepEquals(p4));
        Payee payee1 = (Payee) p1.clone();
        assertTrue(payee1.isDeepEquals(p1));
    }

    @Test
    public void shouldBeDifferent() {
        p4.inactivate();
        p1.updateName("Millenium");
        assertFalse(p1.isDeepEquals(p4));
        assertFalse(p3.isDeepEquals(p4));
    }

    @Test
    public void theNameShouldBeCorrect() {
        assertEquals("Continente", p1.getName());
        assertEquals("Pingo Doce", p2.getName());
        assertEquals("Continente", p3.getName());
        assertEquals("Continente", p4.getName());
    }

    @Test
    public void theActiveStateShouldBeCorrect() {
        assertTrue(p1.isActive());
        assertFalse(p1.isInactive());
        assertTrue(p2.isActive());
        assertFalse(p2.isInactive());
    }

    @Test
    public void shouldChangeTheActiveState() {
        p1.inactivate();
        p2.inactivate();
        assertFalse(p1.isActive());
        assertFalse(p2.isActive());
        assertTrue(p1.isInactive());
        assertTrue(p2.isInactive());
        p2.activate();
        assertTrue(p2.isActive());
    }

    @Test
    public void shouldBeAClone() {
        IPayee payee1 = p1.clone();
        IPayee payee2 = payee1.clone();
        assertTrue(p1.isDeepEquals(payee1));
        assertTrue(payee1.isDeepEquals(payee2));
        assertTrue(p1.isDeepEquals(payee2));
    }
}
