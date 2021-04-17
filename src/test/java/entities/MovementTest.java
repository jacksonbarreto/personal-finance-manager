package entities;

import bll.entities.*;
import bll.exceptions.AmountEqualZeroException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static bll.builders.IMovementBuilder.makeMovement;
import static bll.entities.IMovement.COMPARE_FOR_AMOUNT;
import static bll.enumerators.EOperationType.CREDIT;
import static bll.enumerators.EOperationType.DEBIT;
import static bll.enumerators.ERepetitionFrequency.*;
import static org.junit.jupiter.api.Assertions.*;

public class MovementTest {
    IPayee payee = new Payee("Continent");
    URI uri1 = new URI("/rest.png");
    IFormOfPayment formOfPayment = new FormOfPayment("MB Way");
    IMovementCategory category1 = new MovementCategory("Mercado", uri1);
    IMovementCategory category2 = new MovementCategory("Education", uri1);

    IMovement obj1 = makeMovement("Christmas shopping", "33.50",
            LocalDate.now(), formOfPayment, payee, category1, CREDIT).build();

    IMovement obj2 = makeMovement("English course", "22.30",
            LocalDate.of(1970, Month.JANUARY, 1), formOfPayment, payee, category2, DEBIT).build();

    public MovementTest() throws URISyntaxException {
    }


    @Test
    public void shouldThrowExceptionByNullOperationType() {
        assertThrows(NullArgumentException.class, () -> new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, null));
    }

    @Test
    public void shouldThrowExceptionByNullFrequency() {
        assertThrows(NullArgumentException.class, () -> new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, null, UUID.randomUUID()));
    }

    @Test
    public void shouldHaveTheCorrectGroupID() {
        UUID uuid = UUID.randomUUID();
        UUID uuid1 = UUID.randomUUID();
        IMovement movement = new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, NONE, uuid);
        assertNotEquals(movement.getGroupID(), movement.getID());
        assertEquals(movement.getGroupID(), uuid);

        IMovement movement1 = new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, WEEKLY, uuid1);
        assertNotEquals(movement1.getGroupID(), uuid1);
        assertEquals(movement1.getGroupID(), movement1.getID());

        assertEquals(obj1.getGroupID(), obj1.getID());
    }

    @Test
    public void shouldHaveTheCorrectFrequency() {
        IMovement movement1 = new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, WEEKLY, UUID.randomUUID());

        assertEquals(movement1.getRepetitionFrequency(), WEEKLY);
    }

    @Test
    public void shouldToBeActive() {
        assertTrue(obj1.isActive());
        assertFalse(obj1.isInactive());
    }

    @Test
    public void shouldInactivateAMovement() {
        obj1.inactivate();
        assertTrue(obj1.isInactive());
        assertFalse(obj1.isActive());
    }

    @Test
    public void shouldHaveRegistrationDate() {
        assertEquals(obj1.getRegistrationDate(), LocalDate.now());
        assertEquals(obj2.getRegistrationDate(), LocalDate.now());
    }

    @Test
    public void shouldHaveNullAccomplishDate() {
        assertNull(obj1.getAccomplishDate());
        assertNull(obj2.getAccomplishDate());
    }

    @Test
    public void shouldHaveAccomplishDate() {
        obj1.accomplish();
        assertEquals(LocalDate.now(), obj1.getAccomplishDate());
        obj2.accomplish(LocalDate.of(1970, Month.FEBRUARY, 2));
        assertEquals(LocalDate.of(1970, Month.FEBRUARY, 2), obj2.getAccomplishDate());
    }

    @Test
    public void shouldThrowExceptionByTryAccomplishWithNullDate() {
        assertThrows(NullArgumentException.class, () -> obj1.accomplish(null));
    }

    @Test
    public void shouldHaveTheCorrectOperationType() {
        assertTrue(obj1.isCredit());
        assertFalse(obj1.isDebit());
        assertTrue(obj2.isDebit());
        assertFalse(obj2.isCredit());
    }

    @Test
    public void shouldHaveANegativeValueForADebitMovement() {
        assertEquals(new BigDecimal("-22.30"), obj2.getAmount());
    }

    @Test
    public void shouldHaveAPositiveValueForACreditMovement() {
        assertEquals(new BigDecimal("33.50"), obj1.getAmount());
    }

    @Test
    public void shouldUpdateAmountCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateAmount(null));
        obj2.updateAmount(new BigDecimal("92.40"));
        assertEquals(new BigDecimal("-92.40"), obj2.getAmount());
    }

    @Test
    public void shouldThrowExceptionByTryUpdateAmountWithZero() {
        assertThrows(AmountEqualZeroException.class, () -> obj1.updateAmount(BigDecimal.ZERO));
    }

    @Test
    public void shouldUpdateDueDateCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateDueDate(null));
        LocalDate date = LocalDate.of(1970, Month.JANUARY, 1);
        obj2.updateDueDate(date);
        assertEquals(date, obj2.getDueDate());
    }

    @Test
    public void shouldUpdateNameCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateName(null));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("fv"));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("ShouldBeRaisedForHavingANameLessThanTheMaximumSize"));
        obj1.updateName("Disney Travels");
        assertEquals("Disney Travels", obj1.getName());
    }

    @Test
    public void shouldUpdateDescriptionCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateDescription(null));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateDescription("Sm"));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateDescription(
                "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescriptionShouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription"));
        obj1.updateDescription("My Description...");
        assertEquals("My Description...", obj1.getDescription());
    }

    @Test
    public void shouldUpdateCategoryCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateCategory(null));
        obj1.updateCategory(category2);
        assertEquals(category2, obj1.getCategory());
    }

    @Test
    public void shouldUpdatePayeeCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updatePayee(null));
        IPayee payee1 = new Payee("Sweet drop");
        obj1.updatePayee(payee1);
        assertEquals(payee1, obj1.getPayee());
    }

    @Test
    public void shouldUpdateMovementTypeCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateMovementType(null));
        obj1.updateMovementType(DEBIT);
        assertTrue(obj1.isDebit());
        assertFalse(obj1.isCredit());
        assertEquals(new BigDecimal("-33.50"), obj1.getAmount());
    }

    @Test
    public void shouldUpdateFormOfPaymentCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateFormOfPayment(null));
        IFormOfPayment formOfPayment1 = new FormOfPayment("Transfer");
        obj1.updateFormOfPayment(formOfPayment1);
        assertEquals(formOfPayment1, obj1.getFormOfPayment());
    }

    @Test
    public void shouldUpdateRepetitionFrequencyCorrectly() {
        assertThrows(NullArgumentException.class, () -> obj1.updateRepetitionFrequency(null));
        obj1.updateRepetitionFrequency(YEARLY);
        assertEquals(YEARLY, obj1.getRepetitionFrequency());
    }

    @Test
    public void shouldIdentifyARecurrence() {
        IMovement movement1 = new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, WEEKLY, UUID.randomUUID());

        assertTrue(movement1.isRecurrent());
        assertFalse(obj1.isRecurrent());
    }

    @Test
    public void shouldIdentifyAInstallment() {
        IMovement movement1 = new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());

        assertTrue(movement1.isInstallment());
        assertFalse(obj1.isRecurrent());
    }

    @Test
    public void shouldIdentifyAIndividualMovement() {
        IMovement movement1 = new Movement("Christmas shopping",
                new BigDecimal("33.50"),
                LocalDate.now(),
                formOfPayment,
                payee,
                category1, CREDIT, NONE, null);
        assertTrue(movement1.isCommonMovement());
        assertFalse(movement1.isInstallment());
        assertFalse(obj1.isRecurrent());
    }

    @Test
    public void shouldBeEquals() {
        IMovement obj3 = obj1.clone();
        IMovement obj4 = obj3.clone();
        obj3.updateMovementType(DEBIT);
        obj4.updateName("Travel");
        assertEquals(obj1, obj3);
        assertEquals(obj3, obj4);
        assertEquals(obj1, obj4);
    }

    @Test
    public void shouldBeDeepEquals() {
        IMovement obj3 = obj1.clone();
        IMovement obj4 = obj3.clone();
        assertTrue(obj1.isDeepEquals(obj3));
        assertTrue(obj3.isDeepEquals(obj4));
        assertTrue(obj1.isDeepEquals(obj4));
        obj3.updateName("Travel");
        assertFalse(obj1.isDeepEquals(obj3));
    }

    @Test
    public void shouldBeAClone() {
        IMovement obj3 = obj1.clone();
        IMovement obj4 = obj1.clone();
        assertTrue(obj1.isDeepEquals(obj3));
        assertTrue(obj3.isDeepEquals(obj4));
        assertTrue(obj1.isDeepEquals(obj4));
        obj3.updateMovementType(DEBIT);
        obj4.updateName("Travel");
        assertEquals(obj1, obj3);
        assertEquals(obj3, obj4);
        assertEquals(obj1, obj4);
    }

    @Test
    public void shouldBeDifferent() {
        assertNotEquals(obj1, obj2);
    }

    @Test
    public void shouldReturnAReferenceCorrectly() {
        YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth());
        YearMonth yearMonth2 = YearMonth.of(1970, Month.JANUARY);
        LocalDate date = LocalDate.of(1970, Month.JANUARY, 1);
        assertEquals(yearMonth, obj1.getReference());
        obj2.updateDueDate(date);
        assertEquals(yearMonth2, obj2.getReference());
    }

    @Test
    public void shouldSortByDateCorrectly() {
        LocalDate date = LocalDate.of(1970, Month.JANUARY, 1);
        obj2.updateDueDate(date);
        List<IMovement> movements = new ArrayList<>();
        movements.add(obj1);
        movements.add(obj2);
        assertEquals(obj1, movements.get(0));
        Collections.sort(movements);
        assertEquals(obj2, movements.get(0));
    }

    @Test
    public void shouldSortByAmountCorrectly() {
        LocalDate date = LocalDate.of(1970, Month.JANUARY, 1);
        obj2.updateDueDate(date);
        obj1.updateMovementType(DEBIT);
        List<IMovement> movements = new ArrayList<>();
        movements.add(obj2);
        movements.add(obj1);
        assertEquals(obj2, movements.get(0));
        movements.sort(COMPARE_FOR_AMOUNT);
        assertEquals(obj1, movements.get(0));
    }
}
