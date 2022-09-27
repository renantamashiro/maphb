package com.maphb.connection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * A class to establish a connection with HBase based a given hbase-site.xml file.
 */
class MapHBConnectionImpl implements MapHBConnection {

    protected MapHBConnectionImpl() {

    }

    private Configuration configuration;

    private static final String HBASE_SITE = "hbase-site.xml";

    /**
     * {@inheritDoc}
     */
    public Configuration getConfiguration() throws IOException {
        Configuration config = HBaseConfiguration.create();

        URL hbaseSiteFileUrl = MapHBConnectionImpl.class
                .getClassLoader()
                .getResource(HBASE_SITE);

        if (Objects.isNull(hbaseSiteFileUrl)) {
            throw new IOException("File hbase-site.xml does not exist on application resources folder.");
        } else {
            config.addResource(hbaseSiteFileUrl.getPath());
            this.configuration = config;
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            this.getConfiguration();
            conn = ConnectionFactory.createConnection(this.configuration);
        } catch (IOException e) {
            // add logging
            e.printStackTrace();
        }

        return conn;
    }
}
