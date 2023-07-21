package com.maphb;

import com.maphb.connection.MapHBConnectionFactory;
import com.maphb.manager.DataStoreContext;
import com.maphb.manager.DataStoreContextInitiator;

public final class MapHBContext {

    public static DataStoreContext startApplication(Class<?> applicationContext) {
        MapHBConnectionFactory.getMapHBConnection().setConnection();
        return DataStoreContextInitiator.create(applicationContext, MapHBConnectionFactory.getMapHBConnection().getConnection());
    }
}
