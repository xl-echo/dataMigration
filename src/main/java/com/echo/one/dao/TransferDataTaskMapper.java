package com.echo.one.dao;

import com.echo.one.po.TransferDataTask;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 16:25
 */
public interface TransferDataTaskMapper {

    List<TransferDataTask> getTaskTransfer();


    List<TransferDataTask> healthyCheck();

}
