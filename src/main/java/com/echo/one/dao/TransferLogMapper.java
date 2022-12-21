package com.echo.one.dao;

import com.echo.one.po.TransferLog;

import java.util.List;
import java.util.Map;

/**
 * @author echo
 * @date 2022/11/10 17:52
 */
public interface TransferLogMapper {

    void insertSelective(TransferLog transferLog);

    void updateSelective(TransferLog transferLog);

    List<TransferLog> getTaskTransfer();

    void updateByTraceIdLog(TransferLog transferLog);

    TransferLog getByTraceIdTaskTransfer(String traceId);

    TransferLog getLog(Map<String, Object> map);

}
