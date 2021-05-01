package dal.infra;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static javax.persistence.Persistence.createEntityManagerFactory;

public class EntityManagerSingleton {
    private final EntityManagerFactory factory;
    private final EntityManager entityManager;
    private static EntityManagerSingleton INSTANCE;

    private EntityManagerSingleton() {
        this.factory = createEntityManagerFactory("PFM-PU");
        this.entityManager = this.factory.createEntityManager();
    }

    private void close() {
        this.entityManager.close();
        this.factory.close();
    }

    public static EntityManager getEntityManager() {
        return getInstance().entityManager;
    }

    public static void closingEntityManager() {
        getInstance().close();
        INSTANCE = null;
    }

    private static EntityManagerSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (EntityManagerSingleton.class) {
                if (INSTANCE == null)
                    INSTANCE = new EntityManagerSingleton();
            }
        }
        return INSTANCE;
    }
}
