package com.meession.etm.module.crm.controller.admin.campaign;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.campaign.vo.send.*;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmCareLogDO;
import com.meession.etm.module.crm.service.campaign.CrmCampaignSendService;
import com.meession.etm.module.crm.service.statistics.CrmSendStatisticsService;
import com.meession.etm.module.system.dal.dataobject.mail.MailTemplateDO;
import com.meession.etm.module.system.dal.dataobject.sms.SmsTemplateDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 营销活动群发/关怀/统计")
@RestController
@RequestMapping("/crm/campaign")
@Validated
public class CrmCampaignSendController {

    @Resource
    private CrmCampaignSendService sendService;
    @Resource
    private CrmSendStatisticsService statisticsService;

    // ======================== 群发 ========================

    @PostMapping("/send-sms")
    @Operation(summary = "批量发送短信")
    @PreAuthorize("@ss.hasPermission('crm:campaign:update')")
    public CommonResult<CrmCampaignSendRespVO> sendSms(@Valid @RequestBody CrmCampaignSendReqVO reqVO) {
        return success(sendService.sendSms(reqVO, getLoginUserId()));
    }

    @PostMapping("/send-mail")
    @Operation(summary = "批量发送邮件")
    @PreAuthorize("@ss.hasPermission('crm:campaign:update')")
    public CommonResult<CrmCampaignSendRespVO> sendMail(@Valid @RequestBody CrmCampaignSendReqVO reqVO) {
        return success(sendService.sendMail(reqVO, getLoginUserId()));
    }

    // ======================== 模板列表 ========================

    @GetMapping("/sms-template-list")
    @Operation(summary = "获取短信模板列表(启用状态)")
    @PreAuthorize("@ss.hasPermission('crm:campaign:query')")
    public CommonResult<List<Map<String, Object>>> getSmsTemplateList() {
        List<SmsTemplateDO> list = sendService.getSmsTemplateList();
        return success(list.stream().map(t -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", t.getId());
            m.put("code", t.getCode());
            m.put("name", t.getName());
            m.put("content", t.getContent());
            return m;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/mail-template-list")
    @Operation(summary = "获取邮件模板列表(启用状态)")
    @PreAuthorize("@ss.hasPermission('crm:campaign:query')")
    public CommonResult<List<Map<String, Object>>> getMailTemplateList() {
        List<MailTemplateDO> list = sendService.getMailTemplateList();
        return success(list.stream().map(t -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", t.getId());
            m.put("code", t.getCode());
            m.put("name", t.getName());
            m.put("title", t.getTitle());
            m.put("content", t.getContent());
            return m;
        }).collect(Collectors.toList()));
    }

    // ======================== 关怀日志 ========================

    @GetMapping("/care-log/page")
    @Operation(summary = "获取关怀日志分页")
    @PreAuthorize("@ss.hasPermission('crm:care-log:query')")
    public CommonResult<PageResult<CrmCareLogRespVO>> getCareLogPage(@Valid CrmCareLogPageReqVO pageVO) {
        return success(sendService.getCareLogPage(pageVO));
    }

    // ======================== 发送统计 ========================

    @GetMapping("/send-statistics")
    @Operation(summary = "获取发送统计数据")
    @Parameter(name = "campaignId", description = "营销活动编号(可选，不传则查全部)")
    @PreAuthorize("@ss.hasPermission('crm:send-statistics:query')")
    public CommonResult<Map<String, Object>> getSendStatistics(
            @RequestParam(value = "campaignId", required = false) Long campaignId) {
        return success(statisticsService.getSendStatistics(campaignId));
    }

    @GetMapping("/send-statistics/breakdown")
    @Operation(summary = "获取按营销活动分组的发送统计")
    @PreAuthorize("@ss.hasPermission('crm:send-statistics:query')")
    public CommonResult<List<Map<String, Object>>> getCampaignBreakdown() {
        return success(statisticsService.getCampaignBreakdown());
    }

    // dashboard 和 tasks 端点已移至独立的 CrmStatisticsDashboardController (/crm/statistics)

    // ======================== 发送日志 ========================

    @GetMapping("/send-log/page")
    @Operation(summary = "获取发送日志分页")
    @PreAuthorize("@ss.hasPermission('crm:campaign-send:query')")
    public CommonResult<PageResult<CrmCampaignSendLogRespVO>> getSendLogPage(@Valid CrmCampaignSendLogPageReqVO pageVO) {
        return success(sendService.getSendLogPage(pageVO));
    }

}
