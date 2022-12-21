package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.enums.DatabaseDirection;
import com.echo.one.common.enums.TaskStatus;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.job.DataTaskThread;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.po.TransferLog;
import com.echo.one.po.TransferSelectConfig;
import com.echo.one.service.TransferLogService;
import com.echo.one.utils.jdbc.JdbcUtils;
import com.echo.one.utils.thread.ExecutorServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.deleteData.1")
public class DeleteDataProcessModeOne extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(DeleteDataProcessModeOne.class);
    @Autowired
    private TransferLogService transferLogService;

    @Override
    protected void handler(TransferContext transferContext) {
        logger.info("traceId: {}, 开始执行mode为1的删除process", transferContext.getTraceId());

        // JDBC获取数据库链接
        TransferDatabaseConfig databaseConfig = getDatabaseConfig(transferContext);

        // 拼接删除SQL
        TransferSelectConfig transferSelectConfig = transferContext.getTransferSelectConfig();
        String deleteSql = "delete from " + transferSelectConfig.getTableName() + " where " + transferSelectConfig.getTableWhere() + " limit " + transferSelectConfig.getLimitSize();

        // 执行删除
        execDelete(databaseConfig, deleteSql);

        // 继续轮训执行删除
        loopExec(databaseConfig, transferContext);

    }

    private void oprLog(TransferLogService transferLogService, String taskId, String traceId) {
        TransferLog transferLog = TransferLog.builder()
                .taskId(taskId)
                .traceId(traceId)
                .taskEndDate(new Date())
                .status(TaskStatus.SUCCESS.getCode())
                .updateUname("sys")
                .updateTime(new Date())
                .build();
        transferLogService.updateLog(transferLog);
    }

    private void loopExec(TransferDatabaseConfig databaseConfig, TransferContext transferContext) {

        // 判断源表是否还有需要迁移的数据
        TransferSelectConfig transferSelectConfig = transferContext.getTransferSelectConfig();
        String countSql = "select count(1) as count from " + transferSelectConfig.getTableName() + " where " + transferSelectConfig.getTableWhere();
        int count = executeQueryReturn(databaseConfig, countSql, transferContext.getTraceId());

        if (count <= 0) {
            // 更新日志
            oprLog(transferLogService, transferContext.getTransferDataTask().getTaskId(), transferContext.getTraceId());
            logger.info("traceId: {}, 本次迁移完成---------------------------->", transferContext.getTraceId());
            return;
        }

        ExecutorService instance = ExecutorServiceUtil.getInstance();
        instance.execute(new DataTaskThread(transferContext));
    }

    public int executeQueryReturn(TransferDatabaseConfig databaseConfig, String sql, String traceId) {
        int count = 0;
        try (
                Connection conn = JdbcUtils.getConn(databaseConfig.getDatabaseUrl(), databaseConfig.getDatabaseUsername(), databaseConfig.getDatabasePassword());
                Statement stmt = JdbcUtils.getStmt(conn);
                ResultSet resultSet = stmt.executeQuery(sql);
        ) {
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception e) {
            logger.info("traceId: {}, Statement执行统计SQL: {}, 执行异常, 异常信息: {}", traceId, sql, e.getMessage());
            throw new DataMigrationException("执行统计SQL异常，异常信息: " + e.getMessage());
        }
        return count;
    }

    private void execDelete(TransferDatabaseConfig databaseConfig, String deleteSql) {
        try (Connection newConn = JdbcUtils.getConn(databaseConfig.getDatabaseUrl(), databaseConfig.getDatabaseUsername(), databaseConfig.getDatabasePassword());
             Statement stmt = newConn.createStatement();
        ) {
            stmt.execute(deleteSql);
        } catch (Exception e) {

        }
    }

    /**
     * 获取数据库链接配置
     *
     * @return TransferDatabaseConfig
     */
    private TransferDatabaseConfig getDatabaseConfig(TransferContext transferContext) {
        logger.info("traceId: {}, 开始获取JDBC链接数据库配置参数", transferContext.getTraceId());
        TransferDatabaseConfig transferDatabaseConfig = null;
        List<TransferDatabaseConfig> transferDatabaseConfigs = transferContext.getTransferDatabaseConfig();
        for (int i = 0; i < transferDatabaseConfigs.size(); i++) {
            if (DatabaseDirection.SOURCE.getCode().equals(transferDatabaseConfigs.get(i).getDatabaseDirection())) {
                transferDatabaseConfig = transferDatabaseConfigs.get(i);
            }
        }

        if (transferDatabaseConfig == null) {
            logger.info("traceId: {}, 获取JDBC链接数据库配置参数出错，未发现'source'方向的数据库配置", transferContext.getTraceId());
            throw new DataMigrationException("获取JDBC链接数据库配置参数出错，未发现'source'方向的数据库配置");
        }

        return transferDatabaseConfig;
    }

}
