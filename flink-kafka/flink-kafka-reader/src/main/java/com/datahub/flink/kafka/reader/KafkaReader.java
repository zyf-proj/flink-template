package com.datahub.flink.kafka.reader;

import com.datahub.flink.entity.DataTransferConfig;
import com.datahub.flink.source.BaseDataSource;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public class KafkaReader extends BaseDataSource {
    private DataTransferConfig config;
    public KafkaReader(){}
    public KafkaReader(DataTransferConfig config){
        this.config = config;
    }
    @Override
    public SourceFunction getSourceFunction() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.126.144.34:9092,10.126.144.35:9092,10.126.144.36:9092");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        //flink配置新groupId时,此配置启作用,不配置的话,默认latest,新的groupId也不会从最早的offset消费
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
//		FlinkKafkaConsumer010<ObjectNode> kafkaConsumer010 = new FlinkKafkaConsumer010<ObjectNode>("wordcount", new JSONKeyValueDeserializationSchema(true), properties);
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<String>("test", new SimpleStringSchema(), properties);
        kafkaConsumer.setStartFromLatest();
        kafkaConsumer.setCommitOffsetsOnCheckpoints(true);
        return kafkaConsumer;
    }
}
