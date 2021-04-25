package dal.infra;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static javax.persistence.Persistence.createEntityManagerFactory;

public interface IDAO<T>{

    List<T> selectAll();
    T select(UUID id);
    void create(T element);
    void update(T element);
    void delete(T element);

    static EntityManager getEntityManager() {
        EntityManagerFactory factory = null;
        EntityManager entityManager = null;
        try {
            factory = createEntityManagerFactory("PFM-PU");
            entityManager = factory.createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityManager;
    }

    static void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
