package com.datahub.flink.entity;

import lombok.Data;

@Data
public class DataTransferConfig {
    private PluginConfig pluginConfig;
    private String pluginRoot;
}
