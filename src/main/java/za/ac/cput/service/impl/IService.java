package za.ac.cput.service.impl;

import java.util.List;

public interface IService<T, ID> {
    T create(T t);
    T read(ID id);
    T update(T t);
    void delete(ID id);
    List<T> getAll();
}
