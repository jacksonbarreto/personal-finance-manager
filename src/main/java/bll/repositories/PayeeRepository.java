package bll.repositories;

import bll.entities.IPayee;
import bll.services.SessionService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class PayeeRepository implements IRepository<IPayee> {
    @Override
    public Set<IPayee> get(Predicate<IPayee> predicate) {
        Set<IPayee> payees = new HashSet<>();
        for (IPayee payee : SessionService.getCurrentUser().getPayee())
            if (predicate.test(payee))
                payees.add(payee);
        return payees;
    }

    @Override
    public IPayee get(UUID id) {
        for (IPayee payee : SessionService.getCurrentUser().getPayee())
            if (payee.getID().equals(id))
                return payee;
        return null;
    }

    @Override
    public void add(IPayee element) {
        SessionService.getCurrentUser().addPayee(element);
        UserRepository.getInstance().update(SessionService.getCurrentUser());
    }

    @Override
    public void update(IPayee element) {
        SessionService.getCurrentUser().updatePayee(element);
        UserRepository.getInstance().update(SessionService.getCurrentUser());
    }

    @Override
    public void remove(IPayee element) {
        SessionService.getCurrentUser().removePayee(element);
        UserRepository.getInstance().update(SessionService.getCurrentUser());
    }
}
