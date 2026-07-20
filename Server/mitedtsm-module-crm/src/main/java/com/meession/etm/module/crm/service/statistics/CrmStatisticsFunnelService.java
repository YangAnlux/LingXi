package com.meession.etm.module.crm.service.statistics;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.funnel.*;
import com.meession.etm.module.crm.dal.dataobject.business.CrmBusinessDO;

import java.util.List;

/**
 * CRM 销售漏斗分析 Service
 *
 * @author HUIHUI
 */
public interface CrmStatisticsFunnelService {

    /**
     * 获得销售漏斗数据
     *
     * @param reqVO 请求
     * @return 销售漏斗数据
     */
    CrmStatisticFunnelSummaryRespVO getFunnelSummary(CrmStatisticsFunnelStatReqVO reqVO);

    /**
     * 获得商机结束状态统计
     *
     * @param reqVO 请求
     * @return 商机结束状态统计
     */
    List<CrmStatisticsBusinessSummaryByEndStatusRespVO> getBusinessSummaryByEndStatus(CrmStatisticsFunnelStatReqVO reqVO);

    /**
     * 获取新增商机分析(按日期)
     *
     * @param reqVO 请求
     * @return 新增商机分析
     */
    List<CrmStatisticsBusinessSummaryByDateRespVO> getBusinessSummaryByDate(CrmStatisticsFunnelStatReqVO reqVO);

    /**
     * 获得商机转化率分析(按日期)
     *
     * @param reqVO 请求
     * @return 商机转化率分析
     */
    List<CrmStatisticsBusinessInversionRateSummaryByDateRespVO> getBusinessInversionRateSummaryByDate(CrmStatisticsFunnelStatReqVO reqVO);

    /**
     * 获得商机分页(按日期)
     *
     * @param pageVO 请求
     * @return 商机分页
     */
    PageResult<CrmBusinessDO> getBusinessPageByDate(CrmStatisticsFunnelReqVO pageVO);

    /**
     * 获得商机阶段统计
     *
     * @param reqVO 请求
     * @return 商机阶段统计
     */
    List<CrmStatisticsBusinessSummaryByStatusRespVO> getBusinessSummaryByStatus(CrmStatisticsFunnelStatReqVO reqVO);

}
