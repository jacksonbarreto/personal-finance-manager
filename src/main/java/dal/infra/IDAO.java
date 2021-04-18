package dal.infra;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IDAO<T>{

    Set<T> select(Predicate<T> predicate);
    T select(UUID id);
    void create(T element);
    void update(T element);
    void delete(T element);
}
