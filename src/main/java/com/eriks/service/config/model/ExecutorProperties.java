package com.eriks.service.config.model;

import lombok.Getter;

@Getter
public class ExecutorProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;

    public ExecutorProperties(String corePoolSize, String maxPoolSize, String queueCapacity) {
        this.corePoolSize = parseInt(corePoolSize);
        this.maxPoolSize = parseInt(maxPoolSize);
        this.queueCapacity = parseInt(queueCapacity);
    }

    private int parseInt(String val) {
        return Integer.valueOf(val);
    }
}
