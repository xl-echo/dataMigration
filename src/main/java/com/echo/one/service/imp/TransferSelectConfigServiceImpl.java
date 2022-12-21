package com.echo.one.service.imp;

import com.echo.one.dao.TransferSelectConfigMapper;
import com.echo.one.po.TransferSelectConfig;
import com.echo.one.service.TransferSelectConfigService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:49
 */
@Service
public class TransferSelectConfigServiceImpl implements TransferSelectConfigService {

    @Autowired
    private TransferSelectConfigMapper transferSelectConfigMapper;

    @Override
    public List<TransferSelectConfig> getTransferSelectConfig() {
        return transferSelectConfigMapper.getTransferSelectConfig();
    }

    @Override
    public List<TransferSelectConfig> getBatchTransferSelectConfig(List<String> taskIds) {
        return transferSelectConfigMapper.getBatchTransferSelectConfig(taskIds);
    }
}
