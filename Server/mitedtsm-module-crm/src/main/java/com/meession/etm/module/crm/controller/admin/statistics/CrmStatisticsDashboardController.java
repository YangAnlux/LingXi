package com.meession.etm.module.crm.controller.admin.statistics;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.service.statistics.CrmSendStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发送统计仪表盘")
@RestController
@RequestMapping("/crm/statistics")
@Validated
public class CrmStatisticsDashboardController {

    @Resource
    private CrmSendStatisticsService statisticsService;

    @GetMapping("/dashboard")
    @Operation(summary = "获取统计仪表盘数据（4卡片 + 7天趋势）")
    @PreAuthorize("@ss.hasPermission('crm:send-statistics:query')")
    public CommonResult<Map<String, Object>> getDashboard(
            @RequestParam(value = "bizType", required = false) String bizType,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return success(statisticsService.getDashboard(bizType, channel, startDate, endDate));
    }

    @GetMapping("/tasks")
    @Operation(summary = "获取任务明细分页（含打开率）")
    @PreAuthorize("@ss.hasPermission('crm:send-statistics:query')")
    public CommonResult<PageResult<Map<String, Object>>> getTasks(@Valid CrmSendTaskPageReqVO pageVO) {
        return success(statisticsService.getTaskStatistics(pageVO));
    }

}
