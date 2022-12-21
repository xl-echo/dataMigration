package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.job.DataTaskThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.insertData.0")
public class InsertDataProcessModeZero extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(DataTaskThread.class);

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为0的插入process", traceId);
    }

}
