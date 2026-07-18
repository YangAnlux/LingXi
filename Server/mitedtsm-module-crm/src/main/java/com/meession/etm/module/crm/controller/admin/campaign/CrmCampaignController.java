package com.meession.etm.module.crm.controller.admin.campaign;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignRespVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskDO;
import com.meession.etm.module.crm.dal.mysql.send.CrmSendTaskMapper;
import com.meession.etm.module.crm.service.campaign.CrmCampaignService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "管理后台 - 营销活动")
@RestController
@RequestMapping("/crm/campaign")
@Validated
public class CrmCampaignController {

    @Resource
    private CrmCampaignService campaignService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CrmSendTaskMapper sendTaskMapper;

    @PostMapping("/create")
    @Operation(summary = "创建营销活动")
    @PreAuthorize("@ss.hasPermission('crm:campaign:create')")
    public CommonResult<Long> createCampaign(@Valid @RequestBody CrmCampaignSaveReqVO createReqVO) {
        return success(campaignService.createCampaign(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新营销活动")
    @PreAuthorize("@ss.hasPermission('crm:campaign:update')")
    public CommonResult<Boolean> updateCampaign(@Valid @RequestBody CrmCampaignSaveReqVO updateReqVO) {
        campaignService.updateCampaign(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除营销活动")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:campaign:delete')")
    public CommonResult<Boolean> deleteCampaign(@RequestParam("id") Long id) {
        campaignService.deleteCampaign(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得营销活动")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:campaign:query')")
    public CommonResult<CrmCampaignRespVO> getCampaign(@RequestParam("id") Long id) {
        CrmCampaignDO campaign = campaignService.getCampaign(id);
        return success(buildCampaignDetail(campaign));
    }

    private CrmCampaignRespVO buildCampaignDetail(CrmCampaignDO campaign) {
        if (campaign == null) {
            return null;
        }
        return buildCampaignDetailList(singletonList(campaign)).get(0);
    }

    @GetMapping("/page")
    @Operation(summary = "获得营销活动分页")
    @PreAuthorize("@ss.hasPermission('crm:campaign:query')")
    public CommonResult<PageResult<CrmCampaignRespVO>> getCampaignPage(@Valid CrmCampaignPageReqVO pageVO) {
        PageResult<CrmCampaignDO> pageResult = campaignService.getCampaignPage(pageVO, getLoginUserId());
        return success(new PageResult<>(buildCampaignDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出营销活动 Excel")
    @PreAuthorize("@ss.hasPermission('crm:campaign:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCampaignExcel(@Valid CrmCampaignPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmCampaignDO> list = campaignService.getCampaignPage(pageReqVO, getLoginUserId()).getList();
        ExcelUtils.write(response, "营销活动.xls", "数据", CrmCampaignRespVO.class, buildCampaignDetailList(list));
    }

    private List<CrmCampaignRespVO> buildCampaignDetailList(List<CrmCampaignDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                campaign -> Stream.of(NumberUtils.parseLong(campaign.getCreator()), campaign.getOwnerUserId())));

        // 批量查询每个活动的关联发送任务数
        List<Long> campaignIds = list.stream().map(CrmCampaignDO::getId).toList();
        final Map<Long, Long> taskCountMap;
        if (!campaignIds.isEmpty()) {
            List<CrmSendTaskDO> tasks = sendTaskMapper.selectList(
                    new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<CrmSendTaskDO>()
                            .in(CrmSendTaskDO::getActivityId, campaignIds));
            taskCountMap = tasks.stream()
                    .filter(t -> t.getActivityId() != null)
                    .collect(Collectors.groupingBy(CrmSendTaskDO::getActivityId, Collectors.counting()));
        } else {
            taskCountMap = Collections.emptyMap();
        }

        return BeanUtils.toBean(list, CrmCampaignRespVO.class, campaignVO -> {
            // 将 epoch 0 时间（表示未设置）转为 null，避免导出/列表显示 "1970/01/01 08:00:00"
            if (campaignVO.getStartTime() != null
                    && campaignVO.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < 86400000L) {
                campaignVO.setStartTime(null);
            }
            if (campaignVO.getEndTime() != null
                    && campaignVO.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < 86400000L) {
                campaignVO.setEndTime(null);
            }
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(campaignVO.getCreator()),
                    user -> campaignVO.setCreatorName(user.getNickname()));
            MapUtils.findAndThen(userMap, campaignVO.getOwnerUserId(),
                    user -> campaignVO.setOwnerUserName(user.getNickname()));
            campaignVO.setSendTaskCount(taskCountMap.getOrDefault(campaignVO.getId(), 0L).intValue());
        });
    }

    @PutMapping("/status")
    @Operation(summary = "更新营销活动状态")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @Parameter(name = "status", description = "状态", required = true, example = "2")
    @PreAuthorize("@ss.hasPermission('crm:campaign:update')")
    public CommonResult<Boolean> updateCampaignStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        campaignService.updateCampaignStatus(id, status);
        return success(true);
    }

}
