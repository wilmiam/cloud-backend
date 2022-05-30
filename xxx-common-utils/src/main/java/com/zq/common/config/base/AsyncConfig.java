package com.zq.common.config.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步执行配置
 * <p>
 * 注解ConditionalOnProperty(value = "task.pool.enable", havingValue = "true")
 * 当获取到async.pool.enable的值等于havingValue的值时才加载配置
 *
 * @author wilmiam
 * @since 2021-07-09 17:50
 */
@Configuration
@ConditionalOnProperty(value = "task.pool.enable", havingValue = "true")
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * 核心线程池大小
     */
    @Value("${task.pool.core-pool-size:5}")
    private int corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${task.pool.max-pool-size:15}")
    private int maxPoolSize;

    /**
     * 队列容量
     */
    @Value("${task.pool.queue-capacity:20}")
    private int queueCapacity;

    /**
     * 活跃时间
     */
    @Value("${task.pool.keep-alive-seconds:30}")
    private int threadTimeout;

    @Override
    public Executor getAsyncExecutor() {
        log.info(">> 初始化spring线程池...");
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        // 当一个任务通过execute(Runnable)方法欲添加到线程池时：
        // - 若线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态，也创建新的线程来处理被添加的任务。
        // - 若线程池中的数量等于corePoolSize，但缓冲队列workQueue未满，则任务被放入缓冲队列。
        // - 若线程池中的数量大于corePoolSize，缓冲队列workQueue满，且线程池中的数量小于maximumPoolSize，则创建新线程来处理被添加的任务。
        // - 若线程池中的数量大于corePoolSize，缓冲队列workQueue满，且线程池中的数量等于maximumPoolSize，则通过handler所指定的策略来处理此任务。
        //   即：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程 maximumPoolSize，若三者都满，则使用handler处理被拒绝的任务。
        // - 若线程池中的线程数量大于corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。

        // 线程池维护线程的最少数量
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        // 线程池维护线程的最大数量
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        // 线程池所使用的缓冲队列
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        // 线程池维护线程所允许的空闲时间
        threadPoolTaskExecutor.setKeepAliveSeconds(threadTimeout);

        threadPoolTaskExecutor.setThreadNamePrefix("cloud-async-");

        // don't forget to initialize the thread pool
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            String methodName = method.getDeclaringClass().getName() + method.getName();
            log.error(">> 异步执行错误: {}({})", methodName, params, throwable);
        };
    }

}
