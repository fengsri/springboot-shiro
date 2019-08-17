package com.feng.shiro.common.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description 线程池配置
 * @Author chenlinghong
 * @Date 2019/4/13 17:15
 * @Version V1.0
 */
@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfiguration {

    /**
     * 核心线程数
     */
    @Value("${executor.core_pool_size}")
    private int corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${executor.max_pool_size}")
    private int maxPoolSize;

    /**
     * 队列容量大小
     */
    @Value("${executor.queue_capacity}")
    private int queueCapacity;

    /**
     * 线程池中线程的名称前缀
     */
    @Value("${executor.thread_name_prefix}")
    private String threadNamePrefix;

    /**
     * 保持线程空闲时间
     */
    @Value("${executor.keep_alive_seconds}")
    private int keepAliveSeconds;


    @Bean
    public Executor asyncServiceExecutor() {

        log.info("ExecutorConfiguration#asyncServiceExecutor start...");

        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setKeepAliveSeconds(keepAliveSeconds);

        /**
         * rejection-policy: 当pool已经达到max size时，如何处理新任务
         * caller_runs： 不在新线程中执行任务，而是由调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();

        return executor;
    }


}
