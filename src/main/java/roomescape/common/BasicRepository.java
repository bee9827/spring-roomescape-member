package roomescape.common;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface BasicRepository<T> {
    Collection<T> findAll();
    T findById(Long id);
    Long save(T t);
    void deleteById(Long id);
}
