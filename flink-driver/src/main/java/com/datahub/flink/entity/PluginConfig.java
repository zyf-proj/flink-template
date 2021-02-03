package com.datahub.flink.entity;

import lombok.Data;

@Data
public class PluginConfig {
    private SourceConfig sourceConfig;
    private SinkConfig sinkConfig;
}
