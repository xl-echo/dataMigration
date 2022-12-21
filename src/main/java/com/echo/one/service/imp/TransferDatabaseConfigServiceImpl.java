package com.echo.one.service.imp;

import com.echo.one.dao.TransferDatabaseConfigMapper;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.service.TransferDatabaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:48
 */
@Service
public class TransferDatabaseConfigServiceImpl implements TransferDatabaseConfigService {

    @Autowired
    private TransferDatabaseConfigMapper transferDatabaseConfigMapper;

    @Override
    public List<TransferDatabaseConfig> getDatabaseConfig() {
        return transferDatabaseConfigMapper.getDatabaseConfig();
    }

    @Override
    public List<TransferDatabaseConfig> getBatchDatabaseConfig(List<String> taskIds) {
        return transferDatabaseConfigMapper.getBatchDatabaseConfig(taskIds);
    }
}
