// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款审批状态监听器 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.service.refund.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.crm.service.refund.CrmRefundService;
import com.meession.etm.module.crm.service.refund.CrmRefundServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 退款审批结果监听器
 * <p>
 * BPM 审批通过/驳回后回调更新 crm_refund.audit_status
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@Component
public class CrmRefundStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CrmRefundService refundService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmRefundServiceImpl.BPM_PROCESS_DEFINITION_KEY;
    }

    @Override
    public void onEvent(BpmProcessInstanceStatusEvent event) {
        refundService.updateRefundAuditStatus(
                Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
// [ADD END] 退款审批状态监听器 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
