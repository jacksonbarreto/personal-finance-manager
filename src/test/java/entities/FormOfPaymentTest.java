package entities;

import bll.entities.FormOfPayment;
import bll.entities.IFormOfPayment;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FormOfPaymentTest {
    IFormOfPayment obj1 = new FormOfPayment("MB Way");
    IFormOfPayment obj2 = new FormOfPayment("Cartão de Crédito", false);
    IFormOfPayment obj3 = new FormOfPayment(obj1);
    IFormOfPayment obj4 = new FormOfPayment(obj3);

    @Test
    public void shouldLaunchExceptForHavingShortName() {
        assertThrows(InvalidNameSizeException.class, () -> new FormOfPayment("Xz"));
        assertThrows(InvalidNameSizeException.class, () -> new FormOfPayment(""));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("fV"));
        assertThrows(InvalidNameSizeException.class, () -> obj2.updateName(""));
    }

    @Test
    public void shouldLaunchExceptForHavingLongName() {
        assertThrows(InvalidNameSizeException.class, () -> new FormOfPayment("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
    }

    @Test
    public void shouldThrowExceptionByNullName() {
        String nullValue = null;
        assertThrows(NullArgumentException.class, () -> new FormOfPayment(nullValue));
        assertThrows(NullArgumentException.class, () -> obj1.updateName(nullValue));
        assertThrows(NullArgumentException.class, () -> obj4.updateName(nullValue));
    }

    @Test
    public void shouldLaunchExceptionForCreationWithNullObject() {
        IFormOfPayment nullValue = null;
        assertThrows(NullArgumentException.class, () -> new FormOfPayment(nullValue));
    }

    @Test
    public void shouldBeEquals() {
        obj4.inactivate();
        obj1.updateName("Cheque");
        assertEquals(obj1, obj3);
        assertEquals(obj3, obj4);
        assertEquals(obj1, obj4);
    }

    @Test
    public void shouldBeDeepEquals() {
        assertTrue(obj1.isDeepEquals(obj3));
        assertTrue(obj3.isDeepEquals(obj4));
        assertTrue(obj1.isDeepEquals(obj4));
        IFormOfPayment ts = obj1.clone();
        assertTrue(ts.isDeepEquals(obj1));
    }

    @Test
    public void shouldBeDifferent() {
        obj4.inactivate();
        obj1.updateName("Transporte");
        assertFalse(obj1.isDeepEquals(obj4));
        assertFalse(obj3.isDeepEquals(obj4));
    }

    @Test
    public void theNameShouldBeCorrect() {
        assertEquals("MB Way", obj1.getName());
        assertEquals("Cartão de Crédito", obj2.getName());
        assertEquals("MB Way", obj3.getName());
        assertEquals("MB Way", obj4.getName());
    }

    @Test
    public void ShouldChangeName() {
        obj1.updateName("Transferência Internacional");
        assertEquals("Transferência Internacional", obj1.getName());
    }

    @Test
    public void theActiveStateShouldBeCorrect() {
        assertTrue(obj1.isActive());
        assertFalse(obj1.isInactive());
        assertFalse(obj2.isActive());
        assertTrue(obj2.isInactive());
    }

    @Test
    public void shouldChangeTheActiveState() {
        obj1.inactivate();
        obj2.activate();
        assertFalse(obj1.isActive());
        assertTrue(obj2.isActive());
        assertTrue(obj1.isInactive());
        assertFalse(obj2.isInactive());
        obj2.inactivate();
        assertFalse(obj2.isActive());
    }

    @Test
    public void shouldBeAClone() {
        IFormOfPayment ts1 = obj1.clone();
        IFormOfPayment ts2 = ts1.clone();
        assertTrue(obj1.isDeepEquals(ts1));
        assertTrue(ts1.isDeepEquals(ts2));
        assertTrue(obj1.isDeepEquals(ts2));
    }
}
