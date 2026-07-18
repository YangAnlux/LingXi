package com.meession.etm.module.crm.controller.admin.workreport;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportPageReqVO;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportRespVO;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workreport.CrmWorkReportDO;
import com.meession.etm.module.crm.service.workreport.CrmWorkReportService;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "管理后台 - 工作报告")
@RestController
@RequestMapping("/crm/work-report")
@Validated
public class CrmWorkReportController {

    @Resource
    private CrmWorkReportService workReportService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建工作报告")
    @PreAuthorize("@ss.hasPermission('crm:work-report:create')")
    public CommonResult<Long> createWorkReport(@Valid @RequestBody CrmWorkReportSaveReqVO createReqVO) {
        return success(workReportService.createWorkReport(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作报告")
    @PreAuthorize("@ss.hasPermission('crm:work-report:update')")
    public CommonResult<Boolean> updateWorkReport(@Valid @RequestBody CrmWorkReportSaveReqVO updateReqVO) {
        workReportService.updateWorkReport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-report:delete')")
    public CommonResult<Boolean> deleteWorkReport(@RequestParam("id") Long id) {
        workReportService.deleteWorkReport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:work-report:query')")
    public CommonResult<CrmWorkReportRespVO> getWorkReport(@RequestParam("id") Long id) {
        CrmWorkReportDO report = workReportService.getWorkReport(id);
        return success(buildWorkReportDetail(report));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作报告分页")
    @PreAuthorize("@ss.hasPermission('crm:work-report:query')")
    public CommonResult<PageResult<CrmWorkReportRespVO>> getWorkReportPage(@Valid CrmWorkReportPageReqVO pageVO) {
        PageResult<CrmWorkReportDO> pageResult = workReportService.getWorkReportPage(pageVO);
        return success(new PageResult<>(buildWorkReportDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工作报告 Excel")
    @PreAuthorize("@ss.hasPermission('crm:work-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWorkReportExcel(@Valid CrmWorkReportPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmWorkReportDO> list = workReportService.getWorkReportPage(pageReqVO).getList();
        ExcelUtils.write(response, "工作报告.xls", "数据", CrmWorkReportRespVO.class, buildWorkReportDetailList(list));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-report:update')")
    public CommonResult<Boolean> submitWorkReport(@RequestParam("id") Long id) {
        workReportService.submitWorkReport(id);
        return success(true);
    }

    @PutMapping("/approve")
    @Operation(summary = "审批工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "status", description = "审批结果 20-通过 30-不通过", required = true)
    @Parameter(name = "remark", description = "审批意见")
    @PreAuthorize("@ss.hasPermission('crm:work-report:update')")
    public CommonResult<Boolean> approveWorkReport(@RequestParam("id") Long id,
                                                    @RequestParam("status") Integer status,
                                                    @RequestParam(value = "remark", required = false) String remark) {
        workReportService.approveWorkReport(id, status, remark, getLoginUserId());
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消工作报告")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-report:update')")
    public CommonResult<Boolean> cancelWorkReport(@RequestParam("id") Long id) {
        workReportService.cancelWorkReport(id);
        return success(true);
    }

    private CrmWorkReportRespVO buildWorkReportDetail(CrmWorkReportDO report) {
        if (report == null) {
            return null;
        }
        return buildWorkReportDetailList(singletonList(report)).get(0);
    }

    private List<CrmWorkReportRespVO> buildWorkReportDetailList(List<CrmWorkReportDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                report -> Stream.of(report.getOwnerUserId(), report.getAuditUserId(),
                        NumberUtils.parseLong(report.getCreator()))));
        return BeanUtils.toBean(list, CrmWorkReportRespVO.class, reportVO -> {
            MapUtils.findAndThen(userMap, reportVO.getOwnerUserId(),
                    user -> reportVO.setOwnerUserName(user.getNickname()));
            MapUtils.findAndThen(userMap, reportVO.getAuditUserId(),
                    user -> reportVO.setAuditUserName(user.getNickname()));
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(reportVO.getCreator()),
                    user -> reportVO.setCreatorName(user.getNickname()));
        });
    }

}
