package bll.builders;

import bll.entities.IFormOfPayment;
import bll.entities.IMovement;
import bll.entities.IMovementCategory;
import bll.entities.IPayee;
import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.valueObjects.IAttachment;

import java.time.LocalDate;
import java.util.UUID;

public interface IMovementBuilder {


    static IMovementBuilder makeMovement(String name, String amount, String dueDate, IFormOfPayment formOfPayment,
                                         IPayee payee, IMovementCategory category, EOperationType movementType) {
        return new MovementBuilder(name, amount, dueDate, formOfPayment, payee, category, movementType);
    }

    static IMovementBuilder makeMovement(String name, String amount, LocalDate dueDate, IFormOfPayment formOfPayment,
                                         IPayee payee, IMovementCategory category, EOperationType movementType) {
        return new MovementBuilder(name, amount, dueDate.getDayOfMonth() + "-" + dueDate.getMonthValue() + "-" + dueDate.getYear(), formOfPayment, payee, category, movementType);
    }

    IMovementBuilder addDescription(String description);

    IMovementBuilder addAttachments(IAttachment attachment);

    IMovementBuilder addRepetitionFrequency(ERepetitionFrequency frequency);

    IMovementBuilder addGroupID(UUID groupID);

    IMovement build();
}
