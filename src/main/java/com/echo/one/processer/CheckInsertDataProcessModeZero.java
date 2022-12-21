package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.checkInsertData.0")
public class CheckInsertDataProcessModeZero extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(CheckInsertDataProcessModeZero.class);

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为0的删除过程", transferContext.getTraceId());


    }

}
