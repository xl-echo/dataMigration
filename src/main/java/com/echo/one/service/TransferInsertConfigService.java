package com.echo.one.service;

import com.echo.one.po.TransferInsertConfig;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:48
 */
public interface TransferInsertConfigService {

    List<TransferInsertConfig> getTransferInsertConfig();

    List<TransferInsertConfig> getBatchTransferInsertConfig(List<String> taskIds);
}
