package com.meession.etm.module.promotion.controller.admin.campaign;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignCreateReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignPageReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignRespVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignUpdateReqVO;
import com.meession.etm.module.promotion.convert.campaign.CampaignConvert;
import com.meession.etm.module.promotion.dal.dataobject.campaign.CampaignDO;
import com.meession.etm.module.promotion.service.campaign.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 营销活动")
@RestController
@RequestMapping("/promotion/campaign")
@Validated
public class CampaignController {

    @Resource
    private CampaignService campaignService;

    @PostMapping("/create")
    @Operation(summary = "创建营销活动")
    @PreAuthorize("@ss.hasPermission('promotion:campaign:create')")
    public CommonResult<Long> createCampaign(@Valid @RequestBody CampaignCreateReqVO createReqVO) {
        return success(campaignService.createCampaign(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新营销活动")
    @PreAuthorize("@ss.hasPermission('promotion:campaign:update')")
    public CommonResult<Boolean> updateCampaign(@Valid @RequestBody CampaignUpdateReqVO updateReqVO) {
        campaignService.updateCampaign(updateReqVO);
        return success(true);
    }

    @PutMapping("/close")
    @Operation(summary = "关闭营销活动")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:campaign:close')")
    public CommonResult<Boolean> closeCampaign(@RequestParam("id") Long id) {
        campaignService.closeCampaign(id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除营销活动")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('promotion:campaign:delete')")
    public CommonResult<Boolean> deleteCampaign(@RequestParam("id") Long id) {
        campaignService.deleteCampaign(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得营销活动")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('promotion:campaign:query')")
    public CommonResult<CampaignRespVO> getCampaign(@RequestParam("id") Long id) {
        CampaignDO campaign = campaignService.getCampaign(id);
        if (campaign == null) {
            return success(null);
        }
        return success(CampaignConvert.INSTANCE.convert(campaign));
    }

    @GetMapping("/page")
    @Operation(summary = "获得营销活动分页")
    @PreAuthorize("@ss.hasPermission('promotion:campaign:query')")
    public CommonResult<PageResult<CampaignRespVO>> getCampaignPage(@Valid CampaignPageReqVO pageVO) {
        PageResult<CampaignDO> pageResult = campaignService.getCampaignPage(pageVO);
        return success(CampaignConvert.INSTANCE.convertPage(pageResult));
    }

}
