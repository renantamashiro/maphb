package com.maphb.connection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;


/**
 * Implementation of the MapHBConnection interface.
 * This class manages the connection to HBase and provides methods to retrieve the configuration and connection objects.
 */
class MapHBConnectionImpl implements MapHBConnection {

    private Connection connection;

    final static Logger log = LoggerFactory.getLogger(MapHBConnectionImpl.class);

    protected MapHBConnectionImpl() {

    }

    private Configuration configuration;

    private static final String HBASE_SITE = "hbase-site.xml";


    /**
     * Retrieves the HBase configuration.
     * This method loads the HBase configuration from the hbase-site.xml file and returns the Configuration object.
     * @return The HBase Configuration object.
     * @throws IOException if an I/O error occurs while loading the configuration.
     */
    public Configuration getConfiguration() throws IOException {
        log.info("Starting MapHB connection management.");
        Configuration config = HBaseConfiguration.create();

        URL hbaseSiteFileUrl = MapHBConnectionImpl.class
                .getClassLoader()
                .getResource(HBASE_SITE);

        if (Objects.isNull(hbaseSiteFileUrl)) {
            log.error("File hbase-site.xml does not exist on application resources folder.");
        } else {
            config.addResource(hbaseSiteFileUrl.getPath());
            this.configuration = config;
        }

        return config;
    }


    /**
     * Retrieves the HBase connection.
     * This method returns the Connection object that represents the connection to HBase.
     * @return The HBase Connection object.
     */
    public Connection getConnection() {
        return this.connection;
    }


    /**
     * Sets the HBase connection.
     * This method initializes the HBase connection by creating a Connection object using the HBase Configuration.
     * It also logs the success or failure of the connection initialization.
     */
    public void setConnection() {
        Connection conn = null;
        try {
            this.getConfiguration();
            conn = ConnectionFactory.createConnection(this.configuration);
            log.info("HBase connection created successfully.");
        } catch (IOException e) {
            log.error("An error occurred during hbase connection initialization.");
            log.error("Error message: ()", e);
        }

        this.connection = conn;
    }
}
