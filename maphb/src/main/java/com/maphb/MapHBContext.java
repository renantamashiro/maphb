package com.maphb;

import com.maphb.connection.MapHBConnectionFactory;
import org.apache.hadoop.hbase.client.Connection;

import java.util.Objects;

public final class MapHBContext {

    // utility method which will be deleted. Connection needs to be live here
    public static Connection getConnection() {
        Connection conn = MapHBConnectionFactory.getMapHBConnection().getConnection();

        if (Objects.isNull(conn)) {
            System.out.println("Impossible to establish a connection with HBase");
        } else {
            System.out.println("Connection created!");
        }

        return conn;
    }
}
