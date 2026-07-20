// 23软2张奎良-202305566305
package com.meession.etm.module.report.dal.dataobject.task;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务管理 DO
 * 
 * 任务管理用于记录和跟踪工作任务的执行情况，支持任务的创建、分配、执行和完成。
 * 
 * @author 23软2张奎良
 */
@TableName("report_task")
@KeySequence("report_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务类型
     * 
     * 枚举值：1-日常任务，2-紧急任务，3-项目任务，4-临时任务
     */
    private Integer type;

    /**
     * 优先级
     * 
     * 枚举值：1-低，2-中，3-高，4-紧急
     */
    private Integer priority;

    /**
     * 状态
     * 
     * 枚举值：0-待分配，1-进行中，2-已完成，3-已取消
     */
    private Integer status;

    /**
     * 负责人ID
     */
    private Long assigneeId;

    /**
     * 负责人姓名
     */
    private String assigneeName;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 截止日期
     */
    private LocalDate endDate;

    /**
     * 完成日期
     */
    private LocalDate completeDate;

    /**
     * 完成进度（0-100）
     */
    private Integer progress;

    /**
     * 备注
     */
    private String remark;

}