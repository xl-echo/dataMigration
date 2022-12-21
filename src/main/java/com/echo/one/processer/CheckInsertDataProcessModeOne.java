package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.factory.DataMigrationBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.checkInsertData.1")
public class CheckInsertDataProcessModeOne extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(CheckInsertDataProcessModeOne.class);

    @Autowired
    private DataMigrationBeanFactory dataMigrationBeanFactory;

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为1的检查插入代码", traceId);

        // 清空当前traceId在容器中的数据
        dataMigrationBeanFactory.removeBean(traceId);
    }

}
