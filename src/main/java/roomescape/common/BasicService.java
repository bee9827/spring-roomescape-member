package roomescape.common;

import java.util.List;

public interface  BasicService<T,ID>  {
    public T save(T entity);
    public void delete(T entity);
    public List<T> findAll();
    public T findById(ID id);
}
