// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 回款逾期扫描定时任务 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.job.receivable;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.meession.etm.framework.quartz.core.handler.JobHandler;
import com.meession.etm.framework.tenant.core.job.TenantJob;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivablePlanMapper;
import com.meession.etm.module.system.api.mail.MailSendApi;
import com.meession.etm.module.system.api.mail.dto.MailSendSingleToUserReqDTO;
import com.meession.etm.module.system.api.notify.NotifyMessageSendApi;
import com.meession.etm.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回款逾期扫描定时任务
 * <p>
 * 每天凌晨 3 点扫描未回款且已过 returnTime 的计划，按逾期天数分级：
 * 1-3 天 → 预警、4-7 天 → 警告、8+ 天 → 严重
 * <p>
 * 对每笔逾期计划发送站内信（系统消息）+ 邮件通知给负责人。
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@Component
@Slf4j
public class CrmReceivableJob implements JobHandler {

    private static final String NOTIFY_TEMPLATE_CODE = "crm_receivable_overdue";
    private static final String MAIL_TEMPLATE_CODE = "crm-receivable-overdue-mail";

    @Resource
    private CrmReceivablePlanMapper receivablePlanMapper;

    // [ADD START] 通知服务注入 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
    @Resource
    private NotifyMessageSendApi notifyMessageSendApi;

    @Resource
    private MailSendApi mailSendApi;
    // [ADD END] 通知服务注入 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16

    @Override
    @TenantJob
    public String execute(String param) {
        LocalDateTime today = LocalDateTimeUtil.beginOfDay(LocalDateTime.now());
        List<CrmReceivablePlanDO> overduePlans = receivablePlanMapper.selectOverduePlans(today);

        int warnCount = 0;   // 1-3 天
        int alertCount = 0;  // 4-7 天
        int severeCount = 0; // 8+ 天
        int notifySent = 0;  // 站内信发送数
        int mailSent = 0;    // 邮件发送数

        for (CrmReceivablePlanDO plan : overduePlans) {
            long overdueDays = ChronoUnit.DAYS.between(
                    LocalDateTimeUtil.beginOfDay(plan.getReturnTime()), today);
            String level;
            if (overdueDays >= 8) {
                severeCount++;
                level = "严重告警";
            } else if (overdueDays >= 4) {
                alertCount++;
                level = "警告";
            } else {
                warnCount++;
                level = "预警";
            }

            // [ADD START] 发送站内信 + 邮件 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
            try {
                Map<String, Object> templateParams = new HashMap<>();
                templateParams.put("period", String.valueOf(plan.getPeriod()));
                templateParams.put("overdueDays", String.valueOf(overdueDays));
                templateParams.put("price", plan.getPrice() != null ? plan.getPrice().toString() : "0");
                templateParams.put("returnTime", plan.getReturnTime() != null
                        ? plan.getReturnTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-");
                templateParams.put("level", level);

                // 站内信
                if (plan.getOwnerUserId() != null) {
                    NotifySendSingleToUserReqDTO notifyReq = new NotifySendSingleToUserReqDTO();
                    notifyReq.setUserId(plan.getOwnerUserId());
                    notifyReq.setTemplateCode(NOTIFY_TEMPLATE_CODE);
                    notifyReq.setTemplateParams(templateParams);
                    notifyMessageSendApi.sendSingleMessageToAdmin(notifyReq);
                    notifySent++;
                }

                // 邮件
                if (plan.getOwnerUserId() != null) {
                    MailSendSingleToUserReqDTO mailReq = new MailSendSingleToUserReqDTO();
                    mailReq.setUserId(plan.getOwnerUserId());
                    mailReq.setTemplateCode(MAIL_TEMPLATE_CODE);
                    // 邮件需要 contractNo，这里用 plan id 替代
                    Map<String, Object> mailParams = new HashMap<>(templateParams);
                    mailParams.put("contractNo", "合同ID:" + plan.getContractId());
                    mailReq.setTemplateParams(mailParams);
                    mailSendApi.sendSingleMailToAdmin(mailReq);
                    mailSent++;
                }
            } catch (Exception e) {
                log.error("[CrmReceivableJob][plan({}) 发送通知失败]", plan.getId(), e);
            }
            // [ADD END] 发送站内信 + 邮件 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
        }

        String result = String.format(
                "扫描 %d 条逾期回款计划：预警 %d、警告 %d、严重 %d，发送站内信 %d 封、邮件 %d 封",
                overduePlans.size(), warnCount, alertCount, severeCount, notifySent, mailSent);
        log.info("[CrmReceivableJob] {}", result);
        return result;
    }

}
// [ADD END] 回款逾期扫描定时任务 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
