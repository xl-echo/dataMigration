package com.echo.one.dao;

import com.echo.one.po.TransferDatabaseConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:51
 */
public interface TransferDatabaseConfigMapper {

    List<TransferDatabaseConfig> getDatabaseConfig();

    List<TransferDatabaseConfig> getBatchDatabaseConfig(@Param("taskIds") List<String> taskIds);

}
