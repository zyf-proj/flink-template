package com.datahub.flink.sink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public abstract class BaseDataSink {
    public abstract SinkFunction getSinkFunction();

}
