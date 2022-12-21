package com.echo.one.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author XLecho
 * Date 2019/6/20 0020
 * Time 15:52
 * @WeChat t2421499075
 */
@Getter
@ToString
@AllArgsConstructor
public enum StatusCode {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    ERROR(999, "服务器内部错误"),
    ERROR_(666, "自定义服务器内部错误编码"),
    FAIL(1000, "自定义异常");

    private int code;
    private String msg;

}
