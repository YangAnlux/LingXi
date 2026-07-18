package com.meession.etm.module.crm.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.campaign.vo.send.*;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignSendLogDO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmCareLogDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignMapper;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignSendLogMapper;
import com.meession.etm.module.crm.dal.mysql.care.CrmCareLogMapper;
import com.meession.etm.module.crm.dal.mysql.customer.CrmCustomerMapper;
import com.meession.etm.module.system.api.mail.MailSendApi;
import com.meession.etm.module.system.api.mail.dto.MailSendSingleToUserReqDTO;
import com.meession.etm.module.system.api.sms.SmsSendApi;
import com.meession.etm.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import com.meession.etm.module.system.dal.dataobject.mail.MailTemplateDO;
import com.meession.etm.module.system.dal.dataobject.sms.SmsTemplateDO;
import com.meession.etm.module.system.dal.mysql.mail.MailTemplateMapper;
import com.meession.etm.module.system.dal.mysql.sms.SmsTemplateMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CrmCampaignSendServiceImpl implements CrmCampaignSendService {

    @Resource
    private SmsSendApi smsSendApi;
    @Resource
    private MailSendApi mailSendApi;
    @Resource
    private CrmCustomerMapper customerMapper;
    @Resource
    private CrmCampaignSendLogMapper sendLogMapper;
    @Resource
    private CrmCareLogMapper careLogMapper;
    @Resource
    private CrmCampaignMapper crmCampaignMapper;
    @Resource
    private SmsTemplateMapper smsTemplateMapper;
    @Resource
    private MailTemplateMapper mailTemplateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CrmCampaignSendRespVO sendSms(CrmCampaignSendReqVO reqVO, Long userId) {
        List<CrmCustomerDO> customers = customerMapper.selectBatchIds(reqVO.getCustomerIds());
        int success = 0, fail = 0, skip = 0;

        for (CrmCustomerDO customer : customers) {
            // 过滤无手机号的客户
            if (customer.getMobile() == null || customer.getMobile().isBlank()) {
                log.warn("[sendSms][客户({})无手机号，跳过]", customer.getId());
                skip++;
                continue;
            }
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("name", customer.getName() != null ? customer.getName() : "");
                // 提供常见模板参数默认值，避免因缺少参数而失败
                params.putIfAbsent("code", "");
                params.putIfAbsent("holidayName", "");
                params.putIfAbsent("campaignName", "");
                SmsSendSingleToUserReqDTO smsReq = new SmsSendSingleToUserReqDTO()
                        .setMobile(customer.getMobile())
                        .setTemplateCode(reqVO.getTemplateCode())
                        .setTemplateParams(params);
                smsSendApi.sendSingleSmsToAdmin(smsReq);
                success++;
            } catch (Exception e) {
                log.error("[sendSms][客户({})短信发送失败]", customer.getId(), e);
                fail++;
            }
        }

        CrmCampaignSendLogDO logDO = CrmCampaignSendLogDO.builder()
                .campaignId(reqVO.getCampaignId())
                .channel(1)
                .templateCode(reqVO.getTemplateCode())
                .totalCount(success + fail)
                .successCount(success)
                .failCount(fail)
                .sendTime(LocalDateTime.now())
                .build();
        sendLogMapper.insert(logDO);

        return BeanUtils.toBean(logDO, CrmCampaignSendRespVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CrmCampaignSendRespVO sendMail(CrmCampaignSendReqVO reqVO, Long userId) {
        List<CrmCustomerDO> customers = customerMapper.selectBatchIds(reqVO.getCustomerIds());
        int success = 0, fail = 0, skip = 0;

        for (CrmCustomerDO customer : customers) {
            // 过滤无邮箱的客户
            if (customer.getEmail() == null || customer.getEmail().isBlank()) {
                log.warn("[sendMail][客户({})无邮箱，跳过]", customer.getId());
                skip++;
                continue;
            }
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("name", customer.getName() != null ? customer.getName() : "");
                // 提供常见模板参数默认值，避免因缺少参数而失败
                params.putIfAbsent("code", "");
                params.putIfAbsent("holidayName", "");
                params.putIfAbsent("campaignName", "");
                MailSendSingleToUserReqDTO mailReq = new MailSendSingleToUserReqDTO();
                mailReq.setToMails(customer.getEmail() != null ? List.of(customer.getEmail()) : null);
                mailReq.setTemplateCode(reqVO.getTemplateCode());
                mailReq.setTemplateParams(params);
                mailSendApi.sendSingleMailToAdmin(mailReq);
                success++;
            } catch (Exception e) {
                log.error("[sendMail][客户({})邮件发送失败]", customer.getId(), e);
                fail++;
            }
        }

        CrmCampaignSendLogDO logDO = CrmCampaignSendLogDO.builder()
                .campaignId(reqVO.getCampaignId())
                .channel(2)
                .templateCode(reqVO.getTemplateCode())
                .totalCount(success + fail)
                .successCount(success)
                .failCount(fail)
                .sendTime(LocalDateTime.now())
                .build();
        sendLogMapper.insert(logDO);

        return BeanUtils.toBean(logDO, CrmCampaignSendRespVO.class);
    }

    @Override
    public List<SmsTemplateDO> getSmsTemplateList() {
        return smsTemplateMapper.selectList(
                new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<SmsTemplateDO>()
                        .eq(SmsTemplateDO::getStatus, 0)
                        .orderByAsc(SmsTemplateDO::getId));
    }

    @Override
    public List<MailTemplateDO> getMailTemplateList() {
        return mailTemplateMapper.selectList(
                new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<MailTemplateDO>()
                        .eq(MailTemplateDO::getStatus, 0)
                        .orderByAsc(MailTemplateDO::getId));
    }

    @Override
    public PageResult<CrmCareLogRespVO> getCareLogPage(CrmCareLogPageReqVO pageReqVO) {
        PageResult<CrmCareLogDO> pageResult = careLogMapper.selectPage(pageReqVO);
        List<CrmCareLogRespVO> voList = BeanUtils.toBean(pageResult.getList(), CrmCareLogRespVO.class);

        // 批量查询客户名称
        List<Long> customerIds = pageResult.getList().stream()
                .map(CrmCareLogDO::getCustomerId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        if (!customerIds.isEmpty()) {
            Map<Long, CrmCustomerDO> customerMap = new HashMap<>();
            customerMapper.selectBatchIds(customerIds).forEach(c -> customerMap.put(c.getId(), c));
            voList.forEach(vo -> {
                CrmCustomerDO customer = customerMap.get(vo.getCustomerId());
                if (customer != null) {
                    vo.setCustomerName(customer.getName());
                }
            });
        }

        return new PageResult<>(voList, pageResult.getTotal());
    }

    @Override
    public PageResult<CrmCampaignSendLogRespVO> getSendLogPage(CrmCampaignSendLogPageReqVO pageReqVO) {
        PageResult<CrmCampaignSendLogDO> pageResult = sendLogMapper.selectPage(pageReqVO);
        List<CrmCampaignSendLogRespVO> voList = BeanUtils.toBean(pageResult.getList(), CrmCampaignSendLogRespVO.class);

        // 批量查询营销活动名称
        List<Long> campaignIds = pageResult.getList().stream()
                .map(CrmCampaignSendLogDO::getCampaignId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        if (!campaignIds.isEmpty()) {
            Map<Long, CrmCampaignDO> campaignMap = new HashMap<>();
            crmCampaignMapper.selectBatchIds(campaignIds).forEach(c -> campaignMap.put(c.getId(), c));
            voList.forEach(vo -> {
                CrmCampaignDO campaign = campaignMap.get(vo.getCampaignId());
                if (campaign != null) {
                    vo.setCampaignName(campaign.getName());
                }
            });
        }

        return new PageResult<>(voList, pageResult.getTotal());
    }

}
