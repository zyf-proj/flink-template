package com.datahub.flink.driver;

import com.datahub.flink.entity.ConfigConstant;
import com.datahub.flink.entity.DataTransferConfig;
import com.datahub.flink.entity.PluginConfig;
import com.datahub.flink.entity.SourceConfig;
import com.datahub.flink.source.BaseDataSource;
import com.datahub.flink.source.DataSourceFactory;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//flink-template main driver
public class Driver {
    public static void main(String[] args)throws Exception{
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        setEnv(env, parameterTool);


        DataTransferConfig config = new DataTransferConfig();
        PluginConfig pluginConfig = new PluginConfig();
        SourceConfig sourceConfig = new SourceConfig();
        sourceConfig.setName("kafkareader");
        pluginConfig.setSourceConfig(sourceConfig);
        config.setPluginConfig(pluginConfig);
        config.setPluginRoot("/Users/zyf/IdeaProjects/flink-template/syncplugins");
        BaseDataSource dataSource = DataSourceFactory.getDataSource(config);

        DataStreamSource<String> source = env.addSource(dataSource.getSourceFunction());

        source.print();
//        BaseDataSink dataSink = DataSinkFactory.getDataSink(config);
//        source.addSink(dataSink.getSinkFunction());

        // execute program
        env.execute("Flink Streaming Java API Skeleton");
    }
    public static void setEnv(StreamExecutionEnvironment env, ParameterTool parameterTool){
        int checkpointInterval = parameterTool.getInt(ConfigConstant.CHECKPOINT_INTERVAL, -1);
        if(checkpointInterval > 0){
            String checkpointMode = parameterTool.get(ConfigConstant.CHECKPOINT_MODE, CheckpointingMode.AT_LEAST_ONCE.name());
            long minPause = parameterTool.getLong(ConfigConstant.CHECKPOINT_MIN_PAUSE, 0);
            long timeout = parameterTool.getLong(ConfigConstant.CHECKPOINT_TIMEOUT, 600000L);
            int maxConcurrentCheckpoints = parameterTool.getInt(ConfigConstant.CHECKPOINT_MAX_CONCURRENT, 1);
            env.enableCheckpointing(checkpointInterval, CheckpointingMode.valueOf(checkpointMode));
            //确保检查点之间有1s的时间间隔【checkpoint最小间隔】
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(minPause);
            //检查点必须在10s之内完成，或者被丢弃【checkpoint超时时间】
            env.getCheckpointConfig().setCheckpointTimeout(timeout);
            //同一时间只允许进行一次检查点
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(maxConcurrentCheckpoints);
        }
    }
}
