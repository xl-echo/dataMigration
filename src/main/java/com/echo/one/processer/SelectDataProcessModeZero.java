package com.echo.one.processer;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.echo.one.common.base.TransferContext;
import com.echo.one.common.constant.DataMigrationConstant;
import com.echo.one.common.enums.DatabaseDirection;
import com.echo.one.common.enums.TaskStatus;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.po.TransferLog;
import com.echo.one.po.TransferSelectConfig;
import com.echo.one.service.TransferLogService;
import com.echo.one.utils.jdbc.JdbcUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * @author echo
 * @date 2022/11/22 18:08
 */
@Scope(value = "prototype")
@Component(value = "process.selectData.0")
public class SelectDataProcessModeZero extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(SelectDataProcessModeZero.class);

    @Autowired
    private TransferLogService transferLogService;

    @Override
    protected void handler(TransferContext transferContext) {
        logger.info("traceId: {}, 开始执行mode为0的查询过程", transferContext.getTraceId());

        // JDBC获取数据库链接
        TransferDatabaseConfig databaseConfig = getDatabaseConfig(transferContext);
        logger.info("traceId: {}, 开始使用JDBC链接数据库，传入的数据库配置参数为：{}", transferContext.getTraceId(), databaseConfig);
        DruidDataSource druidDataSource = getDruidDataSource(databaseConfig.getDatabaseUrl(), databaseConfig.getDatabaseUsername(), databaseConfig.getDatabasePassword());
        DruidPooledConnection connection = getConnection(transferContext.getTraceId(), druidDataSource);
        if (connection == null) {
            logger.info("traceId: {}, JDBC链接失败，终止执行程序!!!", transferContext.getTraceId());
            throw new DataMigrationException("JDBC链接失败，终止执行程序!!!");
        }

        // 拼接查询SQL
        TransferSelectConfig transferSelectConfig = transferContext.getTransferSelectConfig();
        String countSql = "select count(1) as count from " + transferSelectConfig.getTableName() + " where " + transferSelectConfig.getTableWhere();
        logger.info("traceId: {}, 拼接统计本次迁移数总条数的SQL: {}", transferContext.getTraceId(), countSql);

        // 获取本次迁移数据的总条数
        String count = executeQueryReturn(connection, countSql);
        if (StringUtils.isEmpty(count) || DataMigrationConstant.ZERO.equals(count)) {
            logger.info("traceId: {}, 本次迁移无需要迁移的数据, 程序结束执行!!!", transferContext.getTraceId());
            return;
        }

        // 拼接迁移SQL
        String sql = joinSql(transferContext);

        // 执行迁移
        boolean status = selectTransferData(sql, transferContext.getTraceId(), druidDataSource);

        // 更新日志
        oprLog(transferLogService, transferContext.getTransferDataTask().getTaskId(), transferContext.getTraceId(), Integer.parseInt(count), status);

    }

    private String executeQueryReturn(Connection conn, String sql) {
        String count = "";
        ResultSet resultSet;
        Statement stmt;
        try {
            stmt = JdbcUtils.getStmt(conn);
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                count = resultSet.getString("count");
            }
        } catch (SQLException e) {
            logger.info("Statement执行统计SQL: {}, 执行异常, 异常信息: {}", sql, e.getMessage());
            throw new DataMigrationException("执行统计SQL异常，异常信息: " + e.getMessage());
        }

        JdbcUtils.closeRs(resultSet);
        JdbcUtils.closeStmt(stmt);
        return count;
    }

    private void oprLog(TransferLogService transferLogService, String taskId, String traceId, int taskTransferDataSize, boolean status) {
        TransferLog transferLog = TransferLog.builder()
                .taskId(taskId)
                .traceId(traceId)
                .taskTransferDataSize(taskTransferDataSize)
                .status(status ? TaskStatus.FAIL.getCode() : TaskStatus.SUCCESS.getCode())
                .updateUname("sys")
                .updateTime(new Date())
                .build();
        transferLogService.updateLog(transferLog);
    }

    private boolean selectTransferData(String sql, String traceId, DruidDataSource druidDataSource) {
        boolean execute;
        Statement stmt = null;
        Connection newConn = null;
        try {
            newConn = druidDataSource.getConnection();
            stmt = newConn.createStatement();
            execute = stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("traceId: {}, mysql executeQuery error!!! error msg: {}", traceId, e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_EXECUTE_QUERY_ERROR);
        } catch (Exception e) {
            logger.error("traceId: {}, 获取查询结果集异常!!! error msg: {}", traceId, e.getMessage());
            throw new DataMigrationException("获取查询结果集异常, 异常信息：" + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (newConn != null) {
                    newConn.close();
                }
            } catch (Exception e) {
                logger.error("traceId: {}, close io error!!! error msg: {}", traceId, e.getMessage());
            }
        }
        return execute;
    }

    private String joinSql(TransferContext transferContext) {
        TransferSelectConfig transferSelectConfig = transferContext.getTransferSelectConfig();
        return "insert into(select " + transferSelectConfig.getTableCloumnName() +
                " from " +
                transferSelectConfig.getTableName() +
                " where " +
                transferSelectConfig.getTableWhere()
                + ")"
                ;
    }

    private DruidDataSource getDruidDataSource(String url, String userName, String passWord) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        return dataSource;
    }

    private DruidPooledConnection getConnection(String traceId, DruidDataSource druidDataSource) {
        DruidPooledConnection connection = null;
        try {
            connection = druidDataSource.getConnection();
        } catch (Exception e) {
            logger.info("traceId: {}, 通过druid获取链接失败, 失败信息: {}", traceId, e.getMessage());
        }
        return connection;
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
