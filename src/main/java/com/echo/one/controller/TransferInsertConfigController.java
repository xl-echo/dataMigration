package com.echo.one.controller;

import com.echo.one.common.enums.StatusCode;
import com.echo.one.common.framework.result.Result;
import com.echo.one.po.TransferDataTask;
import com.echo.one.po.TransferInsertConfig;
import com.echo.one.service.TransferInsertConfigService;
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
@RequestMapping(value = "/insert/config")
public class TransferInsertConfigController {

    @Autowired
    private TransferInsertConfigService transferInsertConfigService;

    @GetMapping(value = "/taskTransfer")
    public Result<List<TransferInsertConfig>> getTaskTransfer() {
        Result<List<TransferInsertConfig>> result = new Result<>();

        List<TransferInsertConfig> list = transferInsertConfigService.getTransferInsertConfig();

        result.setStatus(StatusCode.SUCCESS.getCode());
        result.setData(list);
        return result;
    }

}
