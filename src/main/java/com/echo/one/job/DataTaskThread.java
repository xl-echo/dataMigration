package com.echo.one.job;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.processer.ProcessMode;
import com.echo.one.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * @author echo
 * @date 2022/11/10 15:34
 */
public class DataTaskThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataTaskThread.class);

    private TransferContext transferContext;

    public DataTaskThread(TransferContext transferContext) {
        this.transferContext = transferContext;
    }

    @Override
    public void run() {
        TransferContext transferContext = new TransferContext();
        BeanUtils.copyProperties(this.transferContext, transferContext);
        String traceId = transferContext.getTraceId();
        String taskId = transferContext.getTransferDataTask().getTaskId();
        logger.info("traceId: {}, 任务taskId: {}, 开启新线程执行历史数据迁移任务", traceId, taskId);

        // 查询源表数据
        ProcessMode sourceBean = SpringContextUtils.getBean("process.selectData." + transferContext.getTransferDataTask().getTransferMode(), ProcessMode.class);
        if (sourceBean == null) {
            logger.info("traceId: {}, 未找到mode: {}的查询模型代码，终止执行!", traceId, transferContext.getTransferDataTask().getTransferMode());
            throw new DataMigrationException("未找到mode对应的查询模型代码，终止执行!");
        }
        sourceBean.invoke(transferContext);

        // 校验查询出来的数据是否有误
        ProcessMode checkSelectBean = SpringContextUtils.getBean("process.checkSelectData." + transferContext.getTransferDataTask().getTransferMode(), ProcessMode.class);
        if (checkSelectBean == null) {
            logger.info("traceId: {}, 未找到mode: {}的模式插入代码，终止执行!", traceId, transferContext.getTransferDataTask().getTransferMode());
            throw new DataMigrationException("未找到mode对应的模式代码，终止执行!");
        }
        checkSelectBean.invoke(transferContext);

        // 插入目标数据
        ProcessMode targetBean = SpringContextUtils.getBean("process.insertData." + transferContext.getTransferDataTask().getTransferMode(), ProcessMode.class);
        if (targetBean == null) {
            logger.info("traceId: {}, 未找到mode: {}的模式插入代码，终止执行!", traceId, transferContext.getTransferDataTask().getTransferMode());
            throw new DataMigrationException("未找到mode对应的模式代码，终止执行!");
        }
        targetBean.invoke(transferContext);

        // 校验插入的数据是否有误
        ProcessMode checkInsertBean = SpringContextUtils.getBean("process.checkInsertData." + transferContext.getTransferDataTask().getTransferMode(), ProcessMode.class);
        if (checkInsertBean == null) {
            logger.info("traceId: {}, 未找到mode: {}的模式插入代码，终止执行!", traceId, transferContext.getTransferDataTask().getTransferMode());
            throw new DataMigrationException("未找到mode对应的模式代码，终止执行!");
        }
        checkInsertBean.invoke(transferContext);

        // 删除源数据
        ProcessMode deleteBean = SpringContextUtils.getBean("process.deleteData." + transferContext.getTransferDataTask().getTransferMode(), ProcessMode.class);
        if (deleteBean == null) {
            logger.info("traceId: {}, 未找到mode: {}的模式插入代码，终止执行!", traceId, transferContext.getTransferDataTask().getTransferMode());
            throw new DataMigrationException("未找到mode对应的模式代码，终止执行!");
        }
        deleteBean.invoke(transferContext);
        logger.info("traceId: {}, 任务taskId: {}, 历史数据迁移任务执行完成", traceId, taskId);

    }

}
