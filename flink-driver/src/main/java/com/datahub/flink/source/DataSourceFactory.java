package com.datahub.flink.source;

import com.datahub.flink.classloader.ClassLoaderManager;
import com.datahub.flink.classloader.PluginUtil;
import com.datahub.flink.entity.DataTransferConfig;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Set;

public class DataSourceFactory {
    public static BaseDataSource getDataSource(DataTransferConfig config){
        String pluginName = config.getPluginConfig().getSourceConfig().getName();
        String pluginClassName = PluginUtil.getPluginClassName(pluginName);
        Set<URL> urlList = PluginUtil.getJarFileDirPath(pluginName, config.getPluginRoot());
        try {
            return ClassLoaderManager.newInstance(urlList, cl -> {
                Class<?> clazz = cl.loadClass(pluginClassName);
                Constructor constructor = clazz.getConstructor(DataTransferConfig.class);
                return (BaseDataSource) constructor.newInstance(config);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
