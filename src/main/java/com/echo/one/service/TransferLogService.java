package com.echo.one.service;

import com.echo.one.po.TransferLog;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:49
 */
public interface TransferLogService {

    List<TransferLog> getTaskTransfer();

    void insertLog(TransferLog transferLog);

    void updateLog(TransferLog transferLog);

    void updateByTraceIdLog(TransferLog traceId);

    TransferLog getByTraceIdTaskTransfer(String traceId);

    TransferLog getLog(String taskId, String date);

}
