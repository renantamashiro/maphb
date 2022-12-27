package com.maphb.manager;

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
    public void put(T object) {
        entityRepository.put(object);
    }

    @Override
    public void put(List<T> listOfObjects) {
        entityRepository.put(listOfObjects);
    }

    @Override
    public void delete(String rowkey) {
        entityRepository.delete(rowkey);
    }

    @Override
    public List<T> scan() {
        return null;
    }
}
