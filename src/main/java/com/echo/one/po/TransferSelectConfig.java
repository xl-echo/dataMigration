package com.echo.one.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author echo
 * @date 2022/11/11 9:19
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferSelectConfig {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 任务主键
     */
    private String taskId;

    /**
     * 源库配置ID
     */
    private String sourceConfig;

    /**
     * 源表名
     */
    private String tableName;

    /**
     * 源表字段名cloumn1,cloumn2...
     */
    private String tableCloumnName;

    /**
     * 源表数据查询条件: cloumn1=1 and cloumn2=2...
     */
    private String tableWhere;

    /**
     * 源表数据查询条件: cloumn1=1 and cloumn2=2...
     */
    private Integer limitSize;

    /**
     * 0: 无效; 1: 有效
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createUname;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateUname;

    /**
     * 修改时间
     */
    private Date updateTime;

}

