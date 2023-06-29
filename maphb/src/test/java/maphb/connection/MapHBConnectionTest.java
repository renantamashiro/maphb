package maphb.connection;

import org.apache.hadoop.conf.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class MapHBConnectionTest {

    @Test
    @DisplayName("Should create a Configuration class for connection with hbase")
    void shouldCreateConfigurationClass() throws IOException {

        Configuration configuration = MapHBConnectionFactory.getMapHBConnection().getConfiguration();
        assertThat(configuration).isInstanceOf(Configuration.class)
                .asString()
                .endsWith("hbase-site.xml");
    }

}