package com.echo.one.service;

import com.echo.one.po.TransferDataTask;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 16:24
 */
public interface TransferDataTaskService {

    List<TransferDataTask> getTaskTransfer();

    List<TransferDataTask> healthyCheck();

}
