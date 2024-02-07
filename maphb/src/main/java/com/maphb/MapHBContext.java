package com.maphb;

import com.maphb.connection.MapHBConnectionFactory;
import com.maphb.manager.DataStoreContext;
import com.maphb.manager.DataStoreContextInitiator;
import org.apache.hadoop.hbase.client.Connection;

/**
 * The MapHBContext class provides methods for starting the application and obtaining a database connection.
 */
public final class MapHBContext {

    /**
     * Starts the application and sets up the database connection.
     *
     * @param applicationContext the class representing the application context
     * @return the initialized DataStoreContext
     */
    public static DataStoreContext startApplication(Class<?> applicationContext) {
        MapHBConnectionFactory.getMapHBConnection().setConnection();
        return DataStoreContextInitiator.create(applicationContext, MapHBConnectionFactory.getMapHBConnection().getConnection());
    }

    /**
     * Retrieves the database connection.
     *
     * @return the database connection
     */
    public static Connection getConnection() {
       return MapHBConnectionFactory.getMapHBConnection().getConnection();
    }
}
