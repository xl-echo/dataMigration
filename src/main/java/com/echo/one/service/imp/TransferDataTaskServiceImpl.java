package com.echo.one.service.imp;

import com.echo.one.dao.TransferDataTaskMapper;
import com.echo.one.po.TransferDataTask;
import com.echo.one.service.TransferDataTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 16:25
 */
@Service
public class TransferDataTaskServiceImpl implements TransferDataTaskService {

    @Autowired
    private TransferDataTaskMapper TransferDataTaskMapper;

    @Override
    public List<TransferDataTask> getTaskTransfer() {
        return TransferDataTaskMapper.getTaskTransfer();
    }
    @Override
    public List<TransferDataTask> healthyCheck() {
        return TransferDataTaskMapper.healthyCheck();
    }
}
