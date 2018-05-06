package com.application.upstox.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by harsh.jain on 5/6/18.
 */

public class ExecutorSupplier {

    private static ExecutorSupplier executorSupplier;
    private final ThreadPoolExecutor workerThreadExecutor;

    private ExecutorSupplier() {
        workerThreadExecutor = new ThreadPoolExecutor(2, 2, 30L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public static ExecutorSupplier getInstance() {
        if (executorSupplier == null) {
            executorSupplier = new ExecutorSupplier();
        }

        return executorSupplier;
    }

    public ThreadPoolExecutor getWorkerThreadExecutor() {
        return workerThreadExecutor;
    }
}
