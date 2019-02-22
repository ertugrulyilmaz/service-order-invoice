package com.eriks.service.config.model;

import lombok.Getter;
import software.amazon.awssdk.regions.Region;

@Getter
public class KinesisProperties {

  private final Region region;
  private final String streamName;

  public KinesisProperties(final String region, final String streamName) {
    this.region = Region.of(region);
    this.streamName = streamName;
  }

}
