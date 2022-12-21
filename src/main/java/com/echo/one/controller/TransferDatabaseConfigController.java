package com.echo.one.controller;

import com.echo.one.common.enums.StatusCode;
import com.echo.one.common.framework.result.Result;
import com.echo.one.po.TransferDataTask;
import com.echo.one.po.TransferDatabaseConfig;
import com.echo.one.service.TransferDatabaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author echo
 * @date 2022/11/10 17:40
 */
@RestController
@RequestMapping(value = "/database/config")
public class TransferDatabaseConfigController {

    @Autowired
    private TransferDatabaseConfigService transferDatabaseConfigService;

    @GetMapping(value = "/getDatabaseConfig")
    public Result<List<TransferDatabaseConfig>> getDatabaseConfig() {
        Result<List<TransferDatabaseConfig>> result = new Result<>();

        List<TransferDatabaseConfig> list = transferDatabaseConfigService.getDatabaseConfig();

        result.setStatus(StatusCode.SUCCESS.getCode());
        result.setData(list);
        return result;
    }

}
