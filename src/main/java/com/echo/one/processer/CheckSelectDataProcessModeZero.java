package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.exception.DataMigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.checkSelectData.0")
public class CheckSelectDataProcessModeZero extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(CheckSelectDataProcessModeZero.class);

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为0的检查查询代码", traceId);

        // 根据配置sql查询源表总计多少条数据
        int sourceNum = 0;

        // 根据配置sql查询目标表总计多少条数据
        int targetNum = 0;

        if(sourceNum != targetNum) {
            throw new DataMigrationException(transferContext.getTransferDataTask().getTaskId() + ", 迁移出现问题! insert into select 完成后, 目标表和源表数据条数对不上");
        }

    }

}
