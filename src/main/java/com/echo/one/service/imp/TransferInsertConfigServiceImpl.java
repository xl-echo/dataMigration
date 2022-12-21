package com.echo.one.service.imp;

import com.echo.one.dao.TransferInsertConfigMapper;
import com.echo.one.po.TransferInsertConfig;
import com.echo.one.service.TransferInsertConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:49
 */
@Service
public class TransferInsertConfigServiceImpl implements TransferInsertConfigService {

    @Autowired
    private TransferInsertConfigMapper transferInsertConfigMapper;

    @Override
    public List<TransferInsertConfig> getTransferInsertConfig() {
        return transferInsertConfigMapper.getTransferInsertConfig();
    }

    @Override
    public List<TransferInsertConfig> getBatchTransferInsertConfig(List<String> taskIds) {
        return transferInsertConfigMapper.getBatchTransferInsertConfig(taskIds);
    }
}
