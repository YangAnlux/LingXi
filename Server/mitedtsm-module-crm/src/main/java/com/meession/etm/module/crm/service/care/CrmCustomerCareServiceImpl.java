package com.meession.etm.module.crm.service.care;

import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.dal.dataobject.care.CrmCareLogDO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmHolidayDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.mysql.care.CrmCareLogMapper;
import com.meession.etm.module.crm.dal.mysql.care.CrmHolidayMapper;
import com.meession.etm.module.crm.dal.mysql.customer.CrmCustomerMapper;
import com.meession.etm.module.crm.dal.redis.RedisKeyConstants;
import com.meession.etm.module.system.api.mail.MailSendApi;
import com.meession.etm.module.system.api.mail.dto.MailSendSingleToUserReqDTO;
import com.meession.etm.module.system.api.sms.SmsSendApi;
import com.meession.etm.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CrmCustomerCareServiceImpl implements CrmCustomerCareService {

    @Resource
    private CrmCustomerMapper customerMapper;
    @Resource
    private CrmCareLogMapper careLogMapper;
    @Resource
    private CrmHolidayMapper holidayMapper;
    @Resource
    private SmsSendApi smsSendApi;
    @Resource
    private MailSendApi mailSendApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeBirthdayCare() {
        LocalDate today = LocalDate.now();
        // 查询今天生日的客户（birthday不为null，且月日匹配）
        List<CrmCustomerDO> customers = customerMapper.selectList(
                new MPJLambdaWrapperX<CrmCustomerDO>()
                        .isNotNull(CrmCustomerDO::getBirthday)
                        .apply("MONTH(birthday) = {0} AND DAY(birthday) = {1}",
                                today.getMonthValue(), today.getDayOfMonth()));

        int count = 0;
        for (CrmCustomerDO customer : customers) {
            // Redis 去重：同一客户每天只发一次
            if (isDuplicate(customer.getId())) {
                log.info("[executeBirthdayCare][客户({})今日已发送关怀，跳过]", customer.getId());
                continue;
            }
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("name", customer.getName() != null ? customer.getName() : "客户");

                // 发送短信关怀
                if (customer.getMobile() != null && !customer.getMobile().isEmpty()) {
                    smsSendApi.sendSingleSmsToAdmin(new SmsSendSingleToUserReqDTO()
                            .setMobile(customer.getMobile())
                            .setTemplateCode("crm_birthday_care")
                            .setTemplateParams(params));
                    saveCareLog(customer, 1, 1, "crm_birthday_care", 0);
                    count++;
                }
                // 发送邮件关怀
                if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                    MailSendSingleToUserReqDTO mailReq = new MailSendSingleToUserReqDTO();
                    mailReq.setToMails(List.of(customer.getEmail()));
                    mailReq.setTemplateCode("crm_birthday_care");
                    mailReq.setTemplateParams(params);
                    mailSendApi.sendSingleMailToAdmin(mailReq);
                    saveCareLog(customer, 1, 2, "crm_birthday_care", 0);
                    count++;
                }
            } catch (Exception e) {
                log.error("[executeBirthdayCare][客户({})生日关怀发送失败]", customer.getId(), e);
                saveCareLog(customer, 1, 1, "crm_birthday_care", 1);
            }
        }
        log.info("[executeBirthdayCare][完成，共发送 {} 条关怀消息]", count);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeHolidayCare() {
        LocalDate today = LocalDate.now();
        // 查询今天配置的启用节日
        List<CrmHolidayDO> holidays = holidayMapper.selectListByDate(today);
        if (holidays.isEmpty()) {
            log.info("[executeHolidayCare][今天没有配置节日，跳过]");
            return 0;
        }

        int count = 0;
        for (CrmHolidayDO holiday : holidays) {
            String holidayName = holiday.getName();
            String templateCode = holiday.getTemplateCode() != null ? holiday.getTemplateCode() : "crm_holiday_care";
            log.info("[executeHolidayCare][开始处理节日: {}]", holidayName);

            // 查询所有有手机号或邮箱的客户
            List<CrmCustomerDO> customers = customerMapper.selectList(
                    new MPJLambdaWrapperX<CrmCustomerDO>()
                            .and(w -> w.isNotNull(CrmCustomerDO::getMobile)
                                    .or().isNotNull(CrmCustomerDO::getEmail)));

            for (CrmCustomerDO customer : customers) {
                // Redis 去重：同一客户每天只发一次
                if (isDuplicate(customer.getId())) {
                    log.info("[executeHolidayCare][客户({})今日已发送关怀，跳过]", customer.getId());
                    continue;
                }
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("name", customer.getName() != null ? customer.getName() : "客户");
                    params.put("holidayName", holidayName);

                    // 发送短信关怀
                    if (customer.getMobile() != null && !customer.getMobile().isEmpty()) {
                        smsSendApi.sendSingleSmsToAdmin(new SmsSendSingleToUserReqDTO()
                                .setMobile(customer.getMobile())
                                .setTemplateCode(templateCode)
                                .setTemplateParams(params));
                        saveCareLog(customer, 2, 1, templateCode, 0);
                        count++;
                    }
                    // 发送邮件关怀
                    if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                        MailSendSingleToUserReqDTO mailReq = new MailSendSingleToUserReqDTO();
                        mailReq.setToMails(List.of(customer.getEmail()));
                        mailReq.setTemplateCode(templateCode);
                        mailReq.setTemplateParams(params);
                        mailSendApi.sendSingleMailToAdmin(mailReq);
                        saveCareLog(customer, 2, 2, templateCode, 0);
                        count++;
                    }
                } catch (Exception e) {
                    log.error("[executeHolidayCare][客户({})节日关怀发送失败]", customer.getId(), e);
                    saveCareLog(customer, 2, 1, templateCode, 1);
                }
            }
        }
        log.info("[executeHolidayCare][完成，共发送 {} 条节日关怀消息]", count);
        return count;
    }

    /**
     * Redis SETNX 去重检查
     * 同一客户同一日期只允许发送一次关怀消息，锁定时长 24 小时
     * @return true = 已发过（应该跳过），false = 未发过（可以发送）
     */
    private boolean isDuplicate(Long customerId) {
        String today = LocalDate.now().toString();
        String key = RedisKeyConstants.CARE_SEND + customerId + ":" + today;
        Boolean locked = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofHours(24));
        return locked == null || !locked;
    }

    private void saveCareLog(CrmCustomerDO customer, int careType, int channel, String templateCode, int status) {
        CrmCareLogDO logDO = CrmCareLogDO.builder()
                .customerId(customer.getId())
                .careType(careType)
                .channel(channel)
                .templateCode(templateCode)
                .status(status)
                .sendTime(LocalDateTime.now())
                .build();
        careLogMapper.insert(logDO);
    }

}
