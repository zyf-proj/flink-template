package com.datahub.flink.classloader;

/**
 * Represents a supplier of results.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 *
 * @since 1.8
 * @author toutian
 */
@FunctionalInterface
public interface ClassLoaderSupplier<T> {

    /**
     * 使用给定的类加载器创建对象
     *
     * @param cl 类加载器
     * @return 实例化的对象
     * @throws Exception NoSuchMethodException SecurityException
     */
    T get(ClassLoader cl) throws Exception;
}
