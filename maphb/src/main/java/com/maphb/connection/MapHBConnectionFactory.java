package com.maphb.connection;

public final class MapHBConnectionFactory {

    private MapHBConnectionFactory() {

    }

    public static MapHBConnection getMapHBConnection() {
        return new MapHBConnectionImpl();
    }
}
