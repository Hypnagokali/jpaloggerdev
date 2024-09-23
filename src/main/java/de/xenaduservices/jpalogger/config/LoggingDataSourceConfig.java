package de.xenaduservices.jpalogger.config;

import javax.sql.DataSource;

import de.xenaduservices.jpalogger.logging.ProxyDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class LoggingDataSourceConfig {

    private final DataSourceProperties properties;

    public LoggingDataSourceConfig(DataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DataSource loggingDataSource() {

        DataSource ds = DataSourceBuilder.create()
            .driverClassName( properties.getDriverClassName() )
            .url( properties.getUrl() )
            .username( properties.getUsername() )
            .password( properties.getPassword() )
            .build();

        return new ProxyDataSource( ds );
    }

}
