package bll.factories;

import bll.entities.IMovement;
import bll.entities.Movement;
import bll.enumerators.EOperationType;

import java.time.LocalDate;

public interface IMovementFactory {

    static IMovement createRecurrentMovement(IMovement movement, LocalDate dueDate){
        return new Movement(
                movement.getName(),
                movement.getDescription(),
                movement.getAmount(),
                dueDate,
                movement.getFormOfPayment(),
                movement.getPayee(),
                movement.getCategory(),
                movement.getAttachments(),
                movement.isCredit() ? EOperationType.CREDIT : EOperationType.DEBIT,
                movement.getRepetitionFrequency(),
                movement.getGroupID()
        );
    }
}
