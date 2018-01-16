package com.example.jooq.config;


import com.example.jooq.config.exception.JOOQToSpringExceptionTransformer;
import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"com.example.jooq.config"})
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class PersistenceContext {

    private static final String PROPERTY_NAME_DB_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DB_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DB_SCHEMA_SCRIPT = "db.schema.script";
    private static final String PROPERTY_NAME_DB_URL = "db.url";
    private static final String PROPERTY_NAME_DB_USERNAME = "db.username";
    private static final String PROPERTY_NAME_JOOQ_SQL_DIALECT = "jooq.sql.dialect";

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DB_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DB_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DB_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));

        return dataSource;
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource());
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean
    public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
        return new JOOQToSpringExceptionTransformer();
    }

    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(
                jooqToSpringExceptionTransformer()
        ));


        String sqlDialectName = env.getRequiredProperty(PROPERTY_NAME_JOOQ_SQL_DIALECT);
        SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
        jooqConfiguration.set(dialect);

        return jooqConfiguration;
    }

    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }



}
