package com.echo.one.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author echo
 * @date 2022/11/4 11:05
 */
@Getter
@ToString
@AllArgsConstructor
public enum FormatPattern {

    PATTERN_YYYY("yyyy"),
    PATTERN_YYYY_MM("yyyy-MM"),
    PATTERN_YYYY_MM_DD("yyyy-MM-dd"),
    PATTERN_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    ;

    private String desc;
}
