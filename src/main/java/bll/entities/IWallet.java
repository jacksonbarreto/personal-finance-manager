package bll.entities;

import bll.enumerators.EHandlingMode;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Currency;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IWallet extends Serializable, Comparable<IWallet>, Cloneable {
    int MINIMUM_NAME_SIZE = 3;
    int MAXIMUM_NAME_SIZE = 30;
    int MINIMUM_DESCRIPTION_SIZE = 3;
    int MAXIMUM_DESCRIPTION_SIZE = 250;
    String EMPTY_DESCRIPTION = "";
    Predicate<String> INCORRECT_NAME_SIZE = (s) -> (s.length() < MINIMUM_NAME_SIZE || s.length() > MAXIMUM_NAME_SIZE);
    Predicate<String> INCORRECT_DESCRIPTION_SIZE = (s) -> !s.isEmpty() && (s.length() < MINIMUM_DESCRIPTION_SIZE || s.length() > MAXIMUM_DESCRIPTION_SIZE);
    Comparator<IWallet> COMPARE_BY_ESTIMATED_BALANCE = Comparator.comparing(IWallet::getBalanceExpected);

    /**
     * Updates all its attributes from an external copy.
     *
     * @param externalCopy of the original element.
     * @throws DifferentObjectException if the object sent does not have the same id.
     * @throws NullArgumentException    if the argument is null.
     */
    void autoUpdate(IWallet externalCopy);

    /**
     * Returns the unique identifier of the wallet.
     *
     * @return the unique identifier of the wallet.
     */
    UUID getID();

    /**
     * Returns the name of the wallet.
     *
     * @return the name of the wallet.
     */
    String getName();

    /**
     * Returns the description of the wallet.
     *
     * @return the description of the wallet OR null if the operation has no description..
     */
    String getDescription();

    /**
     * Returns a collection of payment methods supported by the wallet.
     *
     * @return a collection of payment methods supported by the wallet.
     */
    Set<IFormOfPayment> getFormOfPayment();

    /**
     * Returns the currency of the wallet.
     *
     * @return the currency of the wallet.
     */
    Currency getCurrency();

    /**
     * Returns the representation of the portfolio as a Payee,
     * in order to be able to identify the source and destination wallet
     * during the transfer of values between portfolios.
     *
     * @return the representation of the portfolio as a Payee.
     */
    IPayee getPayeeFormat();

    /**
     * Adds a new movement to the portfolio.
     *
     * @param movement a new movement to the portfolio.
     * @throws NullArgumentException              if the argument is null.
     * @throws ExistingMovementException          if the movement already exists.
     * @throws IllegalFormOfPaymentException      if the form of payment does not exist in the wallet.
     * @throws InstallmentForbiddenException      if you try to add an installment.
     * @throws MovementAlreadyAccomplishException If the movement is already accomplished.
     * @throws InactiveMovementException          if you try to use an inactive movement as a parameter.
     */
    void addMovement(IMovement movement);

    /**
     * Creates all the installment movements.
     *
     * @param movement             basic (initial) movement.
     * @param frequency            repetition
     * @param numberOfInstallments number of installment.
     * @throws NullArgumentException               if the argument is null.
     * @throws ExistingMovementException           if the movement already exists.
     * @throws DontIsInstallmentException          if the movement is not an installment plan.
     * @throws IllegalInstallmentQuantityException if the number of plots is less than 2.
     * @throws MovementAlreadyAccomplishException  If the movement is already accomplished.
     * @throws InactiveMovementException           if you try to use an inactive movement as a parameter.
     */
    void addInstallment(IMovement movement, ERepetitionFrequency frequency, int numberOfInstallments);

    /**
     * Removes a movement from the wallet.
     *
     * @param movement to be removed.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws InstallmentWithoutHandlingMode          if the movement is in installments.
     * @throws MovementAlreadyAccomplishException      If the movement is already accomplished.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    void removeMovement(IMovement movement);

    /**
     * Remove an installment.
     *
     * @param installment  to be removed.
     * @param handlingMode How the removal should take place.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws DontIsInstallmentException              if the movement is not an installment plan.
     * @throws MovementAlreadyAccomplishException      If the movement is already accomplished.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    void removeInstallment(IMovement installment, EHandlingMode handlingMode);

    /**
     * Confirms a movement in the wallet, turning it into a transaction.
     *
     * @param movement to be confirmed.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws IllegalFormOfPaymentException           if the form of payment does not exist in the wallet.
     * @throws InsufficientFundsException              if the wallet does not have funds to support this transaction.
     * @throws MovementAlreadyAccomplishException      If the movement is already accomplished.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    void confirmMovement(IMovement movement);

    /**
     * Confirms a movement in the wallet, turning it into a transaction.
     *
     * @param movement       to be confirmed.
     * @param accomplishDate to be confirmed.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws IllegalFormOfPaymentException           if the form of payment does not exist in the wallet.
     * @throws InsufficientFundsException              if the wallet does not have funds to support this transaction.
     * @throws MovementAlreadyAccomplishException      If the movement is already accomplished.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    void confirmMovement(IMovement movement, LocalDate accomplishDate);

    /**
     * Adds a new payment method to the wallet.
     *
     * @param formOfPayment new payment method to the wallet.
     * @throws NullArgumentException          if the argument is null.
     * @throws ExistingFormOfPaymentException if the payment method already existing.
     */
    void addFormOfPayment(IFormOfPayment formOfPayment);

    /**
     * Removes a form of payment from the wallet.
     *
     * @param formOfPayment to be removed.
     * @throws NullArgumentException                if the argument is null.
     * @throws ProhibitedLessFormOfPaymentException if the wallet has only one form of payment.
     */
    void removeFormOfPayment(IFormOfPayment formOfPayment);

    /**
     * Allows you to change the name of the wallet.
     *
     * @param newName the new name of the wallet.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    void updateName(String newName);

    /**
     * Updates the description of the wallet.
     *
     * @param newDescription new description of the wallet.
     * @throws InvalidDescriptionSizeException if the size of the description attribute
     *                                         does not correspond to the minimum and / or maximum limits
     *                                         defined in an MINIMUM_DESCRIPTION_SIZE and MAXIMUM_DESCRIPTION_SIZE.
     * @throws NullArgumentException           if the argument is null.
     */
    void updateDescription(String newDescription);

    /**
     * Change the currency of the wallet.
     *
     * @param newCurrency of the wallet.
     * @throws NullArgumentException if the argument is null.
     */
    void updateCurrency(Currency newCurrency);

    /**
     * Update an movement.
     *
     * @param movement to be updated.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws InstallmentWithoutHandlingMode          if the movement is in installments.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    void updateMovement(IMovement movement);

    /**
     * Update an installment.
     *
     * @param installment  to be updated.
     * @param handlingMode How the update should take place.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws DontIsInstallmentException              if the movement is not an installment plan.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    void updateInstallment(IMovement installment, EHandlingMode handlingMode);

    /**
     * Returns a collection with all the movements of the wallet.
     *
     * @return a collection with all the movements of the wallet.
     */
    Set<IMovement> getMovements();

    /**
     * Returns a collection of all transactions in the wallet.
     *
     * @return a collection of all transactions in the wallet.
     */
    Set<IMovement> getTransactions();

    /**
     * Returns a collection of Operation for the current month/year.
     *
     * @return a collection of Operation for the current month.
     */
    Set<IMovement> getMonthOperations();

    /**
     * Returns a collection of Operation from the reference.
     *
     * @param reference month / year.
     * @return a collection of Operation from the reference.
     * @throws NullArgumentException if the argument is null.
     */
    Set<IMovement> getMonthOperations(YearMonth reference);

    /**
     * Returns a collection of Operation for the current year.
     *
     * @return a collection of Operation for the current year.
     */
    Set<IMovement> getYearOperations();

    /**
     * Returns a collection of Operation for a year.
     *
     * @param year for reference
     * @return a collection of Operation for a year.
     * @throws NullArgumentException if the argument is null.
     */
    Set<IMovement> getYearOperations(Year year);

    /**
     * Returns a collection of operations that occurred in the requested range.
     *
     * @param start initial date
     * @param end   final date
     * @return a collection of operations that occurred in the requested range.
     * @throws NullArgumentException if the argument is null.
     */
    Set<IMovement> getOperationsBetween(YearMonth start, YearMonth end);


    /**
     * Returns the current balance.
     *
     * @return the current balance.
     */
    BigDecimal getBalance();

    /**
     * Returns the reference balance (month / year).
     *
     * @param reference month / year.
     * @return the reference balance (month / year).
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getBalance(YearMonth reference);

    /**
     * Returns the total amount of credit transactions, filtered by their date of accomplish, in the current month.
     *
     * @return the total amount of credit transactions in the current month.
     */
    BigDecimal getCashInflow();

    /**
     * Returns the total value of credit transactions, filtered by their date of accomplish, in the month / year.
     *
     * @param reference month / year.
     * @return the total value of credit transactions in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashInflow(YearMonth reference);

    /**
     * Returns the total amount of credit transactions, filtered by their date of accomplish, in the current year.
     *
     * @return the total amount of credit transactions, filtered by their date of accomplish, in the current year.
     */
    BigDecimal getCashInflowInYear();

    /**
     * Returns the total value of credit transactions, filtered by their date of accomplish, in the year.
     *
     * @param year year for calculation.
     * @return the total value of credit transactions, filtered by their date of accomplish, in the year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashInflowInYear(Year year);

    /**
     * Returns the total amount of debit transactions, filtered by their date of accomplish, in the current month.
     *
     * @return the total amount of debit transactions, filtered by their date of accomplish, in the current month.
     */
    BigDecimal getCashOutflow();

    /**
     * Returns the total value of debit transactions, filtered by their date of accomplish, in the month / year.
     *
     * @param reference month / year.
     * @return the total value of debit transactions, filtered by their date of accomplish, in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashOutflow(YearMonth reference);

    /**
     * Returns the total amount of debit transactions, filtered by their date of accomplish, in the current year.
     *
     * @return the total amount of debit transactions, filtered by their date of accomplish, in the current year.
     */
    BigDecimal getCashOutflowInYear();

    /**
     * Returns the total value of debit transactions, filtered by their date of accomplish, in the year.
     *
     * @param year year for calculation.
     * @return the total value of debit transactions, filtered by their date of accomplish, in the year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashOutflowInYear(Year year);

    /**
     * Returns the current balance expected only movement, filtered by their due date.
     *
     * @return the current balance expected only movement, filtered by their due date.
     */
    BigDecimal getBalanceExpected();

    /**
     * Returns the reference balance expected only movement, filtered by their due date.
     *
     * @param reference month / year.
     * @return the reference balance expected only movement, filtered by their due date.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getBalanceExpected(YearMonth reference);

    /**
     * Returns the total amount of credit movement, filtered by their due date, in the current month.
     *
     * @return the total amount of credit transactions and movement in the current month.
     */
    BigDecimal getCashInflowExpected();

    /**
     * Returns the total value of credit movement, filtered by their due date,  in the month / year.
     *
     * @param reference month / year.
     * @return the total value of credit transactions and movement in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashInflowExpected(YearMonth reference);

    /**
     * Returns the total amount of credit movement, filtered by their due date, in the current year.
     *
     * @return the total amount of credit movement, filtered by their due date, in the current year.
     */
    BigDecimal getCashInflowInYearExpected();

    /**
     * Returns the total value of credit movement, filtered by their due date, in the year.
     *
     * @param year year for calculation.
     * @return the total value of credit transactions and movement in the year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashInflowInYearExpected(Year year);

    /**
     * Returns the total amount of debit movement, filtered by their due date, in the current month.
     *
     * @return the total amount of debit movement, filtered by their due date, in the current month.
     */
    BigDecimal getCashOutflowExpected();

    /**
     * Returns the total value of debit movement, filtered by their due date, the month / year.
     *
     * @param reference month / year.
     * @return the total value of debit movement, filtered by their due date, in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashOutflowExpected(YearMonth reference);

    /**
     * Returns the total amount of debit movement, filtered by their due date, in the current year.
     *
     * @return the total amount of debit movement, filtered by their due date, in the current year.
     */
    BigDecimal getCashOutflowInYearExpected();

    /**
     * Returns the total value of debit movement, filtered by their due date, in the year.
     *
     * @param year year for calculation.
     * @return the total value of debit movement, filtered by their due date, in the year.
     * @throws NullArgumentException if the argument is null.
     */
    BigDecimal getCashOutflowInYearExpected(Year year);

    IWallet clone();

    boolean equals(Object o);

    boolean isDeepEquals(Object o);
}
