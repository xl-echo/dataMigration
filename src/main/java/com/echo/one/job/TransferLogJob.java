package com.echo.one.job;

import com.echo.one.service.TransferHealthyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author echo
 * @date 2022/12/6 13:18
 */
@Component
public class TransferLogJob {

    private static final Logger logger = LoggerFactory.getLogger(TransferLogJob.class);

    @Autowired
    private TransferHealthyService transferHealthyService;

    @Scheduled(cron = "${task.job.transfer.log}")
    public void syncUpdateLog() {
        String healthyFlag = transferHealthyService.getTaskTransfer();
        logger.info("当前服务状态检查执行结果：{}", healthyFlag);
    }

}
