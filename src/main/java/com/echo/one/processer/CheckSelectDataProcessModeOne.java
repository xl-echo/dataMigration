package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.base.TransferDataContext;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.po.TransferSelectConfig;
import com.echo.one.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.checkSelectData.1")
public class CheckSelectDataProcessModeOne extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(CheckSelectDataProcessModeOne.class);

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为1的检查查询代码", traceId);

        TransferDataContext bean = SpringContextUtils.getBean(traceId, TransferDataContext.class);

        if (null == bean) {
            logger.info("traceId: {}, 检查查询类执行: 未获取到容器类注入的数据类!", traceId);
            throw new DataMigrationException("检查查询类执行: 未获取到容器类注入的数据类！");
        }

        List<String> list = bean.getList();
        int size = list.size();

        TransferSelectConfig transferSelectConfig = transferContext.getTransferSelectConfig();
        Integer limitSize = transferSelectConfig.getLimitSize();

        if (size > limitSize) {
            logger.info("traceId: {}, 检查查询类执行: 查询出来的数据大于limit值!", traceId);
            throw new DataMigrationException("检查查询类执行时: 查询出来的数据大于limit值!");
        }
    }

}
