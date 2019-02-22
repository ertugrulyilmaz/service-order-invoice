package com.eriks.service.config.model;

import lombok.Getter;

@Getter
public class DBProperties {
    private String dbConnectionUrl;
    private String dbDriverClassName;
    private String dbUsername;
    private String dbPassword;
    private long hikariIdleTimeout;
    private int hikariMinimumIdle;
    private int hikariMaximumPoolSize;
    private int hikariConnectionTimeout;
    private long hikariMaxLifetime;


    public DBProperties(String dbConnectionUrl,
                        String dbDriverClassName,
                        String dbUsername,
                        String dbPassword,
                        String hikariIdleTimeout,
                        String hikariMinimumIdle,
                        String hikariMaximumPoolSize,
                        String hikariConnectionTimeout,
                        String hikariMaxLifetime) {
        this.dbConnectionUrl = dbConnectionUrl;
        this.dbDriverClassName = dbDriverClassName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.hikariIdleTimeout = parseLong(hikariIdleTimeout);
        this.hikariMinimumIdle = parseInt(hikariMinimumIdle);
        this.hikariMaximumPoolSize = parseInt(hikariMaximumPoolSize);
        this.hikariConnectionTimeout = parseInt(hikariConnectionTimeout);
        this.hikariMaxLifetime = parseLong(hikariMaxLifetime);
    }


    private int parseInt(String val) {
        return Integer.valueOf(val);
    }

    private long parseLong(String val) {
        return Long.valueOf(val);
    }
}
