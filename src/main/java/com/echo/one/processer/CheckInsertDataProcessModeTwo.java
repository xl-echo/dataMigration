package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 使用mysqldump文件导出sql（要求必须能够准确的知道dump文件位置）
 *
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/22 9:31
 */
@Scope(value = "prototype")
@Component(value = "process.checkInsertData.2")
public class CheckInsertDataProcessModeTwo extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(CheckInsertDataProcessModeTwo.class);

    @Override
    protected void handler(TransferContext transferContext) {

        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为2的检查插入代码", traceId);

    }
}
