package com.meession.etm.module.crm.service.care;

public interface CrmCustomerCareService {

    /** 执行生日关怀 — 检测今天生日的客户并发送关怀消息 */
    int executeBirthdayCare();

    /** 执行节日关怀 — 检测今天是否为配置的节日，并向所有客户发送关怀消息 */
    int executeHolidayCare();

}
