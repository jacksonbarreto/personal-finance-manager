package dal.infra;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static dal.infra.EntityManagerSingleton.getEntityManager;

public interface IDAO<T> {

    List<T> select(String query);

    T select(UUID id);

    void create(T element);

    void update(T element);

    void delete(T element);


    static void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction transaction = getEntityManager().getTransaction();
        try {
            transaction.begin();
            action.accept(getEntityManager());
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }
}
