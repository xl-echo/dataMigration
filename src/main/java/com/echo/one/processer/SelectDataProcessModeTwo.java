package com.echo.one.processer;

import com.echo.one.common.base.TransferContext;
import com.echo.one.common.enums.DatabaseDirection;
import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.po.TransferLog;
import com.echo.one.service.TransferLogService;
import com.echo.one.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * 使用mysqldump文件导出sql（要求必须能够准确的知道dump文件位置）
 *
 * @author echo
 * @date 2022/11/23 9:41
 */
@Scope(value = "prototype")
@Component(value = "process.selectData.2")
public class SelectDataProcessModeTwo extends ProcessMode {

    private static final Logger logger = LoggerFactory.getLogger(SelectDataProcessModeTwo.class);

    @Autowired
    private TransferLogService transferLogService;

    @Override
    protected void handler(TransferContext transferContext) {
        logger.info("traceId: {}, 开始执行mode为1的查询过程", transferContext.getTraceId());

        // 获取数据库配置
        TransferDatabaseConfig databaseConfig = getDatabaseConfig(transferContext);

        try {
            Runtime rt = Runtime.getRuntime();
            // 调用 调用mysql的安装目录的命令，若知道mysql安装目录，则“usr/local/mysql/bin/mysqldump”，如果是阿里一类RDS，就直接mysqldump
            Process child = rt.exec("/usr/local/mysql/bin/mysqldump -u" + databaseConfig.getDatabaseUsername() + "  -p" + databaseConfig.getDatabasePassword() + " -h" + databaseConfig.getDatabaseUrl() + " " + databaseConfig.getDatabaseUrl());
            // 设置导出编码为utf-8。这里必须是utf-8
            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
            InputStream in = child.getInputStream();// 控制台的输出信息作为输入流
            InputStreamReader xx = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
            String inStr;
            StringBuffer sb = new StringBuffer("");
            String outStr;
            // 组合控制台输出信息字符串
            BufferedReader br = new BufferedReader(xx);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            outStr = sb.toString();
            // 要用来做导入用的sql目标文件：
            FileOutputStream fout;
            Long startTs = System.currentTimeMillis();
            StringBuffer p = new StringBuffer(getSavePath(transferContext.getTraceId()));
            // 插入的位置，可按照需求修改
            p.insert(31, startTs.toString());
            fout = new FileOutputStream(p.toString());
            OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(outStr);
            writer.flush();
            in.close();
            xx.close();
            br.close();
            writer.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 更新日志
        oprLog(transferLogService, transferContext.getTransferDataTask().getTaskId(), transferContext.getTraceId(), 0, 0);

    }

    /**
     * 获取文件名称
     * 名称规则：/tempFile/12位的日期/traceId/traceId.txt
     *
     * @return fileName
     */
    private String getSavePath(String traceId) {
        String fileName = "";
        fileName = fileName + "/tempFile/";
        fileName = fileName + DateUtils.getCurDate8() + "/";
        fileName = fileName + traceId + "/";
        fileName = fileName + traceId;
        fileName = fileName + ".txt";
        String property = System.getProperty("java.io.tmpdir");
        return property + fileName;
    }

    private void oprLog(TransferLogService transferLogService, String taskId, String traceId, int taskTransferDataSize, int taskBatchNum) {
        TransferLog transferLog = TransferLog.builder()
                .taskId(taskId)
                .traceId(traceId)
                .taskTransferDataSize(taskTransferDataSize)
                .taskBatchNum(taskBatchNum)
                .taskFileNum(taskBatchNum)
                .updateUname("sys")
                .updateTime(new Date())
                .build();
        transferLogService.updateLog(transferLog);
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
