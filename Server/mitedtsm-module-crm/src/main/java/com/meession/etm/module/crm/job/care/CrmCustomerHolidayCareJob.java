package com.meession.etm.module.crm.job.care;

import com.meession.etm.framework.quartz.core.handler.JobHandler;
import com.meession.etm.framework.tenant.core.job.TenantJob;
import com.meession.etm.module.crm.service.care.CrmCustomerCareService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CRM 客户节日关怀定时任务
 * 每天上午9:00执行，检测当天是否为配置的节日并发送短信/邮件关怀
 */
@Component
@Slf4j
public class CrmCustomerHolidayCareJob implements JobHandler {

    @Resource
    private CrmCustomerCareService customerCareService;

    @Override
    @TenantJob
    public String execute(String param) {
        log.info("[CrmCustomerHolidayCareJob][开始执行客户节日关怀]");
        int count = customerCareService.executeHolidayCare();
        return String.format("发送节日关怀消息 %s 条", count);
    }

}
