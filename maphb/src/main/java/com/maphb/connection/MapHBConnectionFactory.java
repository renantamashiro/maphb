package com.maphb.connection;

import java.util.Objects;

public final class MapHBConnectionFactory {

    private static MapHBConnection mapHBConnection = null;

    private MapHBConnectionFactory() {

    }

    public static MapHBConnection getMapHBConnection() {
        if (Objects.isNull(mapHBConnection)) {
            mapHBConnection = new MapHBConnectionImpl();
        }
        return mapHBConnection;
    }
}
