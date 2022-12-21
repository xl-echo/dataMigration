package com.echo.one.po;

import lombok.*;

import java.util.Date;


/**
 * @author echo
 * @date 2022/12/4 0:04
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferLog {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 任务主键
     */
    private String taskId;

    /**
     * 本次任务的追踪ID
     */
    private String traceId;

    /**
     * 定时任务开始时间
     */
    private Date taskBeginDate;

    /**
     * 定时任务结束时间
     */
    private Date taskEndDate;

    /**
     * 定时任务迁移数据条数
     */
    private Integer taskTransferDataSize;

    /**
     * 分成多少批
     */
    private Integer taskBatchNum;

    /**
     * 执行了多少批
     */
    private Integer taskExecBatchNum;

    /**
     * 文件个数
     */
    private Integer taskFileNum;

    /**
     * 写了多少个文件
     */
    private Integer taskWriteFileNum;

    /**
     * 执行状态: 0: 失败; 1: 成功, 2: 处理中
     */
    private String status;

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