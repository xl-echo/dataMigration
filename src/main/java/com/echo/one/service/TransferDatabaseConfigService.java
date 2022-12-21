package com.echo.one.service;

import com.echo.one.po.TransferDatabaseConfig;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:48
 */
public interface TransferDatabaseConfigService {

    List<TransferDatabaseConfig> getDatabaseConfig();

    List<TransferDatabaseConfig> getBatchDatabaseConfig(List<String> taskIds);

}
