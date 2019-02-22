package com.eriks.service.config;

import com.eriks.service.config.model.DBProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;
import org.sql2o.quirks.PostgresQuirks;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final DBProperties dbProperties;

    public DataSourceConfig(final DBProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    @Bean
    public DataSource primaryDataSource() {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbProperties.getDbConnectionUrl());
        hikariConfig.setUsername(dbProperties.getDbUsername());
        hikariConfig.setPassword(dbProperties.getDbPassword());
        hikariConfig.setDriverClassName(dbProperties.getDbDriverClassName());

        hikariConfig.setMaximumPoolSize(dbProperties.getHikariMaximumPoolSize());
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.setConnectionTimeout(dbProperties.getHikariConnectionTimeout());
        hikariConfig.setMinimumIdle(dbProperties.getHikariMinimumIdle());
        hikariConfig.setIdleTimeout(dbProperties.getHikariIdleTimeout());
        hikariConfig.setMaxLifetime(dbProperties.getHikariMaxLifetime());

        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

        return new HikariDataSource(hikariConfig);
    }


    @Bean
    public Sql2o sql2o(DataSource primaryDataSource) {
        return new Sql2o(primaryDataSource, new PostgresQuirks());
    }
}
