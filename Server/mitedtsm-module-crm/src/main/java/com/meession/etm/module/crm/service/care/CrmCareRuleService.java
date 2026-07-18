package com.meession.etm.module.crm.service.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRulePageReqVO;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRuleRespVO;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRuleSaveReqVO;

import jakarta.validation.Valid;

public interface CrmCareRuleService {

    Long createRule(@Valid CrmCareRuleSaveReqVO reqVO);

    void updateRule(@Valid CrmCareRuleSaveReqVO reqVO);

    void deleteRule(Long id);

    CrmCareRuleRespVO getRule(Long id);

    PageResult<CrmCareRuleRespVO> getRulePage(CrmCareRulePageReqVO pageReqVO);

    /** 手动执行关怀规则 */
    int executeRule(Long id);

}
