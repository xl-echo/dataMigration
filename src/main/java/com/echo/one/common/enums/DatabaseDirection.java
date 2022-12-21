package com.echo.one.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author echo
 * @date 2022/11/23 13:21
 */
@Getter
@ToString
@AllArgsConstructor
public enum DatabaseDirection {

    TARGET("target", "源数据库"),
    SOURCE("source", "目标数据库");

    private String code;
    private String msg;

}
