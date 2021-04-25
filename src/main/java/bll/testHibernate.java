package bll;

import bll.entities.*;
import bll.enumerators.EUserState;
import bll.repositories.UserRepository;
import bll.services.AuthenticationService;
import bll.services.IdentificationService;
import bll.services.SessionService;
import bll.valueObjects.Email;
import bll.valueObjects.IEmail;
import dal.infra.UserDAO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static bll.builders.IMovementBuilder.makeMovement;
import static bll.enumerators.EOperationType.CREDIT;
import static bll.enumerators.EOperationType.DEBIT;
import static bll.enumerators.ERole.PREMIUM;
import static bll.enumerators.ERole.SIMPLE;
import static bll.enumerators.EUserState.*;
import static bll.enumerators.EUserState.ACTIVE;

public class testHibernate {
    public static void main(String[] args) {

        IUser user = IdentificationService.identificationServiceDefault().identifyUser("token do  user1");

        System.out.println(user);
        Predicate<IUser> predicate = u -> u.getCredential().getAccessKeys().contains("token do user1");

        System.out.println(AuthenticationService.authenticationServiceDefault().authenticate("token do user1", "password"));;
        System.out.println(SessionService.getCurrentUser());
/*
EntityManagerFactory entityManagerFactory
                = Persistence.createEntityManagerFactory("PFM-PU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        IEmail email1 = new Email("localPart@domain.com");
        ICredential credential1 = new Credential("token do user1", "password");
        IEmail email2 = new Email("meuMail@superdomain.com");
        String name = "Jackson Jr";
        IUser user = new User(name, credential1,
                Arrays.asList(ACTIVE, INVALID_LOGIN_ATTEMPT, INVALID_LOGIN_ATTEMPT),
                Collections.singletonList(SIMPLE),
                email1);
        IWallet wallet = new Wallet("Wallet", new FormOfPayment("MB Way"), new Payee("Wallet"));
        user.addWallet(wallet);
        user.addRole(PREMIUM);
        IPayee p1 = new Payee("Continent");
        user.addPayee(p1);
        IPayee p2 = new Payee("Millennium", true);
        user.addPayee(p2);

        ICredential credential2 = new Credential("usernameDo User2", "senhaSecreta**33");
        IUser user2 = new User("João das Coves", credential2,
                Arrays.asList(ACTIVE, INVALID_LOGIN_ATTEMPT, INVALID_LOGIN_ATTEMPT),
                Collections.singletonList(SIMPLE),
                email2);
        entityManager.persist(user2);

        IMovementCategory t1 = new MovementCategory("Education");
        IMovementCategory t2 = new MovementCategory("Hospital");
        user.addCategory(t1);
        user.addCategory(t2);

        entityManager.persist(user);

        try {
            URI uri1 = new URI("/rest.png");
            IMovementCategory t3 = new MovementCategory("Food", uri1);

            entityManager.persist(t3);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        user.removeCategory(t2);

        transaction.commit();
        transaction = null;


        entityManager.close();
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
        transaction.begin();

        IPayee payee = new Payee("Worten");
        IFormOfPayment formOfPayment = new FormOfPayment("Card");
        IMovementCategory category1 = new MovementCategory("Mercado");

        wallet.addFormOfPayment(formOfPayment);
        entityManager.persist(payee);
        entityManager.persist(category1);

        IMovement mov1 = makeMovement("Christmas shopping", "33.50",
                LocalDate.now(), formOfPayment, payee, category1, CREDIT).build();

        IMovement mov2 = makeMovement("English course", "22.30",
                LocalDate.of(1970, Month.JANUARY, 1), formOfPayment, payee, t1, DEBIT).build();

        wallet.addMovement(mov1);
        wallet.addMovement(mov2);

        user.updateWallet(wallet);

        user.getCredential().addAccessKey("Novo963992305 do user1");

        entityManager.merge(user);

transaction.commit();

        entityManager.close();
        entityManagerFactory.close();
 */

    }
}
