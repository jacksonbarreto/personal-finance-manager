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

import static bll.builders.IMovementBuilder.makeMovement;
import static bll.entities.IWallet.COMPARE_BY_ESTIMATED_BALANCE;
import static bll.enumerators.EHandlingMode.*;
import static bll.enumerators.EOperationType.CREDIT;
import static bll.enumerators.EOperationType.DEBIT;
import static bll.enumerators.ERepetitionFrequency.*;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    IFormOfPayment formOfPayment1 = new FormOfPayment("MB Way");
    IPayee payeeFormat = new Payee("Wallet");
    IWallet obj1 = new Wallet("Wallet", "Save money for Disney",
            Currency.getInstance(Locale.getDefault()), Collections.singleton(formOfPayment1), payeeFormat);
    IPayee payee = new Payee("Continent");
    IFormOfPayment formOfPayment2 = new FormOfPayment("Transfer");
    IMovementCategory category1 = new MovementCategory("Mercado");
    IMovementCategory category2 = new MovementCategory("Education");
    IMovement movement1 = makeMovement("Christmas shopping", "33.50",
            LocalDate.now(), formOfPayment1, payee, category1, DEBIT).build();

    IMovement movement2 = makeMovement("salary", "122.30",
            LocalDate.of(1970, Month.JANUARY, 1), formOfPayment1, payee, category2, CREDIT).build();

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
    public void shouldThrowExceptionForTryingToAddExistingMovement() {
        obj1.addMovement(movement1);
        assertThrows(ExistingMovementException.class, () -> obj1.addMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByTryAddInactiveMovement() {
        movement1.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.addMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByTryAddAAccomplishedMovement() {
        movement2.accomplish();
        assertThrows(MovementAlreadyAccomplishException.class, () -> obj1.addMovement(movement2));
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
    public void shouldThrowExceptionForTryingToRemoveNonexistentMovement() {
        assertThrows(NonExistentMovementException.class, () -> obj1.removeMovement(movement1));
    }


    @Test
    public void shouldThrowExceptionForTryingToRemoveAMovementWithoutHandlingMode() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        obj1.addInstallment(installment, WEEKLY, 2);
        assertThrows(InstallmentWithoutHandlingMode.class, () -> obj1.removeMovement(installment));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveAAccomplishedMovementOutside() {
        obj1.addMovement(movement2);
        movement2.accomplish();
        assertThrows(MovementAlreadyAccomplishException.class, () -> obj1.removeMovement(movement2));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveAAccomplishedMovementInside() {
        obj1.addMovement(movement2);
        obj1.confirmMovement(movement2);
        assertThrows(MovementAlreadyAccomplishException.class, () -> obj1.removeMovement(movement2));
    }

    @Test
    public void shouldThrowExceptionByTryRemoveInactiveMovement() {

        movement1.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.removeMovement(movement1));
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
    }

    @Test
    public void shouldThrowExceptionByTryUpdateExcludedMovement() {
        obj1.addMovement(movement1);
        obj1.removeMovement(movement1);
        assertThrows(AttemptedToUseExcludedMovementException.class, () -> obj1.updateMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateExcludedInstallment() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());

        obj1.addInstallment(installment, MONTHLY, 6);
        obj1.removeInstallment(installment, ALL);
        assertThrows(AttemptedToUseExcludedMovementException.class, () -> obj1.updateInstallment(installment, ALL));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateInactiveMovement() {
        movement1.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.updateMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByTryUpdateInactiveInstallment() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        installment.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.updateInstallment(installment, THIS_AND_NEXT));
    }

    @Test
    public void shouldUpdateAMovementCorrectly() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());

        assertThrows(NullArgumentException.class, () -> obj1.updateMovement(null));
        assertThrows(NonExistentMovementException.class, () -> obj1.updateMovement(movement2));
        assertTrue(obj1.getMovements().isEmpty());
        obj1.addMovement(movement2);
        movement2.updateFormOfPayment(formOfPayment2);
        assertThrows(IllegalFormOfPaymentException.class, () -> obj1.updateMovement(movement2));

        obj1.addInstallment(installment, QUARTERLY, 2);
        assertThrows(InstallmentWithoutHandlingMode.class, () -> obj1.updateMovement(installment));
        obj1.removeInstallment(installment, ALL);

        obj1.addMovement(movement1);
        movement1.updateName("New Movement Name");
        obj1.updateMovement(movement1);
        List<IMovement> movements = new ArrayList<>(obj1.getMovements());
        assertEquals("New Movement Name", movements.get(1).getName());

    }

    @Test
    public void shouldThrowExceptionByTryRemoveInactiveInstallment() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        installment.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.removeInstallment(installment, THIS_AND_NEXT));
    }

    @Test
    public void shouldThrowExceptionByTryAddInactiveInstallment() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        installment.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.addInstallment(installment, MONTHLY, 5));
    }

    @Test
    public void shouldThrowExceptionByTryAddExcludedMovement() {
        obj1.addMovement(movement1);
        obj1.removeMovement(movement1);
        assertThrows(AttemptedToUseExcludedMovementException.class, () -> obj1.addMovement(movement1));
    }

    @Test
    public void shouldThrowExceptionByTryAddExcludedInstallment() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        obj1.addInstallment(installment, MONTHLY, 6);
        obj1.removeInstallment(installment, JUST_THIS_ONE);
        assertThrows(AttemptedToUseExcludedMovementException.class, () -> obj1.addInstallment(installment, MONTHLY, 6));
    }

    @Test
    public void shouldAddInstallment() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        assertThrows(NullArgumentException.class, () -> obj1.addInstallment(null, WEEKLY, 20));
        assertThrows(NullArgumentException.class, () -> obj1.addInstallment(installment, null, 20));
        assertThrows(DontIsInstallmentException.class, () -> obj1.addInstallment(movement1, YEARLY, 20));
        assertThrows(IllegalInstallmentQuantityException.class, () -> obj1.addInstallment(installment, YEARLY, 1));

        obj1.addInstallment(installment, MONTHLY, 5);
        assertEquals(5, obj1.getMovements().size());
        assertThrows(ExistingMovementException.class, () -> obj1.addInstallment(installment, YEARLY, 20));
        int i = 0;
        for (IMovement m : obj1.getMovements()) {
            assertEquals(LocalDate.now().plusMonths(i), m.getDueDate());
            i++;
        }
    }

    @Test
    public void shouldRemoveAInstallment() {
        UUID groupID = UUID.randomUUID();
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, groupID);

        assertThrows(NullArgumentException.class, () -> obj1.removeInstallment(null, NEXT));
        assertThrows(DontIsInstallmentException.class, () -> obj1.removeInstallment(movement1, NEXT));

        obj1.addInstallment(installment, MONTHLY, 6);
        assertThrows(NullArgumentException.class, () -> obj1.removeInstallment(installment, null));
        assertEquals(6, obj1.getMovements().size());

        obj1.removeInstallment(installment, THIS_AND_NEXT);
        assertTrue(obj1.getMovements().isEmpty());
        assertThrows(AttemptedToUseExcludedMovementException.class, () -> obj1.removeInstallment(installment, THIS_AND_NEXT));

        IMovement installment2 = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, groupID);

        List<IMovement> movements;
        obj1.addInstallment(installment2, MONTHLY, 6);
        movements = new ArrayList<>(obj1.getMovements());
        obj1.removeInstallment(movements.get(3), NEXT);
        assertEquals(4, obj1.getMovements().size());
        obj1.removeInstallment(movements.get(1), THIS_AND_PREVIOUS);
        assertEquals(2, obj1.getMovements().size());
        obj1.removeInstallment(movements.get(3), PREVIOUS);
        assertEquals(1, obj1.getMovements().size());
        obj1.removeInstallment(movements.get(3), JUST_THIS_ONE);
        assertTrue(obj1.getMovements().isEmpty());
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
    public void shouldThrowExceptionByTryConfirmInactiveMovement() {
        movement2.inactivate();
        assertThrows(InactiveMovementException.class, () -> obj1.confirmMovement(movement2));
    }

    @Test
    public void shouldThrowExceptionByTryConfirmExcludedMovement() {
        obj1.addMovement(movement2);
        obj1.removeMovement(movement2);
        assertThrows(AttemptedToUseExcludedMovementException.class, () -> obj1.confirmMovement(movement2));
    }

    @Test
    public void shouldThrowExceptionByNonExistentMovementForConfirm() {
        obj1.addMovement(movement1);
        assertThrows(NonExistentMovementException.class, () -> obj1.confirmMovement(movement2));
    }

    @Test
    public void shouldThrowExceptionByTryConfirmMovementAccomplishedOut() {
        obj1.addMovement(movement2);
        movement2.accomplish();
        assertThrows(MovementAlreadyAccomplishException.class, () -> obj1.confirmMovement(movement2));
    }

    @Test
    public void shouldThrowExceptionByTryConfirmMovementAccomplishedInside() {
        obj1.addMovement(movement2);
        obj1.confirmMovement(movement2);
        assertThrows(MovementAlreadyAccomplishException.class, () -> obj1.confirmMovement(movement2));
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
        assertEquals(movement2.getAmount(), obj1.getCashInflow());
        assertEquals(movement2.getAmount(), obj1.getCashInflowInYear());
        assertEquals(BigDecimal.ZERO, obj1.getCashInflow(YearMonth.of(1970, 1)));
        assertEquals(BigDecimal.ZERO, obj1.getCashInflowInYear(Year.of(1970)));
    }

    @Test
    public void shouldHaveTheCorrectCashInFlowExpected() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertEquals(BigDecimal.ZERO, obj1.getCashInflowInYear());
        assertEquals(BigDecimal.ZERO, obj1.getCashInflow());
        obj1.confirmMovement(movement2);
        obj1.confirmMovement(movement1);

        assertEquals(movement2.getAmount(), obj1.getCashInflowExpected(YearMonth.of(1970, 1)));
        assertEquals(BigDecimal.ZERO, obj1.getCashInflow(YearMonth.of(1970, 1)));
        assertEquals(movement2.getAmount(), obj1.getCashInflowInYearExpected(Year.of(1970)));
        assertEquals(BigDecimal.ZERO, obj1.getCashInflowInYear(Year.of(1970)));
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
        IWallet obj2 = obj1.clone();
        obj2.updateName("Wallet 2");
        IWallet obj3 = obj2.clone();
        obj3.updateName("Wallet 3");

        assertEquals(obj1, obj2);
        assertEquals(obj2, obj3);
        assertEquals(obj1, obj3);
    }

    @Test
    public void shouldBeDeepEquals() {
        IWallet obj2 = obj1.clone();
        assertTrue(obj1.isDeepEquals(obj1.clone()));
        assertTrue(obj2.isDeepEquals(obj1));
    }

    @Test
    public void shouldBeDifferent() {
        IWallet obj2 = obj1.clone();
        obj2.updateName("Wallet 2");
        assertFalse(obj1.isDeepEquals(obj2));
    }

    @Test
    public void shouldBeAClone() {
        assertEquals(obj1, obj1.clone());
    }

    @Test
    public void shouldCashOutFlowCorrectly() {
        obj1.addMovement(movement1);
        obj1.addMovement(movement2);
        assertEquals(new BigDecimal("-33.50"), obj1.getCashOutflowExpected());
        assertEquals(BigDecimal.ZERO, obj1.getCashOutflow());
        obj1.confirmMovement(movement2);
        obj1.confirmMovement(movement1);
        assertEquals(new BigDecimal("-33.50"), obj1.getCashOutflowExpected());
        assertEquals(new BigDecimal("-33.50"), obj1.getCashOutflow());
        assertEquals(new BigDecimal("-33.50"), obj1.getCashOutflowInYear());
        assertEquals(BigDecimal.ZERO, obj1.getCashOutflowInYear(Year.of(1970)));
    }

    @Test
    public void shouldCashInFlowExpectedCorrectly() {
        obj1.addMovement(movement2);
        assertEquals(new BigDecimal("122.30"), obj1.getCashInflowExpected(YearMonth.of(1970, Month.JANUARY)));
        assertEquals(new BigDecimal("122.30"), obj1.getCashInflowInYearExpected(Year.of(1970)));
        IMovement movement3 = new Movement("New credit",
                new BigDecimal("80.30"),
                LocalDate.now(),
                formOfPayment1,
                payee,
                category2, CREDIT);
        obj1.addMovement(movement3);
        assertEquals(new BigDecimal("80.30"), obj1.getCashInflowExpected());
        assertEquals(new BigDecimal("80.30"), obj1.getCashInflowInYearExpected());
    }

    @Test
    public void shouldSort() {
        IMovement installment = new Movement("A Installment",
                new BigDecimal("33.60"),
                LocalDate.of(1970, Month.JANUARY, 1),
                formOfPayment1,
                payee,
                category1, CREDIT, NONE, UUID.randomUUID());
        IWallet obj2 = obj1.clone();
        obj2.updateName("wallet 2");
        List<IWallet> wallets = new ArrayList<>(Arrays.asList(obj2, obj1));

        obj2.addInstallment(installment, MONTHLY, 5);
        obj1.addMovement(movement2);

        assertEquals(obj1.getName(), wallets.get(1).getName());
        Collections.sort(wallets);
        assertEquals(obj1.getName(), wallets.get(1).getName());

        wallets.sort(COMPARE_BY_ESTIMATED_BALANCE);
        assertEquals(obj2.getName(), wallets.get(1).getName());

        obj1.confirmMovement(movement2);
        Collections.sort(wallets);
        assertEquals(obj1.getName(), wallets.get(1).getName());

        for (IMovement m : obj2.getMovements())
            obj2.confirmMovement(m);
        Collections.sort(wallets);
        assertEquals(obj2.getName(), wallets.get(1).getName());
    }
}
