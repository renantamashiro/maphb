package com.maphb.manager;

import com.maphb.connection.MapHBConnectionFactory;

/**
 * Factory class for creating instances of EntityRepository.
 */
public final class EntityRepositoryFactory {
    /**
     * Creates an instance of EntityRepository for the given class.
     *
     * @param clazz the class for which the EntityRepository is created
     * @return the created EntityRepository instance
     */
    public static EntityRepository create(Class<?> clazz) {
        return new EntityRepositoryImpl<>(MapHBConnectionFactory.getMapHBConnection().getConnection(), clazz);
    }
}
