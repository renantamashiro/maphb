package com.maphb;

import com.maphb.connection.MapHBConnectionFactory;
import com.maphb.manager.DataStoreContextInitiator;

public final class MapHBContext {

    public static void startApplication(Class<?> applicationContext) {
        MapHBConnectionFactory.getMapHBConnection().setConnection();
        DataStoreContextInitiator.create(applicationContext, MapHBConnectionFactory.getMapHBConnection().getConnection());
    }
}
