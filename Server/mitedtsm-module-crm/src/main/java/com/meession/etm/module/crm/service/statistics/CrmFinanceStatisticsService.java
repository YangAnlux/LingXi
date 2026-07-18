// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 财务汇总Service - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.service.statistics;

import com.meession.etm.module.crm.controller.admin.statistics.vo.finance.CrmFinanceStatisticsRespVO;

/**
 * CRM 财务统计 Service 接口
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
public interface CrmFinanceStatisticsService {

    /**
     * 获取财务汇总数据
     *
     * @return 汇总数据
     */
    CrmFinanceStatisticsRespVO getFinanceSummary();

}
// [ADD END] 财务汇总Service - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
