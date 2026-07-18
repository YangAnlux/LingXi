package com.meession.etm.module.crm.job.task;

import com.meession.etm.framework.quartz.core.handler.JobHandler;
import com.meession.etm.module.crm.dal.dataobject.task.CrmTaskDO;
import com.meession.etm.module.crm.dal.mysql.task.CrmTaskMapper;
import com.meession.etm.module.crm.service.notify.CrmNotifyService;
import com.meession.etm.framework.tenant.core.job.TenantJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CRM 任务截止日期提醒 Job
 *
 * 检查截止时间在未来24小时内的待办/进行中的任务，向负责人发送站内信提醒
 */
@Component
@Slf4j
public class CrmTaskRemindJob implements JobHandler {

    @Resource
    private CrmTaskMapper taskMapper;

    @Resource
    private CrmNotifyService crmNotifyService;

    @Override
    @TenantJob
    public String execute(String param) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime remindEnd = now.plusHours(24);

        // 查询截止时间在未来24小时内的未完成任务
        List<CrmTaskDO> tasks = taskMapper.selectListByDueTimeRange(now, remindEnd);

        int count = 0;
        for (CrmTaskDO task : tasks) {
            try {
                if (task.getOwnerUserId() != null) {
                    crmNotifyService.sendTaskDueRemind(task);
                    count++;
                }
            } catch (Exception e) {
                log.error("[CrmTaskRemindJob][任务({}) 提醒发送失败]", task.getId(), e);
            }
        }
        return String.format("发送任务截止提醒 %s 条", count);
    }

}
