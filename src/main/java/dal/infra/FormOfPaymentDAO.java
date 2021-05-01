package dal.infra;

import bll.entities.FormOfPayment;
import bll.entities.IFormOfPayment;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dal.infra.EntityManagerSingleton.getEntityManager;
import static dal.infra.IDAO.executeInsideTransaction;

public class FormOfPaymentDAO implements IDAO<IFormOfPayment> {
    @Override
    public List<IFormOfPayment> select(String query) {
        List<IFormOfPayment> formOfPayments;
        TypedQuery<FormOfPayment> typedQuery = getEntityManager().createQuery(query, FormOfPayment.class);
        formOfPayments = new ArrayList<>(typedQuery.getResultList());
        return formOfPayments;
    }

    @Override
    public IFormOfPayment select(UUID id) {
        return getEntityManager().find(FormOfPayment.class, id);
    }

    @Override
    public void create(IFormOfPayment element) {
        executeInsideTransaction(entityManager -> entityManager.persist(element));
    }

    @Override
    public void update(IFormOfPayment element) {
        executeInsideTransaction(entityManager -> entityManager.merge(element));
    }

    @Override
    public void delete(IFormOfPayment element) {
        executeInsideTransaction(entityManager -> entityManager.remove(element));
    }

    private FormOfPaymentDAO() {
    }

    public static IDAO<IFormOfPayment> defaultFormOfPaymentDAO() {
        return new FormOfPaymentDAO();
    }
}
