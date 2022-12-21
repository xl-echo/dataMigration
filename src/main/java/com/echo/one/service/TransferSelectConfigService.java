package com.echo.one.service;

import com.echo.one.po.TransferSelectConfig;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:49
 */
public interface TransferSelectConfigService {

    List<TransferSelectConfig> getTransferSelectConfig();

    List<TransferSelectConfig> getBatchTransferSelectConfig(List<String> taskIds);

}
