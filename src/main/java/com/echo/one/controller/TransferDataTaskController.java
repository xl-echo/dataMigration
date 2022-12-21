package com.echo.one.controller;

import com.echo.one.common.enums.StatusCode;
import com.echo.one.common.framework.result.Result;
import com.echo.one.po.TransferDataTask;
import com.echo.one.service.TransferDataTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/3 16:29
 */
@RestController
@RequestMapping(value = "/data/task")
public class TransferDataTaskController {

    @Autowired
    private TransferDataTaskService transferDataTaskService;

    @GetMapping(value = "/taskTransfer")
    public Result<List<TransferDataTask>> getTaskTransfer() {
        Result<List<TransferDataTask>> result = new Result<>();
        transferDataTaskService.healthyCheck();
        result.setStatus(StatusCode.SUCCESS.getCode());
        return result;
    }

}
