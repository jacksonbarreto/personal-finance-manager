package entities;

import bll.entities.*;
import bll.exceptions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;

import static bll.enumerators.EHandlingMode.JUST_THIS_ONE;
import static bll.enumerators.EOperationType.CREDIT;
import static bll.enumerators.EOperationType.DEBIT;
import static bll.enumerators.ERepetitionFrequency.NONE;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    IFormOfPayment formOfPayment1 = new FormOfPayment("MB Way");
    IPayee payeeFormat = new Payee("Wallet");
    IWallet obj1 = new Wallet("Wallet", "Save money for Disney",
            Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), payeeFormat);
    IPayee payee = new Payee("Continent");
    IFormOfPayment formOfPayment2 = new FormOfPayment("Transfer");
    ITransactionCategory category1 = new TransactionCategory("Mercado");
    ITransactionCategory category2 = new TransactionCategory("Education");
    IMovement movement1 = new Movement("Christmas shopping",
            new BigDecimal("33.50"),
            LocalDate.now(),
            formOfPayment1,
            payee,
            category1, DEBIT);
    IMovement movement2 = new Movement("salary",
            new BigDecimal("122.30"),
            LocalDate.of(1970, Month.JANUARY, 1),
            formOfPayment1,
            payee,
            category2, CREDIT);

    @Test
    public void shouldThrowExceptionBySmallName() {
        assertThrows(InvalidNameSizeException.class, () -> new Wallet("Sm", formOfPayment1, payeeFormat));
        assertThrows(InvalidNameSizeException.class, () -> new Wallet("", formOfPayment1, payeeFormat));
        assertThrows(InvalidNameSizeException.class, () -> new Wallet(" ", formOfPayment1, payeeFormat));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("S"));
    }

    @Test
    public void shouldThrowExceptionByBiggerName() {
        assertThrows(InvalidNameSizeException.class, () -> new Wallet("shouldThrowExceptionByBiggerName " +
                "shouldThrowExceptionByBiggerName", formOfPayment1, payeeFormat));
        assertThrows(InvalidNameSizeException.class, () -> obj1.updateName("shouldThrowExceptionByBiggerName " +
                "shouldThrowExceptionByBiggerName"));
    }

    @Test
    public void shouldThrowExceptionByNullName() {
        assertThrows(NullArgumentException.class, () -> new Wallet(null, formOfPayment1, payeeFormat));
        assertThrows(NullArgumentException.class, () -> obj1.updateName(null));
    }

    @Test
    public void shouldThrowExceptionBySmallDescription() {
        assertThrows(InvalidDescriptionSizeException.class, () -> new Wallet("Wallet",
                "Sm", Currency.getInstance(Locale.getDefault()),
                Collections.singleton(formOfPayment1), payeeFormat));
        assertThrows(InvalidDescriptionSizeException.class, () -> obj1.updateDescription("S"));
    }


    @Test
    public void shouldCreateTransactionWithEmptyDescription() {
        assertDoesNotThrow(() -> new Wallet("Wallet",
                " ",
                Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), payeeFormat));
        assertDoesNotThrow(() -> new Wallet("Wallet",
                "",
                Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), payeeFormat));
        assertDoesNotThrow(() -> obj1.updateDescription(""));
    }

    @Test
    public void shouldThrowExceptionByBiggerDescription() {
        assertThrows(InvalidDescriptionSizeException.class, () -> new Wallet("Wallet",
                "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescriptionShouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription",
                Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), payeeFormat));
        assertThrows(InvalidDescriptionSizeException.class, () -> obj1.updateDescription(
                "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescriptionShouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription" +
                        "shouldThrowExceptionByBiggerDescription shouldThrowExceptionByBiggerDescription"));
    }

    @Test
    public void shouldThrowExceptionByNullWallet() {
        assertThrows(NullArgumentException.class, () -> new Wallet(null));
    }

    @Test
    public void shouldThrowExceptionByNullDescription() {
        assertThrows(NullArgumentException.class, () -> new Wallet("Wallet", null,
                Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), payeeFormat));
        assertThrows(NullArgumentException.class, () -> obj1.updateDescription(null));
    }

    @Test
    public void shouldThrowExceptionByNullCurrency() {
        assertThrows(NullArgumentException.class, () -> new Wallet("Wallet", "Description", null,
                Collections.singleton(formOfPayment1), payeeFormat));
        assertThrows(NullArgumentException.class, () -> obj1.updateCurrency(null));
    }

    @Test
    public void shouldThrowExceptionByNullFormOfPayment() {
        assertThrows(NullArgumentException.class, () -> obj1.addFormOfPayment(null));
        assertThrows(NullArgumentException.class, () -> new Wallet("Wallet", " ",
                Currency.getInstance(Locale.getDefault()), null, payeeFormat));
    }

    @Test
    public void shouldThrowExceptionByFormOfPaymentEmpty() {
        assertThrows(ProhibitedLessFormOfPaymentException.class, () -> new Wallet("wallet", "Description",
                Currency.getInstance(Locale.getDefault()), Collections.emptyList(), payeeFormat));
    }

    @Test
    public void shouldThrowExceptionByPayeeFormatNull() {
        assertThrows(NullArgumentException.class, () -> new Wallet("wallet", "Description",
                Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), null));
    }

    @Test
    public void shouldThrowExceptionByNullMovement() {
        assertThrows(NullArgumentException.class, () -> obj1.addMovement(null));
        assertThrows(NullArgumentException.class, () -> obj1.removeMovement(null));
        assertThrows(NullArgumentException.class, () -> obj1.confirmMovement(null));
        assertThrows(NullArgumentException.class, () -> obj1.removeInstallment(null, JUST_THIS_ONE));
        assertThrows(NullArgumentException.class, () -> obj1.updateInstallment(null, JUST_THIS_ONE));
        assertThrows(NullArgumentException.class, () -> obj1.updateMovement(null));
    }


    @Test
    public void shouldThrowExceptionForTryingToRemoveNonexistentMovement() {
        assertThrows(NonExistentMovementException.class, () -> obj1.removeMovement(movement1));
        assertThrows(NonExistentMovementException.class, () -> obj1.removeInstallment(movement1,JUST_THIS_ONE));
    }

    @Test
    public void shouldThrowExceptionForTryingToAddExistingMovement() {
        obj1.addMovement(movement1);
        assertThrows(ExistingMovementException.class, () -> obj1.addMovement(movement1));
    }


    @Test
    public void shouldHaveTheCorrectName() {
        assertEquals("Wallet", obj1.getName());
    }


    @Test
    public void shouldHaveTheCorrectDescription() {
        assertEquals("Save money for Disney", obj1.getDescription());
    }

    @Test
    public void shouldHaveTheCorrectCurrency() {
        assertEquals(Currency.getInstance(Locale.getDefault()), obj1.getCurrency());
    }

    @Test
    public void shouldThrowExceptionWhenAddingMovementWithPaymentMethodThatNotExistInWallet() {
        movement1.updateFormOfPayment(formOfPayment2);
        assertThrows(IllegalFormOfPaymentException.class, () -> obj1.addMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByIllegalFormOfPaymentInConfirmMovement() {
        obj1.addMovement(movement2);
        movement2.updateFormOfPayment(formOfPayment2);
        assertThrows(IllegalFormOfPaymentException.class, () -> obj1.confirmMovement(movement2));
    }

    @Test
    public void shouldAddAMovement() {
        assertTrue(obj1.getMonthOperations().isEmpty());
        assertTrue(obj1.getMovements().isEmpty());
        obj1.addMovement(movement1);
        assertEquals(1, obj1.getMonthOperations().size());
        assertTrue(obj1.getMonthOperations().contains(movement1));

        assertEquals(1, obj1.getMovements().size());
        assertTrue(obj1.getMovements().contains(movement1));
    }

    @Test
    public void shouldRemoveAMovement() {
        assertTrue(obj1.getMonthOperations().isEmpty());
        obj1.addMovement(movement1);
        assertEquals(1, obj1.getMonthOperations().size());
        assertTrue(obj1.getMonthOperations().contains(movement1));
        obj1.removeMovement(movement1);
        assertFalse(obj1.getMonthOperations().contains(movement1));
        assertTrue(obj1.getMonthOperations().isEmpty());

        assertTrue(obj1.getMovements().isEmpty());
        obj1.addMovement(movement1);
        assertEquals(1, obj1.getMovements().size());
        assertTrue(obj1.getMovements().contains(movement1));
        obj1.removeMovement(movement1);
        assertFalse(obj1.getMovements().contains(movement1));
        assertTrue(obj1.getMovements().isEmpty());
    }

    @Test
    public void shouldUpdateAMovementCorrectly(){
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());

        assertThrows(NullArgumentException.class, ()-> obj1.updateMovement(null));
        assertThrows(NonExistentMovementException.class, ()-> obj1.updateMovement(movement1));

        obj1.addMovement(movement2);
        movement2.updateFormOfPayment(formOfPayment2);
        assertThrows(IllegalFormOfPaymentException.class, ()-> obj1.updateMovement(movement2));
       // obj1.removeMovement(movement2);

        obj1.addMovement(installment);
        assertThrows(InstallmentWithoutHandlingMode.class, ()-> obj1.updateMovement(installment));
        //obj1.removeMovement(installment);
        assertEquals(2, obj1.getMovements().size());
        obj1.addMovement(movement1);
        assertEquals(3, obj1.getMovements().size());
        movement1.updateName("New Movement Name");
        obj1.updateMovement(movement1);
        List<IMovement> movements = new ArrayList<>(obj1.getMovements());
        assertEquals("New Movement Name", movements.get(1).getName());

    }

    @Test
    public void shouldRemoveAInstallment(){

    }

    @Test
    public void shouldReturnTheOperationsOfTheMonth() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertEquals(1, obj1.getMonthOperations().size());
        assertEquals(1, obj1.getMonthOperations(YearMonth.now()).size());
        assertEquals(1, obj1.getMonthOperations(YearMonth.of(1970, 1)).size());
    }

    @Test
    public void shouldReturnTheOperationsOfYear() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertEquals(1, obj1.getYearOperations().size());
        assertEquals(1, obj1.getYearOperations(Year.now()).size());
        assertEquals(1, obj1.getYearOperations(Year.of(1970)).size());
    }

    @Test
    public void shouldReturnTheOperationsBetween() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertEquals(2, obj1.getOperationsBetween(YearMonth.of(1970, 1), YearMonth.now()).size());
        assertEquals(1, obj1.getOperationsBetween(YearMonth.of(1990, 1), YearMonth.now()).size());
        assertTrue(obj1.getOperationsBetween(YearMonth.of(1990, 1), YearMonth.now().minusMonths(2)).isEmpty());
    }


    @Test
    public void shouldThrowExceptionByInsufficientFunds() {
        obj1.addMovement(movement1);
        assertThrows(InsufficientFundsException.class, () -> obj1.confirmMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByNonExistentMovementForConfirm() {
        obj1.addMovement(movement1);
        assertThrows(NonExistentMovementException.class, () -> obj1.confirmMovement(movement2));
    }

    @Test
    public void shouldConfirmAMovement() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertDoesNotThrow(() -> obj1.confirmMovement(movement2));
        assertEquals(1, obj1.getTransactions().size());
    }


    @Test
    public void shouldHaveTheCorrectBalance() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertEquals(BigDecimal.ZERO, obj1.getBalance());
        obj1.confirmMovement(movement2);
        obj1.confirmMovement(movement1);
        assertEquals(new BigDecimal("88.80"), obj1.getBalance());

    }


    @Test
    public void shouldHaveTheCorrectCashInFlow() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        obj1.confirmMovement(movement2);
        obj1.confirmMovement(movement1);
        assertEquals(BigDecimal.ZERO, obj1.getCashInflow());
        assertEquals(BigDecimal.ZERO, obj1.getCashInflowInYear());
        assertEquals(movement2.getAmount(), obj1.getCashInflow(YearMonth.of(1970, 1)));
        assertEquals(movement2.getAmount(), obj1.getCashInflowInYear(Year.of(1970)));
    }

    @Test
    public void shouldHaveTheCorrectCashInFlowExpected() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        obj1.confirmMovement(movement2);
        obj1.confirmMovement(movement1);
        assertEquals(BigDecimal.ZERO, obj1.getCashInflow());
        assertEquals(BigDecimal.ZERO, obj1.getCashInflowInYear());
        assertEquals(movement2.getAmount(), obj1.getCashInflow(YearMonth.of(1970, 1)));
        assertEquals(movement2.getAmount(), obj1.getCashInflowInYear(Year.of(1970)));
    }

    @Test
    public void shouldHaveTheCorrectCashOutFlow() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        obj1.confirmMovement(movement2);

        assertEquals(movement1.getAmount(), obj1.getCashOutflowExpected());
        assertEquals(movement1.getAmount(), obj1.getCashOutflowInYearExpected());
        assertEquals(BigDecimal.ZERO, obj1.getCashOutflowExpected(YearMonth.of(1970, 1)));
        assertEquals(BigDecimal.ZERO, obj1.getCashOutflowInYearExpected(Year.of(1970)));
    }

    @Test
    public void shouldHaveTheCorrectCashOutFlowExpected() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        obj1.confirmMovement(movement2);

        assertEquals(movement1.getAmount(), obj1.getCashOutflowExpected());
        assertEquals(movement1.getAmount(), obj1.getCashOutflowInYearExpected());
        assertEquals(BigDecimal.ZERO, obj1.getCashOutflowExpected(YearMonth.of(1970, 1)));
        assertEquals(BigDecimal.ZERO, obj1.getCashOutflowInYearExpected(Year.of(1970)));
    }

    @Test
    public void shouldThrowAnExceptionToRemoveAllFormsOfPayment() {
        assertThrows(ProhibitedLessFormOfPaymentException.class, () -> obj1.removeFormOfPayment(formOfPayment1));
    }

    @Test
    public void shouldBeEquals() {

    }

    @Test
    public void shouldBeDeepEquals() {

    }

    @Test
    public void shouldBeDifferent() {

    }

    @Test
    public void shouldBeAClone() {

    }
}
