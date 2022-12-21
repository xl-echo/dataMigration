package com.echo.one.service.imp;

import com.echo.one.service.TransferHealthyService;
import org.springframework.stereotype.Service;

/**
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/20 10:15
 */
@Service
public class TransferHealthyServiceImpl implements TransferHealthyService {

    @Override
    public String getTaskTransfer() {
        return "service status: healthy!";
    }

}
