package entities;

import bll.entities.IMovementCategory;
import bll.entities.MovementCategory;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovementCategoryTest {

    IMovementCategory t1 = new MovementCategory("Educação");
    IMovementCategory t2 = new MovementCategory("Restauração", "/rest.png", true);
    IMovementCategory t3 = new MovementCategory(t1);
    IMovementCategory t4 = new MovementCategory(t3);
    IMovementCategory t5 = new MovementCategory("Impostos", "./assets/tax.png");

    @Test
    public void exceptionShouldBeRaisedForHavingANameLessThanTheMinimumSize() {
        assertThrows(InvalidNameSizeException.class, () -> new MovementCategory("Xz"));
        assertThrows(InvalidNameSizeException.class, () -> new MovementCategory(""));
        assertThrows(InvalidNameSizeException.class, () -> t1.updateName("fV"));
        assertThrows(InvalidNameSizeException.class, () -> t4.updateName(""));

    }

    @Test
    public void exceptionShouldBeRaisedForHavingANameLessThanTheMaximumSize() {
        assertThrows(InvalidNameSizeException.class, () -> new MovementCategory("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        assertThrows(InvalidNameSizeException.class, () -> t1.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        assertThrows(InvalidNameSizeException.class, () -> t4.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
    }

    @Test
    public void shouldThrowExceptionByNullName() {
        String nullValue = null;
        assertThrows(NullArgumentException.class, () -> new MovementCategory(nullValue));
        assertThrows(NullArgumentException.class, () -> t1.updateName(nullValue));
        assertThrows(NullArgumentException.class, () -> t4.updateName(nullValue));
    }

    @Test
    public void shouldThrowExceptionByNullImgURI() {
        String nullValue = null;
        assertThrows(NullArgumentException.class, () -> new MovementCategory("Name", nullValue));
        assertThrows(NullArgumentException.class, () -> t1.updateName(nullValue));
        assertThrows(NullArgumentException.class, () -> t4.updateName(nullValue));
    }

    @Test
    public void shouldThrowExceptionByNullTransactionCategory() {
        IMovementCategory nullValue = null;
        assertThrows(NullArgumentException.class, () -> new MovementCategory(nullValue));
    }

    @Test
    public void shouldBeEquals() {
        t4.inactivate();
        t1.updateName("Transporte");
        assertEquals(t1, t3);
        assertEquals(t3, t4);
        assertEquals(t1, t4);
    }

    @Test
    public void shouldBeDeepEquals() {
        assertTrue(t1.isDeepEquals(t3));
        assertTrue(t3.isDeepEquals(t4));
        assertTrue(t1.isDeepEquals(t4));
        IMovementCategory ts = t1.clone();
        assertTrue(ts.isDeepEquals(t1));
    }

    @Test
    public void shouldBeDifferent() {
        t4.inactivate();
        t1.updateName("Transporte");
        assertFalse(t1.isDeepEquals(t4));
        assertFalse(t3.isDeepEquals(t4));
    }

    @Test
    public void theNameShouldBeCorrect() {
        assertEquals("Educação", t1.getName());
        assertEquals("Restauração", t2.getName());
        assertEquals("Educação", t3.getName());
        assertEquals("Educação", t4.getName());
        assertEquals("Impostos", t5.getName());
    }

    @Test
    public void theImgURIShouldBeCorrect() {
        assertEquals("", t1.getImgURI());
        assertEquals("/rest.png", t2.getImgURI());
        assertEquals("./assets/tax.png", t5.getImgURI());
        assertEquals("", t3.getImgURI());
        assertEquals("", t4.getImgURI());
    }

    @Test
    public void ShouldChangeImgURI(){
        t1.updateImgURI("./img/tools.jpg");
        assertEquals("./img/tools.jpg", t1.getImgURI());
    }

    @Test
    public void theActiveStateShouldBeCorrect() {
        assertTrue(t1.isActive());
        assertFalse(t1.isInactive());
        assertTrue(t2.isActive());
        assertFalse(t2.isInactive());
        assertTrue(t5.isActive());
    }

    @Test
    public void shouldChangeTheActiveState() {
        t1.inactivate();
        t2.inactivate();
        assertFalse(t1.isActive());
        assertFalse(t2.isActive());
        assertTrue(t1.isInactive());
        assertTrue(t2.isInactive());
        t2.activate();
        assertTrue(t2.isActive());
    }

    @Test
    public void shouldBeAClone() {
        IMovementCategory ts1 = t1.clone();
        IMovementCategory ts2 = ts1.clone();
        assertTrue(t1.isDeepEquals(ts1));
        assertTrue(ts1.isDeepEquals(ts2));
        assertTrue(t1.isDeepEquals(ts2));
    }
}
