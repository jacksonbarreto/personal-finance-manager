package bll.entities;

import bll.enumerators.EHandlingMode;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;

public class Wallet implements IWallet {
    private UUID ID;
    private String name;
    private String description;
    private Currency currency;
    private Set<IFormOfPayment> formOfPayments;
    private Set<IMovement> movements;
    private Set<ITransaction> transactions;
    private IPayee payeeFormat;

    public Wallet(String name, String description, Currency currency,
                  Collection<? extends IFormOfPayment> formOfPayments, IPayee payeeFormat) {
        if (name == null || description == null || currency == null || formOfPayments == null || payeeFormat == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        if (INCORRECT_DESCRIPTION_SIZE.test(description.trim()))
            throw new InvalidDescriptionSizeException();
        if (formOfPayments.isEmpty())
            throw new ProhibitedLessFormOfPaymentException();
        this.ID = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.formOfPayments = new HashSet<>();
        for (IFormOfPayment f : formOfPayments)
            this.formOfPayments.add(f.clone());
        this.movements = new TreeSet<>();
        this.transactions = new TreeSet<>();
        this.payeeFormat = payeeFormat;
    }

    public Wallet(String name, IFormOfPayment formOfPayment, IPayee payeeFormat) {
        this(name, EMPTY_DESCRIPTION, Currency.getInstance(Locale.getDefault()),
                new HashSet<>(Collections.singletonList(formOfPayment)),payeeFormat);
    }

    @SuppressWarnings("unchecked")
    public Wallet(IWallet wallet) {
        if (wallet == null)
            throw new NullArgumentException();
        this.ID = wallet.getID();
        this.name = wallet.getName();
        this.description = wallet.getDescription();
        this.currency = wallet.getCurrency();
        this.formOfPayments = new HashSet<>();
        for (IFormOfPayment f : wallet.getFormOfPayment())
            this.formOfPayments.add(f.clone());
        this.movements = (Set<IMovement>) copyOperations(wallet.getMovements());
        this.transactions = (Set<ITransaction>) copyOperations(wallet.getTransactions());
        this.payeeFormat = wallet.getPayeeFormat();
    }


    /**
     * Returns the unique identifier of the wallet.
     *
     * @return the unique identifier of the wallet.
     */
    @Override
    public UUID getID() {
        return this.ID;
    }

    /**
     * Returns the name of the wallet.
     *
     * @return the name of the wallet.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description of the wallet.
     *
     * @return the description of the wallet OR null if the operation has no description..
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a collection of payment methods supported by the wallet.
     *
     * @return a collection of payment methods supported by the wallet.
     */
    @Override
    public Set<IFormOfPayment> getFormOfPayment() {
        Set<IFormOfPayment> formOfPaymentReturn = new HashSet<>();
        for (IFormOfPayment f : formOfPayments)
            formOfPaymentReturn.add(f.clone());
        return formOfPaymentReturn;
    }

    /**
     * Returns the currency of the wallet.
     *
     * @return the currency of the wallet.
     */
    @Override
    public Currency getCurrency() {
        return this.currency;
    }

    /**
     * Returns the representation of the portfolio as a Payee,
     * in order to be able to identify the source and destination wallet
     * during the transfer of values between portfolios.
     *
     * @return the representation of the portfolio as a Payee.
     */
    @Override
    public IPayee getPayeeFormat() {
        return this.payeeFormat;
    }

    /**
     * Adds a new movement to the portfolio.
     *
     * @param movement a new movement to the portfolio.
     * @throws NullArgumentException         if the argument is null.
     * @throws ExistingMovementException     if the movement already exists.
     * @throws IllegalFormOfPaymentException if the form of payment does not exist in the wallet.
     */
    @Override
    public void addMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (movements.contains(movement))
            throw new ExistingMovementException();
        if (!formOfPayments.contains(movement.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        this.movements.add(movement.clone());
    }


    /**
     * Removes a movement from the wallet.
     *
     * @param movement to be removed.
     * @throws NullArgumentException        if the argument is null.
     * @throws NonExistentMovementException if the movement does not exist in the wallet.
     */
    @Override
    public void removeMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (!this.movements.contains(movement))
            throw new NonExistentMovementException();
        this.movements.remove(movement);
    }

    /**
     * Remove an installment.
     *
     * @param installment  to be removed.
     * @param handlingMode How the removal should take place.
     * @throws NullArgumentException        if the argument is null.
     * @throws NonExistentMovementException if the movement does not exist in the wallet.
     * @throws DontIsInstallmentException   if the movement is not an installment plan.
     */
    @Override
    public void removeInstallment(IMovement installment, EHandlingMode handlingMode) {
        if (installment == null || handlingMode == null)
            throw new NullArgumentException();
        if (!this.movements.contains(installment))
            throw new NonExistentMovementException();
        if (installment.getGroupID() == installment.getID())
            throw new DontIsInstallmentException();

        if (handlingMode == EHandlingMode.JUST_THIS_ONE) {
            movements.remove(installment);
        } else {
            Predicate<IMovement> predicate = (m) -> true;
            switch (handlingMode) {
                case THIS_AND_NEXT -> predicate = (m) -> m.getDueDate().isAfter(installment.getDueDate()) || m.getID().equals(installment.getID());
                case NEXT -> predicate = (m) -> m.getDueDate().isAfter(installment.getDueDate());
                case THIS_AND_PREVIOUS -> predicate = (m) -> m.getDueDate().isBefore(installment.getDueDate()) || m.getID().equals(installment.getID());
                case PREVIOUS -> predicate = (m) -> m.getDueDate().isBefore(installment.getDueDate());
            }

            Iterator<IMovement> it = movements.iterator();
            while (it.hasNext()) {
                IMovement movement = it.next();
                if (movement.getGroupID().equals(installment.getGroupID()) && predicate.test(movement))
                    it.remove();
            }
        }
    }

    /**
     * Confirms a movement in the wallet, turning it into a transaction.
     *
     * @param movement to be confirmed.
     * @throws NullArgumentException         if the argument is null.
     * @throws NonExistentMovementException  if the movement does not exist in the wallet.
     * @throws IllegalFormOfPaymentException if the form of payment does not exist in the wallet.
     * @throws InsufficientFundsException    if the wallet does not have funds to support this transaction.
     */
    @Override
    public void confirmMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (!this.movements.contains(movement))
            throw new NonExistentMovementException();
        if (!this.formOfPayments.contains(movement.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        if (movement.getAmount().add(getBalanceInDate(movement.getDueDate())).compareTo(BigDecimal.ZERO) < 0 ||
                movement.getAmount().add(getBalance(YearMonth.from(movement.getDueDate()))).compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientFundsException();

        transactions.add(new Transaction(movement));
        if (movement.isRecurrent()) {
            IMovement recurrentMovement = movement.clone();
            recurrentMovement.updateDueDate(updateRecurrenceDate(movement.getDueDate(), movement.getRepetitionFrequency()));
            movements.add(new Movement(recurrentMovement));
        }
        movements.remove(movement);

        //Implementa a criação de transação. com factory
    }

    /**
     * Adds a new payment method to the wallet.
     *
     * @param formOfPayment new payment method to the wallet.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void addFormOfPayment(IFormOfPayment formOfPayment) {
        if (formOfPayment == null)
            throw new NullArgumentException();
        this.formOfPayments.add(formOfPayment.clone());
    }

    /**
     * Removes a form of payment from the wallet.
     *
     * @param formOfPayment to be removed.
     * @throws NullArgumentException                if the argument is null.
     * @throws ProhibitedLessFormOfPaymentException if the wallet has only one form of payment.
     */
    @Override
    public void removeFormOfPayment(IFormOfPayment formOfPayment) {
        if (formOfPayment == null)
            throw new NullArgumentException();
        if (formOfPayments.size() == 1)
            throw new ProhibitedLessFormOfPaymentException();

        this.formOfPayments.remove(formOfPayment);
    }

    /**
     * Allows you to change the name of the wallet.
     *
     * @param newName the new name of the wallet.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    @Override
    public void updateName(String newName) {
        if (newName == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(newName.trim()))
            throw new InvalidNameSizeException();
        this.name = newName.trim();
    }

    /**
     * Updates the description of the wallet.
     *
     * @param newDescription new description of the wallet.
     * @throws InvalidDescriptionSizeException if the size of the description attribute
     *                                         does not correspond to the minimum and / or maximum limits
     *                                         defined in an MINIMUM_DESCRIPTION_SIZE and MAXIMUM_DESCRIPTION_SIZE.
     * @throws NullArgumentException           if the argument is null.
     */
    @Override
    public void updateDescription(String newDescription) {
        if (newDescription == null)
            throw new NullArgumentException();
        if (INCORRECT_DESCRIPTION_SIZE.test(newDescription.trim()))
            throw new InvalidDescriptionSizeException();
        this.description = newDescription.trim();
    }

    /**
     * Change the currency of the wallet.
     *
     * @param newCurrency of the wallet.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void updateCurrency(Currency newCurrency) {
        if (newCurrency == null)
            throw new NullArgumentException();
        this.currency = newCurrency;
    }

    /**
     * Update an movement.
     *
     * @param movement to be updated.
     * @throws NullArgumentException          if the argument is null.
     * @throws NonExistentMovementException if the movement does not exist in the wallet.
     * @throws InstallmentWithoutHandlingMode if the movement is in installments.
     * @throws IllegalFormOfPaymentException if the form of payment does not exist in the wallet.
     */
    @Override
    public void updateMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (!this.movements.contains(movement))
            throw new NonExistentMovementException();
        if (movement.isInstallment())
            throw new InstallmentWithoutHandlingMode();
        if (!formOfPayments.contains(movement.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        movements.remove(movement);
        movements.add(movement.clone());
    }

    /**
     * Update an installment.
     *
     * @param installment  to be updated.
     * @param handlingMode How the update should take place.
     * @throws NullArgumentException        if the argument is null.
     * @throws NonExistentMovementException if the movement does not exist in the wallet.
     * @throws DontIsInstallmentException   if the movement is not an installment plan.
     * @throws IllegalFormOfPaymentException if the form of payment does not exist in the wallet.
     */
    @Override
    public void updateInstallment(IMovement installment, EHandlingMode handlingMode) {
        if (installment == null || handlingMode == null)
            throw new NullArgumentException();
        if (!this.movements.contains(installment))
            throw new NonExistentMovementException();
        if (!installment.isInstallment())
            throw new DontIsInstallmentException();
        if (!formOfPayments.contains(installment.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();

        if (handlingMode == EHandlingMode.JUST_THIS_ONE) {
            movements.remove(installment);
            movements.add(installment);
        } else {
            Predicate<IMovement> predicate = (m) -> true;
            switch (handlingMode) {
                case THIS_AND_NEXT -> predicate = (m) -> m.getDueDate().isAfter(installment.getDueDate()) || m.getID().equals(installment.getID());
                case NEXT -> predicate = (m) -> m.getDueDate().isAfter(installment.getDueDate());
                case THIS_AND_PREVIOUS -> predicate = (m) -> m.getDueDate().isBefore(installment.getDueDate()) || m.getID().equals(installment.getID());
                case PREVIOUS -> predicate = (m) -> m.getDueDate().isBefore(installment.getDueDate());
            }

            Iterator<IMovement> it = movements.iterator();
            while (it.hasNext()) {
                IMovement movement = it.next();
                if (movement.getGroupID().equals(installment.getGroupID()) && predicate.test(movement))
                    it.remove();
            }
        }
    }

    /**
     * Returns a collection with all the movements of the wallet.
     *
     * @return a collection with all the movements of the wallet.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<IMovement> getMovements() {
        return (Set<IMovement>) copyOperations(movements);
    }

    /**
     * Returns a collection of all transactions in the wallet.
     *
     * @return a collection of all transactions in the wallet.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<ITransaction> getTransactions() {
        return (Set<ITransaction>) copyOperations(transactions);
    }

    /**
     * Returns a collection of Operation for the current month/year.
     *
     * @return a collection of Operation for the current month.
     */
    @Override
    public Set<IOperation> getMonthOperations() {

        return getMonthOperations(YearMonth.now());
    }

    /**
     * Returns a collection of Operation from the reference.
     *
     * @param reference month / year.
     * @return a collection of Operation from the reference.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public Set<IOperation> getMonthOperations(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();

        Set<IOperation> operations = new TreeSet<>();
        Set<IOperation> allOperations = getAllOperations();

        for (IOperation op : allOperations)
            if (YearMonth.from(op.getDueDate()).equals(reference))
                operations.add(op.clone());

        return operations;
    }

    /**
     * Returns a collection of Operation for the current year.
     *
     * @return a collection of Operation for the current year.
     */
    @Override
    public Set<IOperation> getYearOperations() {
        return getYearOperations(Year.now());
    }

    /**
     * Returns a collection of Operation for a year.
     *
     * @param year for reference
     * @return a collection of Operation for a year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public Set<IOperation> getYearOperations(Year year) {
        if (year == null)
            throw new NullArgumentException();

        Set<IOperation> operations = new TreeSet<>();
        Set<IOperation> allOperations = getAllOperations();
        for (IOperation op : allOperations)
            if (Year.from(op.getDueDate()).equals(year))
                operations.add(op.clone());
        return operations;
    }

    /**
     * Returns a collection of operations that occurred in the requested range.
     *
     * @param start initial date
     * @param end   final date
     * @return a collection of operations that occurred in the requested range.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public Set<IOperation> getOperationsBetween(YearMonth start, YearMonth end) {
        if (start == null || end == null)
            throw new NullArgumentException();

        Set<IOperation> operations = new TreeSet<>();
        Set<IOperation> allOperations = getAllOperations();
        for (IOperation op : allOperations)
            if (YearMonth.from(op.getDueDate()).equals(start) ||
                    YearMonth.from(op.getDueDate()).equals(end) ||
                    (YearMonth.from(op.getDueDate()).isAfter(start) && YearMonth.from(op.getDueDate()).isBefore(end)))
                operations.add(op.clone());
        return operations;
    }

    /**
     * Returns the current balance.
     *
     * @return the current balance.
     */
    @Override
    public BigDecimal getBalance() {
        return getBalance(YearMonth.now());
    }

    /**
     * Returns the reference balance (month / year).
     *
     * @param reference month / year.
     * @return the reference balance (month / year).
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getBalance(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> YearMonth.from(t.getDueDate()).equals(reference) ||
                YearMonth.from(t.getDueDate()).isBefore(reference);
        return getCashFlow(predicate, transactions);
    }

    /**
     * Returns the total amount of credit transactions in the current month.
     *
     * @return the total amount of credit transactions in the current month.
     */
    @Override
    public BigDecimal getCashInflow() {
        return getCashInflow(YearMonth.now());
    }

    /**
     * Returns the total value of credit transactions in the month / year.
     *
     * @param reference month / year.
     * @return the total value of credit transactions in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflow(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();

        Predicate<IOperation> predicate = (t) -> YearMonth.from(t.getDueDate()).equals(reference) && t.isCredit();
        return getCashFlow(predicate, transactions);
    }

    /**
     * Returns the total amount of credit transactions in the current year.
     *
     * @return the total amount of credit transactions in the current year.
     */
    @Override
    public BigDecimal getCashInflowInYear() {
        return getCashInflowInYear(Year.now());
    }

    /**
     * Returns the total value of credit transactions in the year.
     *
     * @param year year for calculation.
     * @return the total value of credit transactions in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflowInYear(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> Year.from(t.getDueDate()).equals(year) && t.isCredit();
        return getCashFlow(predicate, transactions);
    }

    /**
     * Returns the total amount of debit transactions in the current month.
     *
     * @return the total amount of debit transactions in the current month.
     */
    @Override
    public BigDecimal getCashOutflow() {
        return getCashOutflow(YearMonth.now());
    }

    /**
     * Returns the total value of debit transactions in the month / year.
     *
     * @param reference month / year.
     * @return the total value of debit transactions in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflow(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> YearMonth.from(t.getDueDate()).equals(reference) && t.isDebit();
        return getCashFlow(predicate, transactions);
    }

    /**
     * Returns the total amount of debit transactions in the current year.
     *
     * @return the total amount of debit transactions in the current year.
     */
    @Override
    public BigDecimal getCashOutflowInYear() {
        return getCashOutflowInYear(Year.now());
    }

    /**
     * Returns the total value of debit transactions in the year.
     *
     * @param year year for calculation.
     * @return the total value of debit transactions in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflowInYear(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> Year.from(t.getDueDate()).equals(year) && t.isDebit();
        return getCashFlow(predicate, transactions);
    }

    /**
     * Returns the current balance expected (transactions + movement).
     *
     * @return the current balance expected (transactions + movement).
     */
    @Override
    public BigDecimal getBalanceExpected() {
        return getBalanceExpected(YearMonth.now());
    }

    /**
     * Returns the reference balance expected (transactions + movement).
     *
     * @param reference month / year.
     * @return the reference balance expected (transactions + movement).
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getBalanceExpected(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> YearMonth.from(t.getDueDate()).equals(reference) ||
                YearMonth.from(t.getDueDate()).isBefore(reference);
        return getCashFlow(predicate, getAllOperations());
    }

    /**
     * Returns the total amount of credit transactions and movement in the current month.
     *
     * @return the total amount of credit transactions and movement in the current month.
     */
    @Override
    public BigDecimal getCashInflowExpected() {
        return getCashInflowExpected(YearMonth.now());
    }

    /**
     * Returns the total value of credit transactions and movement in the month / year.
     *
     * @param reference month / year.
     * @return the total value of credit transactions and movement in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflowExpected(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> YearMonth.from(t.getDueDate()).equals(reference) && t.isCredit();
        return getCashFlow(predicate, getAllOperations());
    }

    /**
     * Returns the total amount of credit transactions and movement in the current year.
     *
     * @return the total amount of credit transactions and movement in the current year.
     */
    @Override
    public BigDecimal getCashInflowInYearExpected() {
        return getCashInflowInYearExpected(Year.now());
    }

    /**
     * Returns the total value of credit transactions and movement in the year.
     *
     * @param year year for calculation.
     * @return the total value of credit transactions and movement in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflowInYearExpected(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> Year.from(t.getDueDate()).equals(year) && t.isCredit();
        return getCashFlow(predicate, getAllOperations());
    }

    /**
     * Returns the total amount of debit transactions and movement in the current month.
     *
     * @return the total amount of debit transactions and movement in the current month.
     */
    @Override
    public BigDecimal getCashOutflowExpected() {
        return getCashOutflowExpected(YearMonth.now());
    }

    /**
     * Returns the total value of debit transactions and movement in the month / year.
     *
     * @param reference month / year.
     * @return the total value of debit transactions and movement in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflowExpected(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> YearMonth.from(t.getDueDate()).equals(reference) && t.isDebit();
        return getCashFlow(predicate, getAllOperations());
    }

    /**
     * Returns the total amount of debit transactions and movement in the current year.
     *
     * @return the total amount of debit transactions and movement in the current year.
     */
    @Override
    public BigDecimal getCashOutflowInYearExpected() {
        return getCashOutflowInYearExpected(Year.now());
    }

    /**
     * Returns the total value of debit transactions and movement in the year.
     *
     * @param year year for calculation.
     * @return the total value of debit transactions and movement in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflowInYearExpected(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IOperation> predicate = (t) -> Year.from(t.getDueDate()).equals(year) && t.isDebit();
        return getCashFlow(predicate, getAllOperations());
    }

    @Override
    public IWallet clone() {
        return new Wallet(this);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(IWallet o) {
        return getBalance().compareTo(o.getBalance());
    }

    private BigDecimal getCashFlow(Predicate<IOperation> predicate, Collection<? extends IOperation> operations) {
        BigDecimal cashInFlow = BigDecimal.ZERO;

        for (IOperation t : operations)
            if (predicate.test(t))
                cashInFlow = cashInFlow.add(t.getAmount());

        return cashInFlow;
    }

    private LocalDate updateRecurrenceDate(LocalDate lastDate, ERepetitionFrequency frequency) {
        LocalDate nextOccurrence = LocalDate.from(lastDate);
        switch (frequency) {
            case WEEKLY -> nextOccurrence = lastDate.plusWeeks(1);
            case YEARLY -> nextOccurrence = lastDate.plusYears(1);
            case MONTHLY -> nextOccurrence = lastDate.plusMonths(1);
            case FORTNIGHTLY -> nextOccurrence = lastDate.plusDays(15);
            case QUARTERLY -> nextOccurrence = lastDate.plusMonths(3);
        }
        return nextOccurrence;
    }

    private BigDecimal getBalanceInDate(LocalDate date) {
        BigDecimal balance = BigDecimal.ZERO;
        for (ITransaction t : transactions)
            if (t.getDueDate().isEqual(date) || t.getDueDate().isBefore(date))
                balance = balance.add(t.getAmount());

        return balance;
    }

    private Set<IOperation> getAllOperations() {
        Set<IOperation> allOperations = new TreeSet<>();
        allOperations.addAll(transactions);
        allOperations.addAll(movements);
        return allOperations;
    }

    private Set<? extends IOperation> copyOperations(Collection<? extends IOperation> source) {
        Set<IOperation> destination = new TreeSet<>();
        for (IOperation o : source)
            destination.add(o.clone());
        return destination;
    }
    private boolean existingMovement(IMovement movement){
        boolean found = false;
        IMovement currentMovement;
        Iterator<IMovement> iterator = movements.iterator();
        while (iterator.hasNext() && !found) {
            currentMovement = iterator.next();
            if (currentMovement.equals(movement)) {
                found = true;
            }
        }
        return found;
    }
    @SuppressWarnings("unused")
    private Wallet() {
    }

    @SuppressWarnings("unused")
    private void setID(UUID ID) {
        this.ID = ID;
    }

    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    private void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings("unused")
    private void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @SuppressWarnings("unused")
    private void setFormOfPayments(Set<IFormOfPayment> formOfPayments) {
        this.formOfPayments = formOfPayments;
    }

    @SuppressWarnings("unused")
    private void setMovements(Set<IMovement> movements) {
        this.movements = movements;
    }

    @SuppressWarnings("unused")
    private void setTransactions(Set<ITransaction> transactions) {
        this.transactions = transactions;
    }

}
