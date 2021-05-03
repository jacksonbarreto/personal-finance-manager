package bll.entities;

import bll.factories.IMovementFactory;
import bll.enumerators.EHandlingMode;
import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.*;
import bll.valueObjects.IAttachment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Predicate;

@Entity
public class Wallet implements IWallet {
    @Id
    private UUID ID;
    @Column(nullable = false, length = MAXIMUM_NAME_SIZE)
    private String name;
    @Column(nullable = false, length = MAXIMUM_DESCRIPTION_SIZE)
    private String description;
    @Column(nullable = false)
    private Currency currency;
    @Column(nullable = false)
    @ManyToMany(targetEntity = FormOfPayment.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<IFormOfPayment> formOfPayments;
    @OneToMany(targetEntity = Movement.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet")
    private Set<IMovement> movements;
    @JoinColumn(nullable = false)
    @OneToOne(targetEntity = Payee.class, cascade = CascadeType.ALL, orphanRemoval = true)
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
        this.movements = new HashSet<>();
        this.payeeFormat = payeeFormat.clone();
    }

    public Wallet(String name, IFormOfPayment formOfPayment, IPayee payeeFormat) {
        this(name, EMPTY_DESCRIPTION, Currency.getInstance(Locale.getDefault()),
                new HashSet<>(Collections.singletonList(formOfPayment)), payeeFormat);
    }

    private Wallet(IWallet wallet) {
        if (wallet == null)
            throw new NullArgumentException();
        this.ID = wallet.getID();
        this.name = wallet.getName();
        this.description = wallet.getDescription();
        this.currency = wallet.getCurrency();
        this.formOfPayments = new HashSet<>();
        this.formOfPayments.addAll(wallet.getFormOfPayment());
        this.movements = copyMovements(wallet.getMovements());
        this.payeeFormat = wallet.getPayeeFormat();
    }


    @Override
    public void autoUpdate(IWallet externalCopy) {
        if (externalCopy == null)
            throw new NullArgumentException();
        if (this.ID.equals(externalCopy.getID())) {
            this.updateName(externalCopy.getName());
            this.description = externalCopy.getDescription();
            this.currency = externalCopy.getCurrency();
            for (IFormOfPayment f : this.formOfPayments) {
                for (IFormOfPayment fExternal : externalCopy.getFormOfPayment()) {
                    if (fExternal.equals(f)) {
                        f.autoUpdate(fExternal);
                        break;
                    }
                }
            }
            for (IFormOfPayment f : externalCopy.getFormOfPayment()) {
                if (!this.formOfPayments.contains(f)) {
                    this.formOfPayments.add(f.clone());
                }
            }
            this.formOfPayments.retainAll(externalCopy.getFormOfPayment());

            for (IMovement m : externalCopy.getMovements())
                this.movements.add(m.clone());

            for (IMovement m : this.movements) {
                for (IMovement mExternal : externalCopy.getMovements()) {
                    if (m.equals(mExternal)) {
                        m.autoUpdate(mExternal);
                        break;
                    }
                }
            }

            this.movements = copyMovements(externalCopy.getMovements());
        }
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
        return this.payeeFormat.clone();
    }

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
    @Override
    public void addMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (movement.isInactive())
            throw new InactiveMovementException();
        if (movement.isInstallment())
            throw new InstallmentForbiddenException();
        if (movement.isAccomplished())
            throw new MovementAlreadyAccomplishException();
        if (!formOfPayments.contains(movement.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        if (this.movements.contains(movement))
            if (fetchMovement(movement).isInactive())
                throw new AttemptedToUseExcludedMovementException();
            else
                throw new ExistingMovementException();
        this.movements.add(movement.clone());
    }

    /**
     * Creates all the installment movements.
     *
     * @param movement             basic (initial) movement.
     * @param frequency            repetition
     * @param numberOfInstallments number of installment.
     * @throws NullArgumentException                   if the argument is null.
     * @throws ExistingMovementException               if the movement already exists.
     * @throws DontIsInstallmentException              if the movement is not an installment plan.
     * @throws IllegalInstallmentQuantityException     if the number of plots is less than 2.
     * @throws MovementAlreadyAccomplishException      If the movement is already accomplished.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    @Override
    public void addInstallment(IMovement movement, ERepetitionFrequency frequency, int numberOfInstallments) {
        if (movement == null || frequency == null)
            throw new NullArgumentException();
        if (movement.isInactive())
            throw new InactiveMovementException();
        if (movements.contains(movement))
            if (fetchMovement(movement).isInactive())
                throw new AttemptedToUseExcludedMovementException();
            else
                throw new ExistingMovementException();
        if (movement.isAccomplished())
            throw new MovementAlreadyAccomplishException();
        if (!movement.isInstallment())
            throw new DontIsInstallmentException();
        if (numberOfInstallments < 2)
            throw new IllegalInstallmentQuantityException();

        movements.add(movement.clone());
        LocalDate nextDate = updateRecurrenceDate(movement.getDueDate(), frequency);
        for (int i = 0; i < numberOfInstallments - 1; i++) {
            movements.add(new Movement(movement.getName(), movement.getDescription(), movement.getAmount(),
                    nextDate, movement.getFormOfPayment(), movement.getPayee(), movement.getCategory(),
                    movement.getAttachments(), movement.isCredit() ? EOperationType.CREDIT : EOperationType.DEBIT,
                    ERepetitionFrequency.NONE, movement.getGroupID()));
            nextDate = updateRecurrenceDate(nextDate, frequency);
        }
    }

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
    @Override
    public void removeMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (movement.isInactive())
            throw new InactiveMovementException();
        if (this.movements.contains(movement) && fetchMovement(movement).isInactive())
            throw new AttemptedToUseExcludedMovementException();
        if (!this.movements.contains(movement))
            throw new NonExistentMovementException();
        if (movement.isAccomplished() || fetchMovement(movement).isAccomplished())
            throw new MovementAlreadyAccomplishException();
        if (movement.isInstallment())
            throw new InstallmentWithoutHandlingMode();
        fetchMovement(movement).inactivate();
    }

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
    @Override
    public void removeInstallment(IMovement installment, EHandlingMode handlingMode) {
        if (installment == null || handlingMode == null)
            throw new NullArgumentException();
        if (installment.isInactive())
            throw new InactiveMovementException();
        if (!installment.isInstallment())
            throw new DontIsInstallmentException();
        if (this.movements.contains(installment) && fetchMovement(installment).isInactive())
            throw new AttemptedToUseExcludedMovementException();
        if (!this.movements.contains(installment))
            throw new NonExistentMovementException();
        if (installment.isAccomplished())
            throw new MovementAlreadyAccomplishException();
        updateOrDeleteInstallment(installment, handlingMode, Action.REMOVE);
    }

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
    @Override
    public void confirmMovement(IMovement movement) {
        confirmMovement(movement, LocalDate.now());
    }

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
    @Override
    public void confirmMovement(IMovement movement, LocalDate accomplishDate) {
        if (movement == null)
            throw new NullArgumentException();
        if (movement.isInactive())
            throw new InactiveMovementException();
        if (!this.movements.contains(movement))
            throw new NonExistentMovementException();
        IMovement originalMovement = fetchMovement(movement);
        if (originalMovement.isInactive())
            throw new AttemptedToUseExcludedMovementException();
        if (!this.formOfPayments.contains(movement.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        if (movement.getAmount().add(getBalanceInDate(accomplishDate)).compareTo(BigDecimal.ZERO) < 0 ||
                movement.getAmount().add(getBalance(YearMonth.from(accomplishDate))).compareTo(BigDecimal.ZERO) < 0)
            throw new InsufficientFundsException();


        if (movement.isAccomplished() || originalMovement.isAccomplished())
            throw new MovementAlreadyAccomplishException();

        synchronizeMovement(movement, originalMovement);

        if (movement.isRecurrent()) {
            LocalDate nextDate = updateRecurrenceDate(originalMovement.getDueDate(), originalMovement.getRepetitionFrequency());
            IMovement recurrentMovement = IMovementFactory.createRecurrentMovement(originalMovement, nextDate);
            movements.add(recurrentMovement);
        }
        originalMovement.accomplish();
    }


    /**
     * Adds a new payment method to the wallet.
     *
     * @param formOfPayment new payment method to the wallet.
     * @throws NullArgumentException          if the argument is null.
     * @throws ExistingFormOfPaymentException if the payment method already existing.
     */
    @Override
    public void addFormOfPayment(IFormOfPayment formOfPayment) {
        if (formOfPayment == null)
            throw new NullArgumentException();
        if (this.formOfPayments.contains(formOfPayment) ||
                this.formOfPayments.stream().anyMatch(f -> f.getName().equalsIgnoreCase(formOfPayment.getName())))
            throw new ExistingFormOfPaymentException();
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
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws InstallmentWithoutHandlingMode          if the movement is in installments.
     * @throws IllegalFormOfPaymentException           if the form of payment does not exist in the wallet.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    @Override
    public void updateMovement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();
        if (movement.isInactive())
            throw new InactiveMovementException();
        if (movement.isInstallment())
            throw new InstallmentWithoutHandlingMode();
        if (!formOfPayments.contains(movement.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        if (!this.movements.contains(movement))
            throw new NonExistentMovementException();

        IMovement originalMovement = fetchMovement(movement);

        if (originalMovement.isInactive())
            throw new AttemptedToUseExcludedMovementException();

        synchronizeMovement(movement, originalMovement);
    }

    /**
     * Update an installment.
     *
     * @param installment  to be updated.
     * @param handlingMode How the update should take place.
     * @throws NullArgumentException                   if the argument is null.
     * @throws NonExistentMovementException            if the movement does not exist in the wallet.
     * @throws DontIsInstallmentException              if the movement is not an installment plan.
     * @throws IllegalFormOfPaymentException           if the form of payment does not exist in the wallet.
     * @throws InactiveMovementException               if you try to use an inactive movement as a parameter.
     * @throws AttemptedToUseExcludedMovementException if you try to use an already excluded movement as a parameter.
     */
    @Override
    public void updateInstallment(IMovement installment, EHandlingMode handlingMode) {
        if (installment == null || handlingMode == null)
            throw new NullArgumentException();
        if (installment.isInactive())
            throw new InactiveMovementException();
        if (!this.movements.contains(installment))
            throw new NonExistentMovementException();
        if (!installment.isInstallment())
            throw new DontIsInstallmentException();
        if (!formOfPayments.contains(installment.getFormOfPayment()))
            throw new IllegalFormOfPaymentException();
        if (fetchMovement(installment).isInactive())
            throw new AttemptedToUseExcludedMovementException();
        updateOrDeleteInstallment(installment, handlingMode, Action.UPDATE);
    }

    /**
     * Returns a collection with all the movements of the wallet.
     *
     * @return a collection with all the movements of the wallet.
     */
    @Override
    public Set<IMovement> getMovements() {
        Set<IMovement> movementsReturn = new TreeSet<>();
        for (IMovement m : this.movements)
            if (m.isActive())
                movementsReturn.add(m.clone());
        return movementsReturn;
    }

    /**
     * Returns a collection of all transactions in the wallet.
     *
     * @return a collection of all transactions in the wallet.
     */
    @Override
    public Set<IMovement> getTransactions() {
        Set<IMovement> transaction = new TreeSet<>();
        for (IMovement m : this.movements)
            if (m.isAccomplished())
                transaction.add(m.clone());
        return transaction;
    }

    /**
     * Returns a collection of Operation for the current month/year.
     *
     * @return a collection of Operation for the current month.
     */
    @Override
    public Set<IMovement> getMonthOperations() {

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
    public Set<IMovement> getMonthOperations(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();

        Set<IMovement> operations = new TreeSet<>();

        for (IMovement op : this.movements)
            if (op.isActive() && YearMonth.from(op.getDueDate()).equals(reference))
                operations.add(op.clone());

        return operations;
    }

    /**
     * Returns a collection of Operation for the current year.
     *
     * @return a collection of Operation for the current year.
     */
    @Override
    public Set<IMovement> getYearOperations() {
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
    public Set<IMovement> getYearOperations(Year year) {
        if (year == null)
            throw new NullArgumentException();

        Set<IMovement> operations = new TreeSet<>();
        for (IMovement op : this.movements)
            if (op.isActive() && Year.from(op.getDueDate()).equals(year))
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
    public Set<IMovement> getOperationsBetween(YearMonth start, YearMonth end) {
        if (start == null || end == null)
            throw new NullArgumentException();

        YearMonth yearMonthTemp;
        if (start.isAfter(end)) {
            yearMonthTemp = end;
            end = start;
            start = yearMonthTemp;
        }

        Set<IMovement> operations = new TreeSet<>();
        for (IMovement op : this.movements)
            if (op.isActive() && (YearMonth.from(op.getDueDate()).equals(start) ||
                    YearMonth.from(op.getDueDate()).equals(end) ||
                    (YearMonth.from(op.getDueDate()).isAfter(start) && YearMonth.from(op.getDueDate()).isBefore(end))))
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
        Predicate<IMovement> predicate = (t) -> t.isAccomplished() &&
                (t.isActive() && (YearMonth.from(t.getAccomplishDate()).equals(reference) ||
                        YearMonth.from(t.getAccomplishDate()).isBefore(reference)));
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of credit transactions, filtered by their date of accomplish, in the current month.
     *
     * @return the total amount of credit transactions in the current month.
     */
    @Override
    public BigDecimal getCashInflow() {
        return getCashInflow(YearMonth.now());
    }

    /**
     * Returns the total value of credit transactions, filtered by their date of accomplish, in the month / year.
     *
     * @param reference month / year.
     * @return the total value of credit transactions in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflow(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();

        Predicate<IMovement> predicate = (t) -> t.isActive() && t.isAccomplished() &&
                YearMonth.from(t.getAccomplishDate()).equals(reference) &&
                t.isCredit();
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of credit transactions, filtered by their date of accomplish, in the current year.
     *
     * @return the total amount of credit transactions, filtered by their date of accomplish, in the current year.
     */
    @Override
    public BigDecimal getCashInflowInYear() {
        return getCashInflowInYear(Year.now());
    }

    /**
     * Returns the total value of credit transactions, filtered by their date of accomplish, in the year.
     *
     * @param year year for calculation.
     * @return the total value of credit transactions, filtered by their date of accomplish, in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflowInYear(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() && t.isAccomplished() &&
                Year.from(t.getAccomplishDate()).equals(year) &&
                t.isCredit();
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of debit transactions, filtered by their date of accomplish, in the current month.
     *
     * @return the total amount of debit transactions, filtered by their date of accomplish, in the current month.
     */
    @Override
    public BigDecimal getCashOutflow() {
        return getCashOutflow(YearMonth.now());
    }

    /**
     * Returns the total value of debit transactions, filtered by their date of accomplish, in the month / year.
     *
     * @param reference month / year.
     * @return the total value of debit transactions, filtered by their date of accomplish, in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflow(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() && t.isAccomplished() &&
                YearMonth.from(t.getAccomplishDate()).equals(reference) &&
                t.isDebit();
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of debit transactions, filtered by their date of accomplish, in the current year.
     *
     * @return the total amount of debit transactions, filtered by their date of accomplish, in the current year.
     */
    @Override
    public BigDecimal getCashOutflowInYear() {
        return getCashOutflowInYear(Year.now());
    }

    /**
     * Returns the total value of debit transactions, filtered by their date of accomplish, in the year.
     *
     * @param year year for calculation.
     * @return the total value of debit transactions, filtered by their date of accomplish, in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflowInYear(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() && t.isAccomplished() &&
                Year.from(t.getAccomplishDate()).equals(year) &&
                t.isDebit();
        return getCashFlow(predicate);
    }

    /**
     * Returns the current balance expected only movement, filtered by their due date.
     *
     * @return the current balance expected only movement, filtered by their due date.
     */
    @Override
    public BigDecimal getBalanceExpected() {
        return getBalanceExpected(YearMonth.now());
    }

    /**
     * Returns the reference balance expected only movement, filtered by their due date.
     *
     * @param reference month / year.
     * @return the reference balance expected only movement, filtered by their due date.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getBalanceExpected(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() &&
                ((YearMonth.from(t.getDueDate()).equals(reference) ||
                        YearMonth.from(t.getDueDate()).isBefore(reference)));
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of credit movement, filtered by their due date, in the current month.
     *
     * @return the total amount of credit transactions and movement in the current month.
     */
    @Override
    public BigDecimal getCashInflowExpected() {
        return getCashInflowExpected(YearMonth.now());
    }

    /**
     * Returns the total value of credit movement, filtered by their due date,  in the month / year.
     *
     * @param reference month / year.
     * @return the total value of credit transactions and movement in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflowExpected(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() &&
                (YearMonth.from(t.getDueDate()).equals(reference) && t.isCredit());
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of credit movement, filtered by their due date, in the current year.
     *
     * @return the total amount of credit movement, filtered by their due date, in the current year.
     */
    @Override
    public BigDecimal getCashInflowInYearExpected() {
        return getCashInflowInYearExpected(Year.now());
    }

    /**
     * Returns the total value of credit movement, filtered by their due date, in the year.
     *
     * @param year year for calculation.
     * @return the total value of credit transactions and movement in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashInflowInYearExpected(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() &&
                (Year.from(t.getDueDate()).equals(year) && t.isCredit());
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of debit movement, filtered by their due date, in the current month.
     *
     * @return the total amount of debit movement, filtered by their due date, in the current month.
     */
    @Override
    public BigDecimal getCashOutflowExpected() {
        return getCashOutflowExpected(YearMonth.now());
    }

    /**
     * Returns the total value of debit movement, filtered by their due date, the month / year.
     *
     * @param reference month / year.
     * @return the total value of debit movement, filtered by their due date, in the month / year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflowExpected(YearMonth reference) {
        if (reference == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() &&
                (YearMonth.from(t.getDueDate()).equals(reference) && t.isDebit());
        return getCashFlow(predicate);
    }

    /**
     * Returns the total amount of debit movement, filtered by their due date, in the current year.
     *
     * @return the total amount of debit movement, filtered by their due date, in the current year.
     */
    @Override
    public BigDecimal getCashOutflowInYearExpected() {
        return getCashOutflowInYearExpected(Year.now());
    }

    /**
     * Returns the total value of debit movement, filtered by their due date, in the year.
     *
     * @param year year for calculation.
     * @return the total value of debit movement, filtered by their due date, in the year.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public BigDecimal getCashOutflowInYearExpected(Year year) {
        if (year == null)
            throw new NullArgumentException();
        Predicate<IMovement> predicate = (t) -> t.isActive() &&
                (Year.from(t.getDueDate()).equals(year) && t.isDebit());
        return getCashFlow(predicate);
    }

    @Override
    public IWallet clone() {
        return new Wallet(this);
    }

    @Override
    public boolean isDeepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return ID.equals(wallet.ID) && name.equals(wallet.name) && description.equals(wallet.description) &&
                currency.equals(wallet.currency) && formOfPayments.equals(wallet.formOfPayments) &&
                movements.equals(wallet.movements) && payeeFormat.equals(wallet.payeeFormat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return ID.equals(wallet.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
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
        return this.getBalance().compareTo(o.getBalance());
    }

    private BigDecimal getCashFlow(Predicate<IMovement> predicate) {
        BigDecimal cashInFlow = BigDecimal.ZERO;

        for (IMovement t : this.movements)
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
        for (IMovement t : this.movements)
            if (t.isActive() && t.isAccomplished() && (t.getDueDate().isEqual(date) || t.getDueDate().isBefore(date)))
                balance = balance.add(t.getAmount());

        return balance;
    }


    private Set<IMovement> copyMovements(Collection<IMovement> source) {
        Set<IMovement> destination = new TreeSet<>();
        for (IMovement m : source)
            destination.add(m.clone());
        return destination;
    }

    private enum Action {
        UPDATE, REMOVE
    }

    private void updateOrDeleteInstallment(IMovement installment, EHandlingMode handlingMode, Action action) {

        IMovement originalInstallment = fetchMovement(installment);

        if (handlingMode == EHandlingMode.JUST_THIS_ONE) {
            if (fetchMovement(installment).isAccomplished())
                throw new MovementAlreadyAccomplishException();
            if (action == Action.UPDATE)
                synchronizeMovement(installment, originalInstallment);
            else
                originalInstallment.inactivate();
        } else {
            Predicate<IMovement> handlingModePredicate = (m) -> true;
            switch (handlingMode) {
                case THIS_AND_NEXT -> handlingModePredicate = (m) -> m.getDueDate().isAfter(installment.getDueDate()) || m.getID().equals(installment.getID());
                case NEXT -> handlingModePredicate = (m) -> m.getDueDate().isAfter(installment.getDueDate());
                case THIS_AND_PREVIOUS -> handlingModePredicate = (m) -> m.getDueDate().isBefore(installment.getDueDate()) || m.getID().equals(installment.getID());
                case PREVIOUS -> handlingModePredicate = (m) -> m.getDueDate().isBefore(installment.getDueDate());
            }

            for (IMovement m : movements) {
                if (!m.isAccomplished() && m.getGroupID().equals(installment.getGroupID()) && handlingModePredicate.test(m)) {
                    if (action == Action.REMOVE)
                        m.inactivate();
                    else
                        synchronizeMovement(installment, m);
                }
            }
        }
    }

    private void synchronizeMovement(IMovement source, IMovement destiny) {
        destiny.updateName(source.getName());
        destiny.updateMovementType(source.isCredit() ? EOperationType.CREDIT : EOperationType.DEBIT);
        destiny.updateFormOfPayment(source.getFormOfPayment());
        destiny.updateAmount(source.getAmount());
        destiny.updatePayee(source.getPayee());
        destiny.updateCategory(source.getCategory());
        destiny.updateDescription(source.getDescription());
        for (IAttachment a : source.getAttachments())
            destiny.addAttachment(a);

    }

    private IMovement fetchMovement(IMovement movement) {
        IMovement foundMovement = null;
        for (IMovement m : this.movements)
            if (m.equals(movement)) {
                foundMovement = m;
                break;
            }
        return foundMovement;
    }

    protected Wallet() {
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

}
