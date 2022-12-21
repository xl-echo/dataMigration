package com.echo.one.dao;

import com.echo.one.po.TransferSelectConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:52
 */
public interface TransferSelectConfigMapper {

    List<TransferSelectConfig> getTransferSelectConfig();

    List<TransferSelectConfig> getBatchTransferSelectConfig(@Param("taskIds") List<String> taskIds);
}
