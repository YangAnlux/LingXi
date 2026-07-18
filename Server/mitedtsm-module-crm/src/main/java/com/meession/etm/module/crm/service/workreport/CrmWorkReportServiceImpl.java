package com.meession.etm.module.crm.service.workreport;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportPageReqVO;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workreport.CrmWorkReportDO;
import com.meession.etm.module.crm.dal.mysql.workreport.CrmWorkReportMapper;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;

@Service
@Slf4j
public class CrmWorkReportServiceImpl implements CrmWorkReportService {

    @Resource
    private CrmWorkReportMapper workReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkReport(CrmWorkReportSaveReqVO createReqVO, Long userId) {
        CrmWorkReportDO report = BeanUtils.toBean(createReqVO, CrmWorkReportDO.class);
        report.setStatus(CrmAuditStatusEnum.DRAFT.getStatus());
        report.setOwnerUserId(userId);
        workReportMapper.insert(report);
        return report.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkReport(CrmWorkReportSaveReqVO updateReqVO) {
        CrmWorkReportDO report = validateWorkReportExists(updateReqVO.getId());
        checkStatusCanUpdate(report);
        BeanUtils.copyProperties(updateReqVO, report);
        workReportMapper.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkReport(Long id) {
        CrmWorkReportDO report = validateWorkReportExists(id);
        Integer status = report.getStatus();
        if (!CrmAuditStatusEnum.DRAFT.getStatus().equals(status)
                && !CrmAuditStatusEnum.CANCEL.getStatus().equals(status)) {
            throw exception(WORK_REPORT_DELETE_FAIL_NOT_DRAFT);
        }
        workReportMapper.deleteById(id);
    }

    @Override
    public CrmWorkReportDO getWorkReport(Long id) {
        return validateWorkReportExists(id);
    }

    @Override
    public PageResult<CrmWorkReportDO> getWorkReportPage(CrmWorkReportPageReqVO pageReqVO) {
        return workReportMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitWorkReport(Long id) {
        CrmWorkReportDO report = validateWorkReportExists(id);
        if (!CrmAuditStatusEnum.DRAFT.getStatus().equals(report.getStatus())) {
            throw exception(WORK_REPORT_SUBMIT_FAIL_NOT_DRAFT);
        }
        report.setStatus(CrmAuditStatusEnum.PROCESS.getStatus());
        workReportMapper.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveWorkReport(Long id, Integer status, String remark, Long auditUserId) {
        CrmWorkReportDO report = validateWorkReportExists(id);
        if (!CrmAuditStatusEnum.PROCESS.getStatus().equals(report.getStatus())) {
            throw exception(WORK_REPORT_APPROVE_FAIL_NOT_PROCESS);
        }
        if (!CrmAuditStatusEnum.APPROVE.getStatus().equals(status)
                && !CrmAuditStatusEnum.REJECT.getStatus().equals(status)) {
            throw exception(WORK_REPORT_APPROVE_FAIL_NOT_PROCESS);
        }
        report.setStatus(status);
        report.setAuditTime(LocalDateTime.now());
        report.setAuditRemark(remark);
        report.setAuditUserId(auditUserId);
        workReportMapper.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelWorkReport(Long id) {
        CrmWorkReportDO report = validateWorkReportExists(id);
        Integer currentStatus = report.getStatus();
        if (!CrmAuditStatusEnum.DRAFT.getStatus().equals(currentStatus)
                && !CrmAuditStatusEnum.PROCESS.getStatus().equals(currentStatus)) {
            throw exception(WORK_REPORT_APPROVE_FAIL_NOT_PROCESS);
        }
        report.setStatus(CrmAuditStatusEnum.CANCEL.getStatus());
        workReportMapper.updateById(report);
    }

    private void checkStatusCanUpdate(CrmWorkReportDO report) {
        if (!CrmAuditStatusEnum.DRAFT.getStatus().equals(report.getStatus())) {
            throw exception(WORK_REPORT_UPDATE_FAIL_NOT_DRAFT);
        }
    }

    private CrmWorkReportDO validateWorkReportExists(Long id) {
        CrmWorkReportDO report = workReportMapper.selectById(id);
        if (report == null) {
            throw exception(WORK_REPORT_NOT_EXISTS);
        }
        return report;
    }

}
