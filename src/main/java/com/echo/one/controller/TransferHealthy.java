package com.echo.one.controller;

import com.echo.one.common.enums.StatusCode;
import com.echo.one.common.framework.result.Result;
import com.echo.one.service.TransferHealthyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/20 10:12
 */
@RestController
@RequestMapping(value = "/transfer")
public class TransferHealthy {

    @Autowired
    private TransferHealthyService transferHealthyService;

    @GetMapping(value = "/taskTransfer")
    public Result<String> healthyCheck() {
        Result<String> result = new Result<>();

        String healthyFlag = transferHealthyService.getTaskTransfer();

        result.setStatus(StatusCode.SUCCESS.getCode());
        result.setData(healthyFlag);
        return result;
    }

}
