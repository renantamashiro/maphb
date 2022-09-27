package com.maphb.connection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

public interface MapHBConnection {

    /**
     * Set the necessary configurations for a connection with HBase.
     */
    Configuration getConfiguration() throws IOException;

    /**
     * Retrieve a connection with Hbase if succeeded.
     */
    Connection getConnection();
}
