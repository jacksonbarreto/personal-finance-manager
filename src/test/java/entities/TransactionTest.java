package entities;

import bll.entities.*;
import bll.exceptions.*;
import bll.valueObjects.Attachment;
import bll.valueObjects.IAttachment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    IPayee payee = new Payee("Continent");
    IFormOfPayment formOfPayment = new FormOfPayment("MB Way");
    ITransactionCategory category1 = new TransactionCategory("Mercado");
    ITransactionCategory category2 = new TransactionCategory("Education");
    IAttachment attachment1 = new Attachment("www.campus.pt");
    IAttachment attachment2 = new Attachment("www.college.pt");


    ITransaction obj1 = new Transaction(
            "Christmas shopping",
            "Christmas at Paula's house",
            new BigDecimal("33.50"),
            LocalDate.now(),
            formOfPayment,
            payee,
            category1);
    ITransaction obj2 = new Transaction(
            "English course",
            new BigDecimal("-25.90"),
            LocalDate.now(),
            formOfPayment,
            payee,
            category2);
    ITransaction obj3 = new Transaction(obj1);
    ITransaction obj4 = new Transaction(obj3);

    @Test
    public void shouldThrowExceptionByNullTransactionInConstructor() {
        assertThrows(NullArgumentException.class, () -> new Transaction(null));
    }

    @Test
    public void shouldThrowExceptionBySmallName() {
        assertThrows(InvalidNameSizeException.class, () -> new Transaction(
                "Co",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
        assertThrows(InvalidNameSizeException.class, () -> new Transaction(
                "    Co   ",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByBiggerName() {
        assertThrows(InvalidNameSizeException.class, () -> new Transaction(
                "ShouldBeRaisedForHavingANameLessThanTheMaximumSize",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullName() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                null,
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionBySmallDescription() {
        assertThrows(InvalidDescriptionSizeException.class, () -> new Transaction(
                "Christmas at Paula's house",
                "Na",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
        assertThrows(InvalidDescriptionSizeException.class, () -> new Transaction(
                "Christmas at Paula's house",
                "Na ",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldCreateTransactionWithEmptyDescription() {
        assertDoesNotThrow(() -> new Transaction(
                "Christmas at Paula's house",
                "",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByBiggerDescription() {
        assertThrows(InvalidDescriptionSizeException.class, () -> new Transaction(
                "Christmas shopping",
                "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullDescription() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas at Paula's house",
                null,
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullAmount() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                null,
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionForAnAmountEqualToZero() {
        assertThrows(AmountEqualZeroException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                BigDecimal.ZERO,
                LocalDate.now(),
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullDate() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                null,
                formOfPayment,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullFormOfPayment() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                null,
                payee,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullPayee() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                null,
                category1));
    }

    @Test
    public void shouldThrowExceptionByNullCategory() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                null));
    }

    @Test
    public void shouldThrowExceptionByNullAttachment() {
        assertThrows(NullArgumentException.class, () -> new Transaction(
                "Christmas shopping",
                "Christmas at Paula's house",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1,
                null));

        assertThrows(NullArgumentException.class, () -> obj1.addAttachment(null));
        assertThrows(NullArgumentException.class, () -> obj1.removeAttachment(null));
    }

    @Test
    public void shouldThrowExceptionForTryingToRemoveNonexistentAttachment() {
        Set<IAttachment> attachments = new HashSet<>();
        attachments.add(attachment1);
        ITransaction objTemp = new Transaction(
                "English course",
                new BigDecimal("25.90"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category2,
                attachments);
        assertThrows(AttachmentDoesNotExistException.class, () -> obj1.removeAttachment(attachment2));
        assertThrows(AttachmentDoesNotExistException.class, () -> objTemp.removeAttachment(attachment2));
    }

    @Test
    public void shouldHaveTheCorrectName() {
        assertEquals("Christmas shopping", obj1.getName());
    }

    @Test
    public void shouldHaveTheCorrectDescription() {
        assertEquals("Christmas at Paula's house", obj1.getDescription());
        assertEquals("", obj2.getDescription());
    }

    @Test
    public void shouldHaveTheCorrectAmount() {
        assertEquals(33.50, obj1.getAmount().doubleValue());
    }

    @Test
    public void shouldHaveTheCorrectDueDate() {
        assertEquals(LocalDate.now(), obj1.getDueDate());
    }

    @Test
    public void shouldHaveTheCorrectFormOfPayment() {
        assertEquals(formOfPayment, obj1.getFormOfPayment());
    }

    @Test
    public void shouldHaveTheCorrectPayee() {
        assertEquals(payee, obj1.getPayee());
    }

    @Test
    public void shouldHaveTheCorrectCategory() {
        assertEquals(category1, obj1.getCategory());
    }

    @Test
    public void shouldAddAAttachment() {
        obj1.addAttachment(attachment1);
        obj1.addAttachment(attachment2);
        assertEquals(2, obj1.getAttachments().size());
        assertTrue(obj1.getAttachments().contains(attachment1));
        assertTrue(obj1.getAttachments().contains(attachment2));
    }

    @Test
    public void shouldRemoveAAttachment() {
        obj1.addAttachment(attachment1);
        obj1.addAttachment(attachment2);
        obj1.removeAttachment(attachment1);
        assertEquals(1, obj1.getAttachments().size());
        assertTrue(obj1.getAttachments().contains(attachment2));
        obj1.removeAttachment(attachment2);
        assertTrue(obj1.getAttachments().isEmpty());
    }

    @Test
    public void shouldHaveTheCorrectAttachment() {
        Set<IAttachment> attachments = new HashSet<>();
        attachments.add(attachment1);
        ITransaction objTemp = new Transaction(
                "English course",
                new BigDecimal("25.90"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category2,
                attachments);

        assertEquals(attachments, objTemp.getAttachments());
    }

    @Test
    public void shouldBeEquals() {
        obj1.addAttachment(attachment2);
        obj4.addAttachment(attachment1);
        assertEquals(obj1, obj3);
        assertEquals(obj3, obj4);
        assertEquals(obj1, obj4);
    }

    @Test
    public void shouldBeDeepEquals() {
        assertTrue(obj1.isDeepEquals(obj3));
    }

    @Test
    public void shouldBeDifferent() {
        assertNotEquals(obj1, obj2);
        obj3.addAttachment(attachment1);
        assertFalse(obj1.isDeepEquals(obj3));
    }

    @Test
    public void shouldBeAClone() {
        ITransaction clone = obj1.clone();
        assertTrue(obj1.isDeepEquals(clone));
    }

    @Test
    public void shouldBeADebit() {
        assertTrue(obj2.isDebit());
    }

    @Test
    public void shouldBeACredit() {
        assertTrue(obj1.isCredit());
    }

}
