package com.echo.one.job;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.enums.TaskStatus;
import com.echo.one.po.*;
import com.echo.one.service.*;
import com.echo.one.utils.DateUtils;
import com.echo.one.utils.thread.ExecutorServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author echo
 * @date 2022/11/10 15:28
 */
@Component
public class TransferDataTaskJob {

    private static final Logger logger = LoggerFactory.getLogger(TransferDataTaskJob.class);

    @Autowired
    private TransferDataTaskService transferDataTaskService;
    @Autowired
    private TransferDatabaseConfigService transferDatabaseConfigService;
    @Autowired
    private TransferInsertConfigService transferInsertConfigService;
    @Autowired
    private TransferSelectConfigService transferSelectConfigService;
    @Autowired
    private TransferLogService transferLogService;

    @Scheduled(cron = "${task.job.transfer.start}")
    public void startTransfer() {
        String traceId = UUID.randomUUID().toString();
        logger.info("traceId:{}-定时任务: 开始执行数据迁移！", traceId);

        List<TransferDataTask> transferDataTasks = transferDataTaskService.getTaskTransfer();

        if (CollectionUtils.isEmpty(transferDataTasks)) {
            logger.info("traceId:{}-定时任务: 未查询到对应的数据迁移任务，程序结束执行", traceId);
            return;
        }

        List<TransferContext> transferContexts = packageTransferContext(transferDataTasks);
        logger.info("traceId:{}-定时任务: 查询到的历史数据任务有: {}个", traceId, transferContexts.size());

        // 新建线程执行任务
        newThreadExec(traceId, transferContexts);
    }

    private void newThreadExec(String traceId, List<TransferContext> transferContexts) {
        if (CollectionUtils.isEmpty(transferContexts)) return;

        for (int i = 0; i < transferContexts.size(); i++) {
            TransferContext transferContext = transferContexts.get(i);
            boolean b = oprLog(transferLogService, transferContext);

            if (!b) continue;

            ExecutorService instance = ExecutorServiceUtil.getInstance();
            instance.execute(new DataTaskThread(transferContext));
            logger.info("traceId: {}, taskId:{}-数据迁移任务, 开始执行; ", traceId, transferContext.getTransferDataTask().getTaskId());
        }
    }

    private boolean oprLog(TransferLogService transferLogService, TransferContext transferContext) {
        TransferLog log = transferLogService.getLog(transferContext.getTransferDataTask().getTaskId(), DateUtils.getCurDate());
        if (log == null) {
            insertLog(transferLogService, transferContext);
            return true;
        }

        String status = log.getStatus();
        if (TaskStatus.ING.getCode().equals(status)) {
            logger.info("traceId: {}, taskId:{} 本次迁移任务正在执行，不在继续创建新任务执行! ", transferContext.getTraceId(), transferContext.getTransferDataTask().getTaskId());
            return false;
        }

        if (TaskStatus.SUCCESS.getCode().equals(status)) {
            logger.info("traceId: {}, taskId:{} 本次迁移任务在当日已经成功执行，不在继续创建新任务执行! ", transferContext.getTraceId(), transferContext.getTransferDataTask().getTaskId());
            return false;
        }

        insertLog(transferLogService, transferContext);
        return true;
    }

    private void insertLog(TransferLogService transferLogService, TransferContext transferContext) {
        TransferLog transferLog = TransferLog.builder()
                .taskId(transferContext.getTransferDataTask().getTaskId())
                .traceId(transferContext.getTraceId())
                .taskBeginDate(new Date())
                .status(TaskStatus.ING.getCode())
                .createUname("sys")
                .createTime(new Date())
                .build();
        transferLogService.insertLog(transferLog);
    }

    /**
     * 封装历史迁移任务context
     *
     * @param transferDataTasks
     * @return
     */
    private List<TransferContext> packageTransferContext(List<TransferDataTask> transferDataTasks) {

        // 获取taskId
        List<String> taskIds = transferDataTasks.stream().map(TransferDataTask::getTaskId).distinct().collect(Collectors.toList());

        // 查询数据库配置
        List<TransferDatabaseConfig> transferDatabaseConfigs = transferDatabaseConfigService.getBatchDatabaseConfig(taskIds);
        Map<String, List<TransferDatabaseConfig>> collect = transferDatabaseConfigs.stream().collect(Collectors.groupingBy(TransferDatabaseConfig::getTaskId));

        // 查询源表信息
        List<TransferSelectConfig> transferSelectConfigs = transferSelectConfigService.getBatchTransferSelectConfig(taskIds);

        // 查询目标表信息
        List<TransferInsertConfig> transferInsertConfigs = transferInsertConfigService.getBatchTransferInsertConfig(taskIds);

        List<TransferContext> list = new ArrayList<>();
        for (int i = 0; i < transferDataTasks.size(); i++) {
            TransferDataTask transferDataTask = transferDataTasks.get(i);

            TransferContext transferContext = new TransferContext();
            String traceId = UUID.randomUUID().toString();
            transferContext.setTraceId(traceId);

            List<TransferDatabaseConfig> transferDatabaseConfigs1 = collect.get(transferDataTask.getTaskId());
            if (CollectionUtils.isEmpty(transferDatabaseConfigs1) || transferDatabaseConfigs1.size() < 2) {
                logger.info("taskId: {} 获取到的数据库配置不符合两条的规定，源数据库或者目标数据库配置不存在！跳过当前任务...", transferDataTask.getTaskId());
                continue;
            }
            transferContext.setTransferDatabaseConfig(transferDatabaseConfigs1);
            transferContext.setTransferDataTask(transferDataTask);

            transferSelectConfigs.forEach(it -> {
                if (transferDataTask.getTaskId().equals(it.getTaskId())) {
                    transferContext.setTransferSelectConfig(it);
                }
            });

            transferInsertConfigs.forEach(it -> {
                if (transferDataTask.getTaskId().equals(it.getTaskId())) {
                    transferContext.setTransferInsertConfig(it);
                }
            });

            list.add(transferContext);
        }

        return list;
    }

}
