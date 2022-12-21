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
@Component(value = "process.deleteData.0")
public class DeleteDataProcessModeZero extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(DeleteDataProcessModeZero.class);

    @Override
    protected void handler(TransferContext transferContext) {
        logger.info("traceId: {}, 开始执行mode为0的删除process", transferContext.getTraceId());
    }

}
