package com.meession.etm.module.crm.service.workreport;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportPageReqVO;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workreport.CrmWorkReportDO;

import jakarta.validation.Valid;

public interface CrmWorkReportService {

    Long createWorkReport(@Valid CrmWorkReportSaveReqVO createReqVO, Long userId);

    void updateWorkReport(@Valid CrmWorkReportSaveReqVO updateReqVO);

    void deleteWorkReport(Long id);

    CrmWorkReportDO getWorkReport(Long id);

    PageResult<CrmWorkReportDO> getWorkReportPage(CrmWorkReportPageReqVO pageReqVO);

    void submitWorkReport(Long id);

    void approveWorkReport(Long id, Integer status, String remark, Long auditUserId);

    void cancelWorkReport(Long id);

}
