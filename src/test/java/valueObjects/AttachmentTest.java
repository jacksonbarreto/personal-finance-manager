package valueObjects;

import bll.exceptions.EmptyArgumentException;
import bll.valueObjects.Attachment;
import bll.valueObjects.IAttachment;
import org.junit.jupiter.api.Test;
import bll.exceptions.NullArgumentException;

import static org.junit.jupiter.api.Assertions.*;

public class AttachmentTest {

    IAttachment obj1 = new Attachment("www.ipvc.pt");
    IAttachment obj2 = new Attachment("www.estg.ipvc.pt");
    IAttachment obj3 = new Attachment("www.ipvc.pt");
    IAttachment obj4 = new Attachment("www.ipvc.pt");

    @Test
    public void shouldThrowExceptionByNullURI() {
        assertThrows(NullArgumentException.class, () -> new Attachment(null));
    }

    @Test
    public void shouldThrowExceptionByEmptyURI() {
        assertThrows(EmptyArgumentException.class, () -> new Attachment(""));
    }

    @Test
    public void shouldHaveTheCorrectURI() {
        assertEquals("www.ipvc.pt", obj1.getURI());
        assertEquals("www.estg.ipvc.pt", obj2.getURI());
    }

    @Test
    public void shouldBeEquals() {
        assertEquals(obj1, obj3);
        assertEquals(obj3, obj4);
        assertEquals(obj1, obj4);
    }

    @Test
    public void shouldBeDifferent() {
        assertNotEquals(obj1, obj2);
    }
}
