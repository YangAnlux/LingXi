// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销审批状态监听器 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.service.reimbursement.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.crm.service.reimbursement.CrmReimbursementService;
import com.meession.etm.module.crm.service.reimbursement.CrmReimbursementServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 报销审批结果监听器
 * <p>
 * BPM 审批通过/驳回后回调更新 crm_reimbursement.audit_status
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@Component
public class CrmReimbursementStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CrmReimbursementService reimbursementService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmReimbursementServiceImpl.BPM_PROCESS_DEFINITION_KEY;
    }

    @Override
    public void onEvent(BpmProcessInstanceStatusEvent event) {
        reimbursementService.updateReimbursementAuditStatus(
                Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
// [ADD END] 报销审批状态监听器 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
