package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.base.TransferDataContext;
import com.echo.one.common.constant.DataMigrationConstant;
import com.echo.one.common.enums.DatabaseColumnType;
import com.echo.one.common.enums.DatabaseDirection;
import com.echo.one.common.enums.TaskStatus;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.common.factory.DataMigrationBeanFactory;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.po.TransferInsertConfig;
import com.echo.one.po.TransferLog;
import com.echo.one.po.TransferSelectConfig;
import com.echo.one.service.TransferLogService;
import com.echo.one.utils.DateUtils;
import com.echo.one.utils.jdbc.JdbcUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author echo
 * @date 2022/11/23 9:41
 */
@Scope(value = "prototype")
@Component(value = "process.selectData.3")
public class SelectDataTransactionalProcessModeThree extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(SelectDataTransactionalProcessModeThree.class);

    @Autowired
    private TransferLogService transferLogService;
    @Autowired
    private DataMigrationBeanFactory dataMigrationBeanFactory;

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为3的查询过程", traceId);

        // JDBC获取数据库链接
        TransferDatabaseConfig databaseConfig = getDatabaseConfig(transferContext);
        logger.info("traceId: {}, 开始使用JDBC链接数据库，传入的数据库配置参数为：{}", traceId, databaseConfig);
        Connection connection = JdbcUtils.getConn(databaseConfig.getDatabaseUrl(), databaseConfig.getDatabaseUsername(), databaseConfig.getDatabasePassword());
        if (connection == null) {
            logger.info("traceId: {}, JDBC链接失败，终止执行程序!!!", traceId);
            throw new DataMigrationException("JDBC链接失败，终止执行程序!!!");
        }

        // 拼接查询SQL
        TransferSelectConfig transferSelectConfig = transferContext.getTransferSelectConfig();
        String countSql = "select count(1) as count from " + transferSelectConfig.getTableName() + " where " + transferSelectConfig.getTableWhere();
        logger.info("traceId: {}, 拼接统计本次迁移数总条数的SQL: {}", traceId, countSql);

        // 获取本次迁移数据的总条数
        String count = executeQueryReturn(connection, countSql);
        if (StringUtils.isEmpty(count) || DataMigrationConstant.ZERO.equals(count)) {
            logger.info("traceId: {}, 本次迁移无需要迁移的数据, 程序结束执行!!!", traceId);
            oprLogSuccess(transferLogService, transferContext.getTransferDataTask().getTaskId(), traceId);
            throw new DataMigrationException("当前任务没有需要迁移的数据，程序结束执行------------------------------------------------------>");
        }
        logger.info("traceId: {}, 本次迁移剩余需要迁移 {} 条数据", traceId, count);
        JdbcUtils.closeConn(connection);

        // 分批次查询数据
        String selectSqlList = getSelectSqlList(transferSelectConfig);
        logger.info("traceId: {}, 本次迁移拼接SQL为：{}", traceId, selectSqlList);

        // 执行sql，拼接查询出来的数据
        List<String> maps = selectTransferData(databaseConfig, selectSqlList, traceId, transferContext.getTransferInsertConfig());

        // 将查询出来的数据, 拼接成为批量的insert
        List<String> bachtSql = joinBatchSql(maps, transferContext.getTransferInsertConfig());

        // 将数据注入bean
        registerBean(traceId, bachtSql);

        // 更新日志
        oprLog(transferLogService, transferContext.getTransferDataTask().getTaskId(), traceId, count);

    }

    /**
     * 将查询出来的数据, 拼接成为批量的insert
     *
     * @param maps 查询出来的数据集
     * @return list
     */
    private List<String> joinBatchSql(List<String> maps, TransferInsertConfig transferInsertConfig) {
        if (CollectionUtils.isEmpty(maps)) return null;

        List<String> batchSql = new ArrayList<>(maps.size());
        String[] cloumnSplit = transferInsertConfig.getTableCloumnName().split(",");
        String sqlHead = joinSqlHead(transferInsertConfig, cloumnSplit);
        StringBuilder sb = new StringBuilder(sqlHead);
        for (int i = 0; i < maps.size(); i++) {
            if (i != 0 && i % transferInsertConfig.getLimitSize() == 0) {
                batchSql.add(sb.substring(0, sb.length() - 1));
                sb = new StringBuilder(sqlHead);
            }
            sb.append(maps.get(i)).append(",");
        }
        batchSql.add(sb.substring(0, sb.length() - 1));
        return batchSql;
    }

    /**
     * 拼接sql头
     *
     * @param transferInsertConfig 目标库配置
     * @param cloumnSplit          字段集
     * @return String
     */
    private String joinSqlHead(TransferInsertConfig transferInsertConfig, String[] cloumnSplit) {
        StringBuilder sqlHead = new StringBuilder("insert into ");
        sqlHead.append(transferInsertConfig.getTableName());
        sqlHead.append(" (");
        Arrays.asList(cloumnSplit).forEach(it -> sqlHead.append(it).append(", "));
        return sqlHead.substring(0, sqlHead.length() - 2) + ") values ";
    }

    /**
     * 将数据注入bean
     *
     * @param traceId beanId
     * @param list    数据
     */
    private void registerBean(String traceId, List<String> list) {
        TransferDataContext transferDataContext = dataMigrationBeanFactory.produceAndRegisterBean(TransferDataContext.class, traceId);
        transferDataContext.setList(list);
    }

    /**
     * 执行sql，拼接查询出来的数据
     *
     * @param sql
     * @param traceId
     * @param transferInsertConfig
     * @return
     */
    private List<String> selectTransferData(TransferDatabaseConfig databaseConfig, String sql, String traceId, TransferInsertConfig transferInsertConfig) {
        long start = System.currentTimeMillis();
        logger.info("traceId: {}, 开始执行sql--------------------------------------->", traceId);
        List<String> aLineData = new ArrayList<>();
        Connection newConn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            newConn = JdbcUtils.getConn(databaseConfig.getDatabaseUrl(), databaseConfig.getDatabaseUsername(), databaseConfig.getDatabasePassword());
            stmt = newConn.createStatement();
            rs = stmt.executeQuery(sql);
            String[] cloumnSplit = transferInsertConfig.getTableCloumnName().split(",");
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                StringBuilder data = new StringBuilder("(");
                for (int i = 0; i < cloumnSplit.length; i++) {
                    String cloumnName = cloumnSplit[i].replaceAll(" ", "");
                    String string = rs.getString(cloumnName);
                    int columnType = metaData.getColumnType(i + 1);
                    boolean b = ifNumType(columnType);
                    if (b) {
                        data.append(string).append(", ");
                    } else {
                        data.append("'").append(string).append("'").append(", ");
                    }
                }
                String dataStr = data.substring(0, data.length() - 2) + ")";
                aLineData.add(dataStr);
            }
        } catch (SQLException e) {
            logger.error("traceId: {}, mysql executeQuery error!!! error msg: {}", traceId, e.getMessage());
            throw new DataMigrationException(DataMigrationConstant.MYSQL_EXECUTE_QUERY_ERROR);
        } finally {
            if (newConn != null) {
                try {
                    newConn.close();
                } catch (Exception e) {
                    logger.error("traceId: {}, 关闭conn失败!", traceId);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    logger.error("traceId: {}, 关闭stmt失败!", traceId);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    logger.error("traceId: {}, 关闭rs失败!", traceId);
                }
            }
        }
        long end = System.currentTimeMillis();
        logger.info("traceId: {}, 结束执行sql, 耗时: {} ms--------------------------------------->", traceId, end - start);
        return aLineData;
    }

    /**
     * 判断是不是数字类型
     *
     * @return
     */
    private boolean ifNumType(int columnType) {
        boolean flag = false;
        if (columnType == DatabaseColumnType.BIGINT.getNum()
                || columnType == DatabaseColumnType.TINYINT.getNum()
                || columnType == DatabaseColumnType.NUMERIC.getNum()
                || columnType == DatabaseColumnType.DECIMAL.getNum()
                || columnType == DatabaseColumnType.INTEGER.getNum()
                || columnType == DatabaseColumnType.SMALLINT.getNum()
                || columnType == DatabaseColumnType.FLOAT.getNum()
                || columnType == DatabaseColumnType.REAL.getNum()
                || columnType == DatabaseColumnType.DOUBLE.getNum()
        ) {
            flag = true;
        }
        return flag;
    }

    public String executeQueryReturn(Connection conn, String sql) {
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

    private void oprLogSuccess(TransferLogService transferLogService, String taskId, String traceId) {
        TransferLog log = transferLogService.getLog(taskId, DateUtils.getCurDate());
        if (log == null) return;
        TransferLog transferLog = TransferLog.builder()
                .taskId(taskId)
                .traceId(traceId)
                .taskTransferDataSize(0)
                .taskEndDate(new Date())
                .status(TaskStatus.SUCCESS.getCode())
                .updateUname("sys")
                .updateTime(new Date())
                .build();
        transferLogService.updateLog(transferLog);
    }

    private void oprLog(TransferLogService transferLogService, String taskId, String traceId, String count) {
        TransferLog log = transferLogService.getLog(taskId, DateUtils.getCurDate());
        if (log == null) return;

        Integer taskTransferDataSize = log.getTaskTransferDataSize();

        TransferLog transferLog;
        if (taskTransferDataSize == null) {
            transferLog = TransferLog.builder()
                    .taskId(taskId)
                    .traceId(traceId)
                    .taskTransferDataSize(Integer.parseInt(count))
                    .status(TaskStatus.ING.getCode())
                    .updateUname("sys")
                    .updateTime(new Date())
                    .build();
        } else {
            transferLog = TransferLog.builder()
                    .taskId(taskId)
                    .traceId(traceId)
                    .status(TaskStatus.ING.getCode())
                    .updateUname("sys")
                    .updateTime(new Date())
                    .build();
        }
        transferLogService.updateLog(transferLog);
    }

    /**
     * 拼接查询的sql
     *
     * @param transferSelectConfig
     * @return
     */
    private String getSelectSqlList(TransferSelectConfig transferSelectConfig) {
        return "select " + transferSelectConfig.getTableCloumnName() +
                " from " +
                transferSelectConfig.getTableName() +
                " where " +
                transferSelectConfig.getTableWhere() +
                " limit " +
                transferSelectConfig.getLimitSize();
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
