package com.maphb.connection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

/**
 * This interface represents a connection to a MapHB database.
 */
public interface MapHBConnection {

    /**
     * Retrieves the configuration for the MapHB connection.
     *
     * @return the configuration object
     * @throws IOException if an I/O error occurs while retrieving the configuration
     */
    Configuration getConfiguration() throws IOException;

    /**
     * Retrieves the connection to the MapHB database.
     *
     * @return the database connection
     */
    Connection getConnection();

    /**
     * Sets the connection to the MapHB database.
     */
    void setConnection();
}
