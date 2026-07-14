// 23软2张奎良-202305566305
package com.meession.etm.module.report.dal.dataobject.workreport;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDate;

/**
 * 工作报表 DO
 * 
 * 工作报表用于记录员工的日常工作情况，支持日报、周报、月报三种类型。
 * 
 * @author 23软2张奎良
 */
@TableName("report_work_report")
@KeySequence("report_work_report_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkReportDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 报表类型
     * 
     * 枚举值：1-日报，2-周报，3-月报
     */
    private Integer type;

    /**
     * 报表标题
     */
    private String title;

    /**
     * 报表日期（日报：具体日期，周报/月报：周期开始日期）
     */
    private LocalDate reportDate;

    /**
     * 周期结束日期（周报/月报使用）
     */
    private LocalDate endDate;

    /**
     * 工作内容
     */
    private String content;

    /**
     * 工作成果
     */
    private String achievements;

    /**
     * 问题与困难
     */
    private String problems;

    /**
     * 下周/下月工作计划
     */
    private String plan;

    /**
     * 报告人ID
     */
    private Long reporterId;

    /**
     * 报告人姓名
     */
    private String reporterName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 状态
     * 
     * 枚举值：0-草稿，1-已提交
     */
    private Integer status;

}