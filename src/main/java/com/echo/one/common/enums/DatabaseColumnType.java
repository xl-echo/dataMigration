package com.echo.one.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 表字段类型
 *
 * @author echo
 * @wechat t2421499075
 * @date 2022/12/16 12:59
 */
@Getter
@ToString
@AllArgsConstructor
public enum DatabaseColumnType {

    BIT("BIT", -7),
    TINYINT("TINYINT", -6),
    BIGINT("BIGINT", -5),
    LONGVARBINARY("LONGVARBINARY", -4),
    VARBINARY("VARBINARY", -3),
    BINARY("BINARY", -2),
    LONGVARCHAR("LONGVARCHAR", -1),
    NULL("NULL", 0),
    CHAR("CHAR", 1),
    NUMERIC("NUMERIC", 2),
    DECIMAL("DECIMAL", 3),
    INTEGER("INTEGER", 4),
    SMALLINT("SMALLINT", 5),
    FLOAT("FLOAT", 6),
    REAL("REAL", 7),
    DOUBLE("DOUBLE", 8),
    VARCHAR("VARCHAR", 12),
    DATE("DATE", 91),
    TIME("TIME", 92),
    TIMESTAMP("TIMESTAMP", 93),
    OTHER("OTHER", 111),
    ;

    private String type;
    private int num;

}
