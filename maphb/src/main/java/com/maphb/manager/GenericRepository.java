package com.maphb.manager;

import com.maphb.models.TableFilter;

import java.util.List;

public abstract class GenericRepository<T> implements IGenericRepository<T> {

    private EntityRepository<T> entityRepository;

    public GenericRepository(Class<T> clazz) {
        this.entityRepository = EntityRepositoryFactory.create(clazz);
    }

    @Override
    public T get(String rowkey) {
        return entityRepository.get(rowkey);
    }

    @Override
    public T put(T object) {
        return entityRepository.put(object);
    }

    @Override
    public List<T> put(List<T> listOfObjects) {
        return entityRepository.put(listOfObjects);
    }

    @Override
    public void delete(String rowkey) {
        entityRepository.delete(rowkey);
    }

    @Override
    public List<T> scan(TableFilter<T> tableFilter) {
        return entityRepository.scan(tableFilter);
    }
}
