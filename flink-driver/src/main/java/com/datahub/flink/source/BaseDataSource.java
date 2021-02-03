package com.datahub.flink.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

public abstract class BaseDataSource {
    public abstract SourceFunction getSourceFunction();
}
