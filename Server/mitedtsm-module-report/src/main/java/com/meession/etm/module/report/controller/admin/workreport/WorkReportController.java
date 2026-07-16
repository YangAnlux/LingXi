// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.workreport;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import java.util.*;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import static com.meession.etm.framework.common.pojo.CommonResult.success;

import com.meession.etm.module.report.controller.admin.workreport.vo.*;
import com.meession.etm.module.report.dal.dataobject.workreport.WorkReportDO;
import com.meession.etm.module.report.service.workreport.WorkReportService;

/**
 * 工作报表 Controller
 * 
 * 提供工作报表的 REST API 接口，包括创建、更新、删除、查询等操作。
 * 
 * @author 23软2张奎良
 */
@Tag(name = "管理后台 - 工作报表")
@RestController
@RequestMapping("/report/work-report")
@Validated
public class WorkReportController {

    @Resource
    private WorkReportService workReportService;

    /**
     * 创建工作报表
     * 
     * @param createReqVO 创建请求参数
     * @return 创建后的报表ID
     */
    @PostMapping("/create")
    @Operation(summary = "创建工作报表")
    @PreAuthorize("@ss.hasPermission('report:work-report:create')")
    public CommonResult<Long> createWorkReport(@Valid @RequestBody WorkReportSaveReqVO createReqVO) {
        return success(workReportService.createWorkReport(createReqVO));
    }

    /**
     * 更新工作报表
     * 
     * @param updateReqVO 更新请求参数
     * @return 更新结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新工作报表")
    @PreAuthorize("@ss.hasPermission('report:work-report:update')")
    public CommonResult<Boolean> updateWorkReport(@Valid @RequestBody WorkReportSaveReqVO updateReqVO) {
        workReportService.updateWorkReport(updateReqVO);
        return success(true);
    }

    /**
     * 删除工作报表
     * 
     * @param id 报表ID
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除工作报表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('report:work-report:delete')")
    public CommonResult<Boolean> deleteWorkReport(@RequestParam("id") Long id) {
        workReportService.deleteWorkReport(id);
        return success(true);
    }

    /**
     * 获取工作报表详情
     * 
     * @param id 报表ID
     * @return 报表详情
     */
    @GetMapping("/get")
    @Operation(summary = "获得工作报表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('report:work-report:query')")
    public CommonResult<WorkReportRespVO> getWorkReport(@RequestParam("id") Long id) {
        WorkReportDO workReport = workReportService.getWorkReport(id);
        return success(BeanUtils.toBean(workReport, WorkReportRespVO.class));
    }

    /**
     * 获取工作报表分页
     * 
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "获得工作报表分页")
    @PreAuthorize("@ss.hasPermission('report:work-report:query')")
    public CommonResult<PageResult<WorkReportRespVO>> getWorkReportPage(@Valid WorkReportPageReqVO pageReqVO) {
        PageResult<WorkReportDO> pageResult = workReportService.getWorkReportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkReportRespVO.class));
    }

    /**
     * 提交工作报表
     * 
     * @param id 报表ID
     * @return 提交结果
     */
    @PutMapping("/submit")
    @Operation(summary = "提交工作报表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('report:work-report:submit')")
    public CommonResult<Boolean> submitWorkReport(@RequestParam("id") Long id) {
        workReportService.submitWorkReport(id);
        return success(true);
    }

    /**
     * 审批工作报表
     * 
     * @param approveReqVO 审批请求参数
     * @return 审批结果
     */
    @PutMapping("/approve")
    @Operation(summary = "审批工作报表")
    @PreAuthorize("@ss.hasPermission('report:work-report:approve')")
    public CommonResult<Boolean> approveWorkReport(@Valid @RequestBody WorkReportApproveReqVO approveReqVO) {
        workReportService.approveWorkReport(approveReqVO);
        return success(true);
    }

}