// 23软2张奎良-202305566305
package com.meession.etm.module.report.service.workreport;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportApproveReqVO;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportPageReqVO;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportSaveReqVO;
import com.meession.etm.module.report.dal.dataobject.workreport.WorkReportDO;

import jakarta.validation.Valid;

/**
 * 工作报表 Service 接口
 * 
 * 定义工作报表的业务操作方法，包括创建、更新、删除、查询等功能。
 * 
 * @author 23软2张奎良
 */
public interface WorkReportService {

    /**
     * 创建工作报表
     * 
     * @param createReqVO 创建请求参数
     * @return 创建后的报表ID
     */
    Long createWorkReport(@Valid WorkReportSaveReqVO createReqVO);

    /**
     * 更新工作报表
     * 
     * @param updateReqVO 更新请求参数
     */
    void updateWorkReport(@Valid WorkReportSaveReqVO updateReqVO);

    /**
     * 删除工作报表
     * 
     * @param id 报表ID
     */
    void deleteWorkReport(Long id);

    /**
     * 获取工作报表详情
     * 
     * @param id 报表ID
     * @return 报表详情
     */
    WorkReportDO getWorkReport(Long id);

    /**
     * 查询工作报表分页
     * 
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    PageResult<WorkReportDO> getWorkReportPage(WorkReportPageReqVO pageReqVO);

    /**
     * 提交工作报表
     * 
     * @param id 报表ID
     */
    void submitWorkReport(Long id);

    /**
     * 审批工作报表
     * 
     * @param approveReqVO 审批请求参数
     */
    void approveWorkReport(WorkReportApproveReqVO approveReqVO);

}