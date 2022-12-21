package com.echo.one.common.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author echo
 * @date 2022/12/6 13:14
 */
@Configuration
public class BeanUtilsConfig {

    @Bean(value = "updateLogQueue")
    public Queue<String> updateLogQueue() {
        return new ArrayBlockingQueue<String>(2000);
    }

}
