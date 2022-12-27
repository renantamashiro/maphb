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
 * A class to establish a connection with HBase based a given hbase-site.xml file.
 */
class MapHBConnectionImpl implements MapHBConnection {

    private Connection connection;

    final static Logger log = LoggerFactory.getLogger(MapHBConnectionImpl.class);

    protected MapHBConnectionImpl() {

    }

    private Configuration configuration;

    private static final String HBASE_SITE = "hbase-site.xml";

    /**
     * {@inheritDoc}
     */
    public Configuration getConfiguration() throws IOException {
        log.info("MapHB connection management initialize.");
        Configuration config = HBaseConfiguration.create();

        URL hbaseSiteFileUrl = MapHBConnectionImpl.class
                .getClassLoader()
                .getResource(HBASE_SITE);

        if (Objects.isNull(hbaseSiteFileUrl)) {
            log.error("File hbase-site.xml does not exist on application resources folder.");
        } else {
            log.info("HBase connection created successfully.");
            config.addResource(hbaseSiteFileUrl.getPath());
            this.configuration = config;
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * {@inheritDoc}
     */
    public void setConnection() {
        Connection conn = null;
        try {
            this.getConfiguration();
            conn = ConnectionFactory.createConnection(this.configuration);
        } catch (IOException e) {
            log.error("An error occurred during hbase connection initialization.");
            log.error("Error message: ()", e);
        }

        this.connection = conn;
    }
}
