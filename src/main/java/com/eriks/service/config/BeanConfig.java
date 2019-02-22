package com.eriks.service.config;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.eriks.service.config.model.DBProperties;
import com.eriks.service.config.model.ExecutorProperties;
import com.eriks.service.config.model.KinesisProperties;
import com.eriks.service.config.model.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Base64;

@Slf4j
@Configuration
@Profile("!test")
public class BeanConfig {

  @Value("${consul.agent.host}")
  private String consulAgentHost;

  @Value("${consul.agent.port}")
  private int consulAgentPort;

  @Bean
  public ConsulClient consulClient() {
    return new ConsulClient(consulAgentHost, consulAgentPort);
  }

  @Bean
  public DBProperties dbProperties(ConsulClient consulClient) {
    return new DBProperties(
    getConsulValue(consulClient, "app/datasource/dbConnectionUrl"),
    getConsulValue(consulClient, "app/datasource/dbDriverClassName"),
    getConsulValue(consulClient, "app/datasource/dbUsername"),
    getConsulValue(consulClient, "app/datasource/dbPassword"),
    getConsulValue(consulClient, "app/datasource/hikariIdleTimeout"),
    getConsulValue(consulClient, "app/datasource/hikariMinimumIdle"),
    getConsulValue(consulClient, "app/datasource/hikariMaximumPoolSize"),
    getConsulValue(consulClient, "app/datasource/hikariConnectionTimeout"),
    getConsulValue(consulClient, "app/datasource/hikariMaxLifetime")
    );
  }

  @Bean
  public ExecutorProperties executorProperties(ConsulClient consulClient) {
    return new ExecutorProperties(
    getConsulValue(consulClient, "app/executorConfig/corePoolSize"),
    getConsulValue(consulClient, "app/executorConfig/maxPoolSize"),
    getConsulValue(consulClient, "app/executorConfig/queueCapacity")
    );
  }

  @Bean
  public SecurityProperties securityProperties(ConsulClient consulClient) {
    return new SecurityProperties(
    getConsulValue(consulClient, "app/security/jwtAuthExpMinute"),
    getConsulValue(consulClient, "app/security/jwtAuthSecret")
    );
  }

  @Bean
  public KinesisProperties kinesisProperties(final ConsulClient consulClient) {
    return new KinesisProperties(
        getConsulValue(consulClient, "app/aws/region"),
        getConsulValue(consulClient, "app/aws/kinesis/streamName")
    );
  }

  private static String getConsulValue(final ConsulClient consulClient, final String key) {
    try {
      final GetValue getValue = consulClient.getKVValue(key).getValue();

      if (getValue == null) {
        log.error("Did you add global parameters (key/value) to consul ? Hint: [sh start_consul.sh] key is -> " + key);
        System.exit(0);
      }

      final String encoded = getValue.getValue();

      return new String(Base64.getDecoder().decode(encoded.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
