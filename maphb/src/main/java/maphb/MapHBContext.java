package maphb;

import maphb.connection.MapHBConnectionFactory;
import maphb.manager.DataStoreContextInitiator;

public final class MapHBContext {

    public static void startApplication(Class<?> applicationContext) {
        MapHBConnectionFactory.getMapHBConnection().setConnection();
        DataStoreContextInitiator.create(applicationContext, MapHBConnectionFactory.getMapHBConnection().getConnection());
    }
}
