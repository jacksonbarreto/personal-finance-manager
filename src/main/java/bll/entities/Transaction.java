package bll.entities;

import bll.exceptions.NullArgumentException;
import bll.valueObjects.IAttachment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

final public class Transaction extends Operation implements ITransaction {


    public Transaction(String name, String description, BigDecimal amount, LocalDate dueDate,
                       IFormOfPayment formOfPayment, IPayee payee, ITransactionCategory category,
                       Set<IAttachment> attachments) {
        super(name, description, amount, dueDate, formOfPayment, payee, category, attachments);

    }

    public Transaction(String name, BigDecimal amount, LocalDate dueDate, IFormOfPayment formOfPayment, IPayee payee,
                       ITransactionCategory category, Set<IAttachment> attachments) {
        super(name, EMPTY_DESCRIPTION, amount, dueDate, formOfPayment, payee, category, attachments);
    }

    public Transaction(String name, String description, BigDecimal amount, LocalDate dueDate,
                       IFormOfPayment formOfPayment, IPayee payee, ITransactionCategory category) {
        super(name, description, amount, dueDate, formOfPayment, payee, category, new HashSet<>());
    }

    public Transaction(String name, BigDecimal amount, LocalDate dueDate, IFormOfPayment formOfPayment, IPayee payee,
                       ITransactionCategory category) {
        super(name, EMPTY_DESCRIPTION, amount, dueDate, formOfPayment, payee, category, new HashSet<>());
    }

    public Transaction(IOperation transaction) {
        if (transaction == null)
            throw new NullArgumentException();

        this.name = transaction.getName();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.dueDate = transaction.getDueDate();
        this.formOfPayment = transaction.getFormOfPayment();
        this.payee = transaction.getPayee();
        this.category = transaction.getCategory();
        this.ID = transaction.getID();
        this.attachments = new HashSet<>();
        this.attachments.addAll(transaction.getAttachments());
    }

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    @Override
    public ITransaction clone() {
        return new Transaction(this);
    }


    @SuppressWarnings("unused")
    private Transaction() {
        super();
    }
}
