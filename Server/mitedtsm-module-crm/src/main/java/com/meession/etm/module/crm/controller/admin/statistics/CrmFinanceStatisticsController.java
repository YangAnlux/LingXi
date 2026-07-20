// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 财务汇总Controller - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.controller.admin.statistics;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.finance.CrmFinanceStatisticsRespVO;
import com.meession.etm.module.crm.service.statistics.CrmFinanceStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 财务统计")
@RestController
@RequestMapping("/crm/finance-statistics")
@Validated
public class CrmFinanceStatisticsController {

    @Resource
    private CrmFinanceStatisticsService financeStatisticsService;

    @GetMapping("/summary")
    @Operation(summary = "获取财务汇总数据")
    @PreAuthorize("@ss.hasPermission('crm:finance-statistics:query')")
    public CommonResult<CrmFinanceStatisticsRespVO> getFinanceSummary() {
        return success(financeStatisticsService.getFinanceSummary());
    }

}
// [ADD END] 财务汇总Controller - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
