package com.datahub.flink.classloader;


import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jiangbo
 * @date 2019/10/21
 */
public class PluginUtil {

    private static final String COMMON_DIR = "common";

    private static final String READER_SUFFIX = "reader";

    private static final String WRITER_SUFFIX = "writer";

    private static final String PACKAGE_PREFIX = "com.datahub.flink.";

    private static final String JAR_PREFIX = "flinkx";

    private static final String SP = File.separator;
    public static List<URL> findJarsInDir(File dir)  throws MalformedURLException {
        List<URL> urlList = new ArrayList<>();

        if(dir.exists() && dir.isDirectory()) {
            File[] jarFiles = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jar");
                }
            });

            for(File jarFile : jarFiles) {
                urlList.add(jarFile.toURI().toURL());
            }

        }

        return urlList;
    }
    public static Set<URL> getJarFileDirPath(String pluginName, String pluginRoot) {
        Set<URL> urlList = new HashSet<>();
        List<File> pathDir = new ArrayList<>();

        if (pluginRoot != null) {
            pathDir.add(new File(pluginRoot + File.separator + pluginName));
//            pathDir.add(new File(pluginRoot + File.separator + COMMON_DIR + File.separator));
        }

        try {
            for(File path : pathDir) {
                urlList.addAll(findJarsInDir(path));
            }

            return urlList;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPluginClassName(String pluginName) {
        String pluginClassName;
        if (pluginName.toLowerCase().endsWith(READER_SUFFIX)) {
            pluginClassName = PACKAGE_PREFIX + camelize(pluginName, READER_SUFFIX);
        } else if (pluginName.toLowerCase().endsWith(WRITER_SUFFIX)) {
            pluginClassName = PACKAGE_PREFIX + camelize(pluginName, WRITER_SUFFIX);
        } else {
            throw new IllegalArgumentException("Plugin Name should end with reader, writer or database");
        }

        return pluginClassName;
    }

    private static String camelize(String pluginName, String suffix) {
        int pos = pluginName.indexOf(suffix);
        String left = pluginName.substring(0, pos);
        left = left.toLowerCase();
        suffix = suffix.toLowerCase();
        StringBuffer sb = new StringBuffer();
        sb.append(left + "." + suffix + ".");
        sb.append(left.substring(0, 1).toUpperCase() + left.substring(1));
        sb.append(suffix.substring(0, 1).toUpperCase() + suffix.substring(1));
        return sb.toString();
    }
}
