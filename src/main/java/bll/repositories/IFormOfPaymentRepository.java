package bll.repositories;

import bll.entities.IFormOfPayment;

import java.util.Set;

public interface IFormOfPaymentRepository extends IRepository<IFormOfPayment> {

    /**
     * Returns all movement categories, that is, public and user categories.
     *
     * @return all movement categories, that is, public and user categories.
     */
    Set<IFormOfPayment> getAll();
}
