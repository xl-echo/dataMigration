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
public class TransferDatabaseConfig {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 任务主键
     */
    private String taskId;

    /**
     * 数据库方向: source target
     */
    private String databaseDirection;

    /**
     * 数据库地址
     */
    private String databaseUrl;

    /**
     * 用户名
     */
    private String databaseUsername;

    /**
     * 密码
     */
    private String databasePassword;

    /**
     * 0: 无效; 1: 有效
     */
    private Long status;

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

