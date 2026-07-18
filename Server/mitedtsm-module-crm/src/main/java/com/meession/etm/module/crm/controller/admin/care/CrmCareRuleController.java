package com.meession.etm.module.crm.controller.admin.care;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRulePageReqVO;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRuleRespVO;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRuleSaveReqVO;
import com.meession.etm.module.crm.service.care.CrmCareRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 自动关怀规则")
@RestController
@RequestMapping("/crm/care-rule")
@Validated
public class CrmCareRuleController {

    @Resource
    private CrmCareRuleService careRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建关怀规则")
    @PreAuthorize("@ss.hasPermission('crm:care-rule:create')")
    public CommonResult<Long> create(@Valid @RequestBody CrmCareRuleSaveReqVO reqVO) {
        return success(careRuleService.createRule(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新关怀规则")
    @PreAuthorize("@ss.hasPermission('crm:care-rule:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody CrmCareRuleSaveReqVO reqVO) {
        careRuleService.updateRule(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除关怀规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:care-rule:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        careRuleService.deleteRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取关怀规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:care-rule:query')")
    public CommonResult<CrmCareRuleRespVO> get(@RequestParam("id") Long id) {
        return success(careRuleService.getRule(id));
    }

    @GetMapping("/page")
    @Operation(summary = "关怀规则分页")
    @PreAuthorize("@ss.hasPermission('crm:care-rule:query')")
    public CommonResult<PageResult<CrmCareRuleRespVO>> page(@Valid CrmCareRulePageReqVO pageVO) {
        return success(careRuleService.getRulePage(pageVO));
    }

    @PostMapping("/execute/{id}")
    @Operation(summary = "手动执行关怀规则")
    @Parameter(name = "id", description = "规则编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:care-rule:update')")
    public CommonResult<Integer> execute(@PathVariable("id") Long id) {
        int count = careRuleService.executeRule(id);
        return success(count);
    }

}
