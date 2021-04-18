package bll.repositories;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IRepository<T> {

    Set<T> get(Predicate<T> predicate);

    T get(UUID id);

    void add(T element);

    void update(T element);

    void remove(T element);
}
