package com.echo.one;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 社区资源互换平台
 *
 * @author echo
 * @date 2022-11-03
 */
@EnableScheduling
@SpringBootApplication
@MapperScan("com.echo.one.dao")
public class DataMigrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataMigrationApplication.class, args);
    }

}
