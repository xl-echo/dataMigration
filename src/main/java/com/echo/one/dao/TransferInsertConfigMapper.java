package com.echo.one.dao;

import com.echo.one.po.TransferInsertConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:51
 */
public interface TransferInsertConfigMapper {

    List<TransferInsertConfig> getTransferInsertConfig();

    List<TransferInsertConfig> getBatchTransferInsertConfig(@Param("taskIds") List<String> taskIds);

}
