package com.datahub.flink.classloader;

/**
 * @company: www.dtstack.com
 * @author: toutian
 * @create: 2019/10/14
 */
public class ClassLoaderSupplierCallBack {

    public static <R> R callbackAndReset(ClassLoaderSupplier<R> supplier, ClassLoader toSetClassLoader) throws Exception {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(toSetClassLoader);
        try {
            return supplier.get(toSetClassLoader);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }
}
