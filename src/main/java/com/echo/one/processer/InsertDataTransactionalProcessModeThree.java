package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.base.TransferDataContext;
import com.echo.one.common.enums.DatabaseDirection;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.utils.DateUtils;
import com.echo.one.utils.SpringContextUtils;
import com.echo.one.utils.jdbc.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author echo
 * @date 2022/11/23 13:06
 */
@Scope(value = "prototype")
@Component(value = "process.insertData.3")
public class InsertDataTransactionalProcessModeThree extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(InsertDataTransactionalProcessModeThree.class);

    @Override
    protected void handler(TransferContext transferContext) {
        String traceId = transferContext.getTraceId();
        logger.info("traceId: {}, 开始执行mode为3的插入process", transferContext.getTraceId());

        // 获取数据库配置信息
        TransferDatabaseConfig databaseConfig = getDatabaseConfig(transferContext);

        // 获取数据
        TransferDataContext bean = SpringContextUtils.getBean(traceId, TransferDataContext.class);
        if (null == bean) {
            logger.info("traceId: {}, insert类执行: 未获取到容器类注入的数据类!", traceId);
            return;
        }

        // 开始执行插入数据
        insertData(bean, databaseConfig, traceId);

    }

    public void insertData(TransferDataContext bean, TransferDatabaseConfig databaseConfig, String traceId) {
        List<String> list = bean.getList();
        if (CollectionUtils.isEmpty(list)) return;

        Connection newConn = null;
        Statement stmt = null;
        try {
            newConn = JdbcUtils.getConn(databaseConfig.getDatabaseUrl(), databaseConfig.getDatabaseUsername(), databaseConfig.getDatabasePassword());
            newConn.setAutoCommit(false);
            stmt = newConn.createStatement();
            for (String s : list) {
                stmt.execute(s);
            }
        } catch (Exception e) {
            logger.info("traceId: {}, insert类执行: 插入数据报错, 错误信息: {}", traceId, e.getMessage());
            throw new DataMigrationException("插入数据报错,错误信息: " + e.getMessage());
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
            if (DatabaseDirection.TARGET.getCode().equals(transferDatabaseConfigs.get(i).getDatabaseDirection())) {
                transferDatabaseConfig = transferDatabaseConfigs.get(i);
            }
        }

        if (transferDatabaseConfig == null) {
            logger.info("traceId: {}, 获取JDBC链接数据库配置参数出错，未发现'target'方向的数据库配置", transferContext.getTraceId());
            throw new DataMigrationException("获取JDBC链接数据库配置参数出错，未发现'target'方向的数据库配置");
        }

        return transferDatabaseConfig;
    }

    /**
     * 获取传入指定文件夹下面的文件名称集合
     *
     * @param traceId
     * @return
     */
    private List<String> getTxtFileNames(String traceId) {
        List<String> list = new ArrayList<>();

        String fileName = "";
        fileName = fileName + "/tempFile/";
        fileName = fileName + DateUtils.getCurDate8() + "/";
        fileName = fileName + traceId + "/";
        File srcFile = new File(fileName);

        // 判断传入的文件是不是为空
        if (srcFile == null) {
            logger.error("traceId: {}, 传入的文件为空，请检查是已经准备插入的数据！", traceId);
            throw new DataMigrationException("传入的文件夹为空！");
        }
        // 把所有目录、文件放入数组
        File[] files = srcFile.listFiles();
        // 遍历数组每一个元素
        if (files == null || files.length == 0) {
            logger.error("traceId: {}, 传入的文件夹无子文件，请检查是已经准备插入的数据！", traceId);
            return list;
        }
        for (File f : files) {
            // 判断元素是不是文件夹，是文件夹就重复调用此方法（递归）
            if (f.isDirectory()) {
                getTxtFileNames(traceId);
            } else {
                // 判断文件是不是以.txt结尾的文件，并且count++（注意：文件要显示扩展名）
                if (f.getName().endsWith(".txt")) {
                    list.add(fileName + f.getName());
                }
            }
        }
        return list;
    }

}
