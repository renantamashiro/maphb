package com.example.demoservice;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class HbaseConfig {

    @Bean
    public Configuration getConnection() {
        Configuration config = HBaseConfiguration.create();

        String path = this.getClass()
                .getClassLoader()
                .getResource("hbase-site.xml")
                .getPath();

        config.addResource(path);

        return config;
    }

    @Bean
    public Connection connection() throws IOException {
        HbaseConfig c = new HbaseConfig();
        Configuration conf = c.getConnection();

        Connection conn = ConnectionFactory.createConnection(conf);
        return conn;
    }
}
