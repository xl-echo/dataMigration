package com.echo.one;

import com.echo.one.utils.uuid.SnowflakeIdWorker;

/**
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/23 13:40
 */
public class SnowflakeIdWorkerTest {

    public static void main(String[] args) {

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
        long l = snowflakeIdWorker.nextId();
        System.out.println(System.currentTimeMillis());
        System.out.println(l);

    }

}
