package com.echo.one.controller;

import com.echo.one.common.enums.StatusCode;
import com.echo.one.common.framework.result.Result;
import com.echo.one.po.TransferInsertConfig;
import com.echo.one.po.TransferLog;
import com.echo.one.service.TransferLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:45
 */
@RestController
@RequestMapping(value = "/log")
public class TransferLogController {

    @Autowired
    private TransferLogService transferLogService;

    @GetMapping(value = "/taskTransfer")
    public Result<List<TransferLog>> getTaskTransfer() {
        Result<List<TransferLog>> result = new Result<>();

        List<TransferLog> list = transferLogService.getTaskTransfer();

        result.setStatus(StatusCode.SUCCESS.getCode());
        result.setData(list);
        return result;
    }
}
