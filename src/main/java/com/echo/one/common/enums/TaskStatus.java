package com.echo.one.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author echo
 * @date 2022/12/4 0:41
 */
@Getter
@ToString
@AllArgsConstructor
public enum TaskStatus {

    /**
     * 成功
     */
    FAIL("0", "任务失败"),
    SUCCESS("1", "任务处理成功"),
    ING("2", "任务处理中");

    private String code;
    private String msg;

}
