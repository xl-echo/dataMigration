package com.echo.one.controller;

import com.echo.one.common.enums.StatusCode;
import com.echo.one.common.framework.result.Result;
import com.echo.one.po.TransferLog;
import com.echo.one.po.TransferSelectConfig;
import com.echo.one.service.TransferSelectConfigService;
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
@RequestMapping(value = "/select/config")
public class TransferSelectConfigController {

    @Autowired
    private TransferSelectConfigService transferSelectConfigService;

    @GetMapping(value = "/taskTransfer")
    public Result<List<TransferSelectConfig>> getTaskTransfer() {
        Result<List<TransferSelectConfig>> result = new Result<>();

        List<TransferSelectConfig> list = transferSelectConfigService.getTransferSelectConfig();

        result.setStatus(StatusCode.SUCCESS.getCode());
        result.setData(list);
        return result;
    }
}
