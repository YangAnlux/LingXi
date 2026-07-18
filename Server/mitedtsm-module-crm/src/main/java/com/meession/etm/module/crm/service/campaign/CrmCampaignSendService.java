package com.meession.etm.module.crm.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.campaign.vo.send.*;
import com.meession.etm.module.system.dal.dataobject.mail.MailTemplateDO;
import com.meession.etm.module.system.dal.dataobject.sms.SmsTemplateDO;

import jakarta.validation.Valid;
import java.util.List;

public interface CrmCampaignSendService {

    /** 批量发送短信 */
    CrmCampaignSendRespVO sendSms(@Valid CrmCampaignSendReqVO reqVO, Long userId);

    /** 批量发送邮件 */
    CrmCampaignSendRespVO sendMail(@Valid CrmCampaignSendReqVO reqVO, Long userId);

    /** 获取启用的短信模板列表 */
    List<SmsTemplateDO> getSmsTemplateList();

    /** 获取启用的邮件模板列表 */
    List<MailTemplateDO> getMailTemplateList();

    /** 关怀日志分页 */
    PageResult<CrmCareLogRespVO> getCareLogPage(CrmCareLogPageReqVO pageReqVO);

    /** 发送日志分页 */
    PageResult<CrmCampaignSendLogRespVO> getSendLogPage(CrmCampaignSendLogPageReqVO pageReqVO);

}
