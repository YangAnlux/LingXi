package com.meession.etm.module.crm.dal.tdengine;

import com.meession.etm.module.crm.dal.dataobject.send.CrmSendLogTDengineDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * CRM 发送日志 TDengine Mapper
 * 使用时需配合 @TDengineDS 注解切换数据源
 */
@Mapper
public interface CrmSendLogTDengineMapper {

    /** 创建超级表（首次初始化时调用） */
    void createSTable();

    /** 查询超级表是否存在 */
    int showSTable();

    /** 插入一条发送日志 */
    int insert(CrmSendLogTDengineDO log);

    /** 今日统计聚合：按 biz_type + channel 分组 */
    List<Map<String, Object>> selectTodayStats(@Param("bizType") String bizType,
                                                @Param("channel") String channel);

    /** 近7天趋势：按天聚合发送量 */
    List<Map<String, Object>> select7DayTrend(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /** 任务维度统计 */
    List<Map<String, Object>> selectTaskStats(@Param("taskId") Long taskId);

}
