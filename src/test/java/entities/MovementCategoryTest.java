package entities;

import bll.entities.IMovementCategory;
import bll.entities.MovementCategory;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class MovementCategoryTest {
    URI uri1 = new URI("/rest.png");
    URI uri2 = new URI("./assets/tax.png");
    IMovementCategory t1 = new MovementCategory("Educação");
    IMovementCategory t2 = new MovementCategory("Restauração", null, true, true);
    IMovementCategory t3 = new MovementCategory(t1);
    IMovementCategory t4 = new MovementCategory(t3);
    IMovementCategory t5 = new MovementCategory("Impostos", uri2);

    public MovementCategoryTest() throws URISyntaxException {
    }

    @Test
    public void exceptionShouldBeRaisedForHavingANameLessThanTheMinimumSize() {
        assertThrows(InvalidNameSizeException.class, () -> new MovementCategory("Xz",uri1));
        assertThrows(InvalidNameSizeException.class, () -> new MovementCategory("",uri1));
        assertThrows(InvalidNameSizeException.class, () -> t1.updateName("fV"));
        assertThrows(InvalidNameSizeException.class, () -> t4.updateName(""));

    }

    @Test
    public void exceptionShouldBeRaisedForHavingANameLessThanTheMaximumSize() {
        assertThrows(InvalidNameSizeException.class, () -> new MovementCategory("ShouldBeRaisedForHavingANameLessThanTheMaximumSize",uri1));
        assertThrows(InvalidNameSizeException.class, () -> t1.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        assertThrows(InvalidNameSizeException.class, () -> t4.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
    }

    @Test
    public void shouldThrowExceptionByNullName() {
        assertThrows(NullArgumentException.class, () -> new MovementCategory(null,uri1));
        assertThrows(NullArgumentException.class, () -> t1.updateName(null));
        assertThrows(NullArgumentException.class, () -> t4.updateName(null));
    }


    @Test
    public void shouldThrowExceptionByNullTransactionCategory() {
        IMovementCategory nullCategory = null;
        assertThrows(NullArgumentException.class, () -> new MovementCategory(nullCategory));
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
    public void shouldHaveVisibilityCorrect(){
        assertTrue(t2.isPublic());
        assertFalse(t1.isPublic());
        assertFalse(t3.isPublic());
        assertFalse(t4.isPublic());
        assertTrue(t5.isPublic());
    }

    @Test
    public void theImgURIShouldBeCorrect() {
        assertNull( t1.getImage());
        assertNull(t2.getImage());
        assertEquals(uri2, t5.getImage());
        assertNull( t3.getImage());
        assertNull(t4.getImage());
    }

    @Test
    public void ShouldChangeImgURI() throws URISyntaxException {
        URI newURI = new URI("./img/tools.jpg");
        t1.updateImage(newURI);
        assertEquals(newURI, t1.getImage());
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
