package com.echo.one.processer;

import com.echo.one.common.exception.DataMigrationException;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.po.TransferInsertConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author echo
 * @date 2022/11/23 9:41
 */
public class InsertDataProcessOneThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(InsertDataProcessOneThread.class);

    private String fileName;
    private TransferInsertConfig transferInsertConfig;
    private TransferDatabaseConfig databaseConfig;
    private String traceId;

    public InsertDataProcessOneThread(String fileName, TransferInsertConfig transferInsertConfig, String traceId, TransferDatabaseConfig databaseConfig) {
        this.fileName = fileName;
        this.transferInsertConfig = transferInsertConfig;
        this.traceId = traceId;
        this.databaseConfig = databaseConfig;
    }

    @Override
    public void run() {
        logger.info("traceId: {}, 开始执行插入任务，对应插入文件名称为：{}!", traceId, fileName);

        // 获取获取对应文件中的数据
        List<String> list = selectTransferData();

        // 拼接SQL
        List<String> sqlList = joinSql(list);

        // 写库
        inputDataBase(sqlList);

    }

    private List<String> joinSql(List<String> list) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) return sqlList;

        int num = transferInsertConfig.getLimitSize();
        String insertWith = "INSERT INTO " + transferInsertConfig.getTableName() + "(" + transferInsertConfig.getTableCloumnName() + ") VALUES ";
        StringBuffer sb = new StringBuffer(insertWith);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0 || i % num == 0) {
                sqlList.add(sb.substring(0, sb.length() - 2));
                sb = new StringBuffer(insertWith);
            }
            sb.append(list.get(i));
            sb.append(", ");
        }
        return sqlList;
    }

    private void inputDataBase(List<String> list) {
        if (CollectionUtils.isEmpty(list)) return;

        list.forEach(it -> logger.info("traceId: {}, 拼接出来需要插入的SQL为：{}", traceId, it));
    }

    /**
     * 获取指定文件下面的内容，封装成为集合
     *
     * @return List<String>
     */
    private List<String> selectTransferData() {
        List<String> list = new ArrayList<>();

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            logger.info("traceId: {}, 开始执行插入任务，对应插入文件名称为：{}!", traceId, fileName);
            throw new DataMigrationException("读取文件内容出错，终止执行！");
        }

        return list;
    }

}
