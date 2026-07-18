package com.meession.etm.module.crm.job.workorder;

import com.meession.etm.framework.quartz.core.handler.JobHandler;
import com.meession.etm.framework.tenant.core.job.TenantJob;
import com.meession.etm.module.crm.service.workorder.CrmWorkOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 工单 SLA 超时扫描 Job
 * 23软件工程4班蔡磊202305566515
 */
@Component
public class CrmWorkOrderSlaJob implements JobHandler {

    @Resource
    private CrmWorkOrderService workOrderService;

    @Override
    @TenantJob
    public String execute(String param) {
        int count = workOrderService.updateSlaBreached(); // 23软件工程4班蔡磊202305566515
        return String.format("标记超SLA工单 %s 个", count);
    }

}
