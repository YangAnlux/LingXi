package com.meession.etm.module.crm.service.notify;

import com.meession.etm.module.crm.dal.dataobject.schedule.CrmScheduleDO;
import com.meession.etm.module.crm.dal.dataobject.task.CrmTaskDO;
import com.meession.etm.module.system.api.notify.NotifyMessageSendApi;
import com.meession.etm.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * CRM 站内信通知服务
 *
 * 封装向 Admin 用户发送站内信的逻辑
 */
@Service
@Slf4j
public class CrmNotifyService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * 任务分配通知模板编码
     */
    private static final String TEMPLATE_TASK_ASSIGN = "crm_task_assign";

    /**
     * 任务截止提醒通知模板编码
     */
    private static final String TEMPLATE_TASK_DUE = "crm_task_due";

    /**
     * 日程变更通知模板编码
     */
    private static final String TEMPLATE_SCHEDULE_CHANGE = "crm_schedule_change";

    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    /**
     * 发送任务分配通知
     */
    public void sendTaskAssignNotify(CrmTaskDO task) {
        if (task.getOwnerUserId() == null) return;
        Map<String, Object> params = new HashMap<>();
        params.put("taskName", task.getName());
        params.put("endTime", task.getEndTime() != null ? task.getEndTime().format(FORMATTER) : "无");
        sendNotify(task.getOwnerUserId(), TEMPLATE_TASK_ASSIGN, params);
    }

    /**
     * 发送任务截止日期提醒
     */
    public void sendTaskDueRemind(CrmTaskDO task) {
        if (task.getOwnerUserId() == null) return;
        Map<String, Object> params = new HashMap<>();
        params.put("taskName", task.getName());
        params.put("endTime", task.getEndTime() != null ? task.getEndTime().format(FORMATTER) : "无");
        sendNotify(task.getOwnerUserId(), TEMPLATE_TASK_DUE, params);
    }

    /**
     * 发送日程变更通知
     */
    public void sendScheduleChangeNotify(CrmScheduleDO schedule, String action) {
        if (schedule.getOwnerUserId() == null) return;
        Map<String, Object> params = new HashMap<>();
        params.put("scheduleTitle", schedule.getTitle());
        params.put("action", action);
        params.put("startTime", schedule.getStartTime() != null ? schedule.getStartTime().format(FORMATTER) : "无");
        sendNotify(schedule.getOwnerUserId(), TEMPLATE_SCHEDULE_CHANGE, params);
    }

    private void sendNotify(Long userId, String templateCode, Map<String, Object> params) {
        try {
            notifyMessageSendApi.sendSingleMessageToAdmin(
                    new NotifySendSingleToUserReqDTO()
                            .setUserId(userId)
                            .setTemplateCode(templateCode)
                            .setTemplateParams(params));
        } catch (Exception e) {
            log.error("[CrmNotifyService][发送站内信失败 userId={}, template={}]", userId, templateCode, e);
        }
    }

}
