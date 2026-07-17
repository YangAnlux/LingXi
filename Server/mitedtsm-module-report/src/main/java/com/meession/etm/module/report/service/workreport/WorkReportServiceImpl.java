// 23软2张奎良-202305566305
package com.meession.etm.module.report.service.workreport;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportPageReqVO;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportSaveReqVO;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportApproveReqVO;
import com.meession.etm.module.report.dal.dataobject.workreport.WorkReportDO;
import com.meession.etm.module.report.dal.mysql.workreport.WorkReportMapper;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.report.enums.ErrorCodeConstants.WORK_REPORT_NOT_EXISTS;
import static com.meession.etm.module.report.enums.ErrorCodeConstants.WORK_REPORT_NOT_DRAFT;
import static com.meession.etm.module.report.enums.ErrorCodeConstants.WORK_REPORT_NOT_SUBMITTED;

/**
 * 工作报表 Service 实现类
 * 
 * 实现工作报表的业务逻辑，包括创建、更新、删除、查询等功能。
 * 
 * @author 23软2张奎良
 */
@Service
@Validated
public class WorkReportServiceImpl implements WorkReportService {

    @Resource
    private WorkReportMapper workReportMapper;

    /**
     * 创建工作报表
     * 
     * @param createReqVO 创建请求参数
     * @return 创建后的报表ID
     */
    @Override
    public Long createWorkReport(WorkReportSaveReqVO createReqVO) {
        // 将 VO 转换为 DO
        WorkReportDO workReport = BeanUtils.toBean(createReqVO, WorkReportDO.class);
        // 插入数据库
        workReportMapper.insert(workReport);
        // 返回报表ID
        return workReport.getId();
    }

    /**
     * 更新工作报表
     * 
     * @param updateReqVO 更新请求参数
     */
    @Override
    public void updateWorkReport(WorkReportSaveReqVO updateReqVO) {
        // 校验报表是否存在
        validateWorkReportExists(updateReqVO.getId());
        // 将 VO 转换为 DO
        WorkReportDO workReport = BeanUtils.toBean(updateReqVO, WorkReportDO.class);
        // 更新数据库
        workReportMapper.updateById(workReport);
    }

    /**
     * 删除工作报表
     * 
     * @param id 报表ID
     */
    @Override
    public void deleteWorkReport(Long id) {
        // 校验报表是否存在
        validateWorkReportExists(id);
        // 删除数据库记录
        workReportMapper.deleteById(id);
    }

    /**
     * 获取工作报表详情
     * 
     * @param id 报表ID
     * @return 报表详情
     */
    @Override
    public WorkReportDO getWorkReport(Long id) {
        return workReportMapper.selectById(id);
    }

    /**
     * 查询工作报表分页
     * 
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<WorkReportDO> getWorkReportPage(WorkReportPageReqVO pageReqVO) {
        return workReportMapper.selectPage(pageReqVO);
    }

    /**
     * 校验工作报表是否存在
     * 
     * @param id 报表ID
     */
    private void validateWorkReportExists(Long id) {
        if (workReportMapper.selectById(id) == null) {
            throw exception(WORK_REPORT_NOT_EXISTS);
        }
    }

    /**
     * 提交工作报表
     * 
     * 将工作报表状态从草稿(0)变更为已提交(1)。
     * 只有草稿状态的报表才能提交。
     * 
     * @param id 报表ID
     */
    @Override
    public void submitWorkReport(Long id) {
        WorkReportDO workReport = workReportMapper.selectById(id);
        validateWorkReportExists(id);
        
        if (!workReport.getStatus().equals(0)) {
            throw exception(WORK_REPORT_NOT_DRAFT);
        }
        
        workReport.setStatus(1);
        workReportMapper.updateById(workReport);
    }

    /**
     * 审批工作报表
     * 
     * 将工作报表状态从已提交(1)变更为已审批(2)或已驳回(3)。
     * 只有已提交状态的报表才能审批。
     * 
     * @param approveReqVO 审批请求参数
     */
    @Override
    public void approveWorkReport(WorkReportApproveReqVO approveReqVO) {
        Long id = approveReqVO.getId();
        WorkReportDO workReport = workReportMapper.selectById(id);
        validateWorkReportExists(id);
        
        if (!workReport.getStatus().equals(1)) {
            throw exception(WORK_REPORT_NOT_SUBMITTED);
        }
        
        workReport.setStatus(approveReqVO.getStatus());
        workReport.setApproveComment(approveReqVO.getApproveComment());
        workReport.setApproveTime(LocalDateTime.now());
        
        workReportMapper.updateById(workReport);
    }

}