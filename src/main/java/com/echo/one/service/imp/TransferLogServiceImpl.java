package com.echo.one.service.imp;

import com.echo.one.dao.TransferLogMapper;
import com.echo.one.po.TransferLog;
import com.echo.one.service.TransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author echo
 * @date 2022/11/10 17:49
 */
@Service
public class TransferLogServiceImpl implements TransferLogService {

    @Autowired
    private TransferLogMapper transferLogMapper;

    @Override
    public List<TransferLog> getTaskTransfer() {
        return transferLogMapper.getTaskTransfer();
    }

    @Override
    public void insertLog(TransferLog transferLog) {
        transferLogMapper.insertSelective(transferLog);
    }

    @Override
    public void updateLog(TransferLog transferLog) {
        transferLogMapper.updateSelective(transferLog);
    }

    @Override
    public void updateByTraceIdLog(TransferLog transferLog) {
        transferLogMapper.updateByTraceIdLog(transferLog);
    }

    @Override
    public TransferLog getByTraceIdTaskTransfer(String traceId) {
        return transferLogMapper.getByTraceIdTaskTransfer(traceId);
    }

    @Override
    public TransferLog getLog(String taskId, String date) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskId);
        map.put("date", date);
        return transferLogMapper.getLog(map);
    }
}
