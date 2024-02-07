package com.maphb.connection;

import java.util.Objects;

/**
 * The MapHBConnectionFactory class is responsible for providing a singleton instance of MapHBConnection.
 * It ensures that only one instance of MapHBConnection is created and shared across the application.
 */
public final class MapHBConnectionFactory {

    private static MapHBConnection mapHBConnection = null;

    private MapHBConnectionFactory() {

    }

    /**
     * Returns the singleton instance of MapHBConnection.
     * If the instance does not exist, it creates a new instance of MapHBConnectionImpl and returns it.
     *
     * @return The singleton instance of MapHBConnection.
     */
    public static MapHBConnection getMapHBConnection() {
        if (Objects.isNull(mapHBConnection)) {
            mapHBConnection = new MapHBConnectionImpl();
        }
        return mapHBConnection;
    }
}
