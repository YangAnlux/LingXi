package com.meession.etm.module.crm.service.statistics;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;

import java.util.List;
import java.util.Map;

public interface CrmSendStatisticsService {

    /** 获取发送统计数据，可按营销活动过滤 */
    Map<String, Object> getSendStatistics(Long campaignId);

    /** 获取按营销活动分组的发送统计 */
    List<Map<String, Object>> getCampaignBreakdown();

    /** 获取统计仪表盘数据（4 卡片 + 7天趋势） */
    Map<String, Object> getDashboard(String bizType, String channel, String startDate, String endDate);

    /** 获取任务明细分页（含打开率） */
    PageResult<Map<String, Object>> getTaskStatistics(CrmSendTaskPageReqVO pageReqVO);

}
