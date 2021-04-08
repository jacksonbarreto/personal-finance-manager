package bll.builders;

import bll.entities.*;
import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.valueObjects.IAttachment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static bll.enumerators.ERepetitionFrequency.NONE;

public class MovementBuilder implements IMovementBuilder {

    private final String name;
    private String description;
    private final BigDecimal amount;
    private final LocalDate dueDate;
    private final IFormOfPayment formOfPayment;
    private final IPayee payee;
    private final IMovementCategory category;
    private final Set<IAttachment> attachments;
    private final EOperationType movementType;
    private ERepetitionFrequency frequency;
    private UUID groupID;


    protected MovementBuilder(String name, String amount, String dueDate, IFormOfPayment formOfPayment,
                              IPayee payee, IMovementCategory category, EOperationType movementType) {
        this.name = name;
        this.amount = new BigDecimal(amount);

        this.dueDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("d-M-yyyy"));
        this.formOfPayment = formOfPayment;
        this.payee = payee;
        this.category = category;
        this.movementType = movementType;
        this.attachments = new HashSet<>();
        this.frequency = NONE;
        this.description = "";
    }

    @Override
    public IMovementBuilder addDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public IMovementBuilder addAttachments(IAttachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    @Override
    public IMovementBuilder addRepetitionFrequency(ERepetitionFrequency frequency) {
        this.frequency = frequency;
        return this;
    }

    @Override
    public IMovementBuilder addGroupID(UUID groupID) {
        this.groupID = groupID;
        return this;
    }

    @Override
    public IMovement build() {
        return new Movement(this.name, this.description, this.amount, this.dueDate, this.formOfPayment,
                this.payee, this.category, this.attachments, this.movementType, this.frequency, this.groupID);
    }
}
