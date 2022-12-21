package com.echo.one.utils.thread;

import com.echo.one.common.exception.DataMigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @date 2022-11-10 11:54
 */
public class ExecutorServiceUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceUtil.class);

    private static ThreadPoolExecutor executorService;

    private static final int INT_CORE_POOL_SIZE = 50;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int WORK_QUEUE_SIZE = 2000;

    static {
        executorService = new ThreadPoolExecutor(
                INT_CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(WORK_QUEUE_SIZE),
                new CommunityThreadFactory(),
                new DataMigrationRejectedExecutionHandler());
    }

    private ExecutorServiceUtil() {
        if(executorService != null){
            throw new DataMigrationException("Reflection call constructor terminates execution!!!");
        }
    }

    public static ExecutorService getInstance() {
        logger.info("queue size: {}", executorService.getQueue().size());
        logger.info("Number of active threads: {}", executorService.getActiveCount());
        logger.info("Number of execution completion threads: {}", executorService.getCompletedTaskCount());
        return executorService;
    }
}
