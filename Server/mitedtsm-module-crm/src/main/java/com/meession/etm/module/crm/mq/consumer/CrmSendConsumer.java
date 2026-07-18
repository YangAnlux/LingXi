package com.meession.etm.module.crm.mq.consumer;

import com.meession.etm.module.crm.config.CrmRabbitMQConfiguration;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignSendLogDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendDetailLogDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskMessage;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignSendLogMapper;
import com.meession.etm.module.crm.dal.mysql.send.CrmSendDetailLogMapper;
import com.meession.etm.module.crm.dal.mysql.send.CrmSendTaskMapper;
import com.meession.etm.module.crm.service.channel.ChannelService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * CRM 发送消费者（纯文字版）
 * 监听 sms/email 队列，逐条发送并写入明细日志
 */
@Component
@Slf4j
public class CrmSendConsumer {

    @Resource
    private ChannelService channelService;
    @Resource
    private CrmCampaignSendLogMapper sendLogMapper;
    @Resource
    private CrmSendTaskMapper sendTaskMapper;
    @Resource
    private CrmSendDetailLogMapper detailLogMapper;

    @RabbitListener(queues = CrmRabbitMQConfiguration.SMS_QUEUE)
    public void onSmsMessage(CrmSendTaskMessage msg) {
        log.info("[CrmSendConsumer][收到短信发送任务 taskId={}, count={}]",
                msg.getTaskId(), msg.getCustomers().size());
        processSend(msg, 1);
    }

    @RabbitListener(queues = CrmRabbitMQConfiguration.EMAIL_QUEUE)
    public void onEmailMessage(CrmSendTaskMessage msg) {
        log.info("[CrmSendConsumer][收到邮件发送任务 taskId={}, count={}]",
                msg.getTaskId(), msg.getCustomers().size());
        processSend(msg, 2);
    }

    private void processSend(CrmSendTaskMessage msg, int channel) {
        int success = 0, fail = 0;

        // 更新任务状态为 RUNNING
        updateTaskStatus(msg.getTaskId(), "RUNNING");

        // 获取所有待发送的明细日志
        java.util.List<CrmSendDetailLogDO> detailLogs = detailLogMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CrmSendDetailLogDO>()
                        .eq(CrmSendDetailLogDO::getTaskId, msg.getTaskId()));

        for (CrmSendDetailLogDO detailLog : detailLogs) {
            try {
                String recipient = detailLog.getRecipient();
                String resolvedContent = msg.getTemplateContent();

                // 从消息中查找对应的客户数据
                Map<String, Object> customer = findCustomer(msg, recipient);
                if (customer != null && customer.containsKey("resolvedContent")) {
                    resolvedContent = (String) customer.get("resolvedContent");
                }

                if (channel == 1) {
                    if (recipient == null || recipient.isBlank()) {
                        updateDetailLog(detailLog, 1, "手机号为空");
                        fail++;
                        continue;
                    }
                    boolean ok = channelService.sendSms(recipient, msg.getTemplateCode(),
                            java.util.Map.of("name", getParam(customer, "name", "贵宾")));
                    if (!ok) throw new RuntimeException("短信发送失败");
                } else {
                    if (recipient == null || recipient.isBlank()) {
                        updateDetailLog(detailLog, 1, "邮箱为空");
                        fail++;
                        continue;
                    }
                    // 纯文字邮件：不注入追踪像素和点击追踪，直接发送 text/plain
                    boolean ok = channelService.sendMail(recipient, msg.getTemplateCode(),
                            java.util.Map.of("name", getParam(customer, "name", "贵宾"),
                                    "_trackedContent", resolvedContent));
                    if (!ok) throw new RuntimeException("邮件发送失败");
                }

                updateDetailLog(detailLog, 0, null);
                success++;
            } catch (Exception e) {
                log.error("[CrmSendConsumer][发送失败 recipient={}, channel={}]",
                        detailLog.getRecipient(), channel, e);
                updateDetailLog(detailLog, 1, e.getMessage());
                fail++;
            }
        }

        // 写入 MySQL 汇总日志
        CrmCampaignSendLogDO logDO = CrmCampaignSendLogDO.builder()
                .campaignId(msg.getActivityId())
                .channel(channel)
                .templateCode(msg.getTemplateCode())
                .totalCount(success + fail)
                .successCount(success)
                .failCount(fail)
                .sendTime(LocalDateTime.now())
                .remark("群发发送完成")
                .build();
        sendLogMapper.insert(logDO);

        // 更新任务状态为 COMPLETED
        updateTaskStatus(msg.getTaskId(), "COMPLETED");

        log.info("[CrmSendConsumer][任务完成 taskId={}, success={}, fail={}]",
                msg.getTaskId(), success, fail);
    }

    private Map<String, Object> findCustomer(CrmSendTaskMessage msg, String recipient) {
        for (Map<String, Object> c : msg.getCustomers()) {
            if (recipient.equals(c.get("recipient"))) {
                return c;
            }
        }
        return null;
    }

    private String getParam(Map<String, Object> customer, String key, String defaultValue) {
        if (customer == null) return defaultValue;
        Object val = customer.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    private void updateDetailLog(CrmSendDetailLogDO detail, int status, String failReason) {
        detail.setStatus(status);
        detail.setFailReason(failReason);
        detail.setSendTime(LocalDateTime.now());
        detailLogMapper.updateById(detail);
    }

    private void updateTaskStatus(Long taskId, String status) {
        try {
            CrmSendTaskDO task = sendTaskMapper.selectById(taskId);
            if (task != null) {
                task.setStatus(status);
                sendTaskMapper.updateById(task);
            }
        } catch (Exception e) {
            log.warn("[CrmSendConsumer][更新任务状态失败 taskId={}]", taskId, e);
        }
    }
}
