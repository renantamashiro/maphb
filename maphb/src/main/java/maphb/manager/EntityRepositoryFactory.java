package maphb.manager;

import maphb.connection.MapHBConnectionFactory;

public final class EntityRepositoryFactory {
    public static EntityRepository create(Class<?> clazz) {
        return new EntityRepositoryImpl<>(MapHBConnectionFactory.getMapHBConnection().getConnection(), clazz);
    }
}
