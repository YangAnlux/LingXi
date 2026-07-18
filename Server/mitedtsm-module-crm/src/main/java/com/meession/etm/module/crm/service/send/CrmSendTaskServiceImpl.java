package com.meession.etm.module.crm.service.send;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.config.CrmRabbitMQConfiguration;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskCreateReqVO;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskRespVO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskMessage;
import com.meession.etm.module.crm.dal.mysql.customer.CrmCustomerMapper;
import com.meession.etm.module.crm.dal.mysql.send.CrmSendTaskMapper;
import com.meession.etm.module.crm.service.utils.VariableResolver;
import com.meession.etm.module.system.dal.dataobject.mail.MailTemplateDO;
import com.meession.etm.module.system.dal.dataobject.sms.SmsTemplateDO;
import com.meession.etm.module.system.dal.mysql.mail.MailTemplateMapper;
import com.meession.etm.module.system.dal.mysql.sms.SmsTemplateMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CrmSendTaskServiceImpl implements CrmSendTaskService {

    @Resource
    private CrmSendTaskMapper sendTaskMapper;
    @Resource
    private CrmCustomerMapper customerMapper;
    @Resource
    private SmsTemplateMapper smsTemplateMapper;
    @Resource
    private MailTemplateMapper mailTemplateMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CrmSendTaskRespVO createTask(CrmSendTaskCreateReqVO reqVO, Long userId) {
        // 1. 获取模板内容
        String templateContent = getTemplateContent(reqVO.getTemplateCode(), reqVO.getChannel());
        if (templateContent == null) {
            throw new IllegalArgumentException("模板不存在: " + reqVO.getTemplateCode());
        }

        // 2. 根据选择模式获取客户列表
        List<CrmCustomerDO> customers = resolveCustomers(reqVO);

        // 3. 检查变量缺失情况
        Set<String> availableVars = Set.of("name", "customerName", "product", "holidayName",
                "campaignName", "code", "taskName", "endTime");
        List<String> missingVars = VariableResolver.checkMissingVars(templateContent, availableVars);
        int missingVarCount = missingVars.isEmpty() ? 0 : customers.size();

        // 4. 如果有缺失变量且非强制发送，返回警告
        if (!missingVars.isEmpty() && (reqVO.getForceSend() == null || !reqVO.getForceSend())) {
            CrmSendTaskRespVO warnResp = new CrmSendTaskRespVO();
            warnResp.setHasWarning(true);
            warnResp.setMissingVariables(missingVars);
            warnResp.setMissingVarCount(missingVarCount);
            return warnResp;
        }

        // 5. 创建任务记录
        CrmSendTaskDO task = CrmSendTaskDO.builder()
                .activityId(reqVO.getActivityId())
                .taskType(reqVO.getTaskType())
                .templateId(reqVO.getTemplateId())
                .templateCode(reqVO.getTemplateCode())
                .channel(reqVO.getChannel())
                .customerSelectMode(reqVO.getCustomerSelectMode())
                .customerCount(customers.size())
                .sendMode(reqVO.getSendMode())
                .scheduledTime(reqVO.getScheduledTime())
                .status("PENDING")
                .missingVarCount(missingVarCount)
                .build();
        sendTaskMapper.insert(task);

        // 6. 投递 RabbitMQ 消息
        List<Map<String, Object>> customerData = customers.stream().map(c -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("customerId", c.getId());
            m.put("name", c.getName() != null ? c.getName() : "");
            m.put("mobile", c.getMobile());
            m.put("email", c.getEmail());
            return m;
        }).collect(Collectors.toList());

        CrmSendTaskMessage message = CrmSendTaskMessage.builder()
                .taskId(task.getId())
                .activityId(reqVO.getActivityId())
                .bizType(reqVO.getTaskType())
                .channel(reqVO.getChannel())
                .templateCode(reqVO.getTemplateCode())
                .templateContent(templateContent)
                .customers(customerData)
                .userId(userId)
                .build();

        String routingKey = reqVO.getChannel() == 1
                ? CrmRabbitMQConfiguration.SMS_ROUTING_KEY
                : CrmRabbitMQConfiguration.EMAIL_ROUTING_KEY;
        rabbitTemplate.convertAndSend(CrmRabbitMQConfiguration.EXCHANGE_NAME, routingKey, message);

        log.info("[createTask][任务创建成功 taskId={}, channel={}, customerCount={}]",
                task.getId(), reqVO.getChannel(), customers.size());

        // 7. 返回结果
        CrmSendTaskRespVO resp = BeanUtils.toBean(task, CrmSendTaskRespVO.class);
        resp.setHasWarning(false);
        return resp;
    }

    @Override
    public PageResult<CrmSendTaskRespVO> getTaskPage(CrmSendTaskPageReqVO pageReqVO) {
        PageResult<CrmSendTaskDO> pageResult = sendTaskMapper.selectPage(pageReqVO);
        List<CrmSendTaskRespVO> voList = BeanUtils.toBean(pageResult.getList(), CrmSendTaskRespVO.class);
        return new PageResult<>(voList, pageResult.getTotal());
    }

    @Override
    public CrmSendTaskRespVO getTask(Long id) {
        CrmSendTaskDO task = sendTaskMapper.selectById(id);
        return BeanUtils.toBean(task, CrmSendTaskRespVO.class);
    }

    /**
     * 根据客户选择模式解析客户列表
     */
    private List<CrmCustomerDO> resolveCustomers(CrmSendTaskCreateReqVO reqVO) {
        return switch (reqVO.getCustomerSelectMode()) {
            case "ALL_ACTIVE" -> customerMapper.selectList();  // 全部活跃客户
            case "CSV", "BY_TAG" -> {
                if (reqVO.getCustomerIds() != null && !reqVO.getCustomerIds().isEmpty()) {
                    yield customerMapper.selectBatchIds(reqVO.getCustomerIds());
                }
                yield Collections.emptyList();
            }
            default -> {
                if (reqVO.getCustomerIds() != null && !reqVO.getCustomerIds().isEmpty()) {
                    yield customerMapper.selectBatchIds(reqVO.getCustomerIds());
                }
                yield Collections.emptyList();
            }
        };
    }

    /**
     * 获取模板原始内容
     */
    private String getTemplateContent(String templateCode, Integer channel) {
        if (channel == 1) {
            SmsTemplateDO sms = smsTemplateMapper.selectOne(
                    new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<SmsTemplateDO>()
                            .eq(SmsTemplateDO::getCode, templateCode));
            return sms != null ? sms.getContent() : null;
        } else {
            MailTemplateDO mail = mailTemplateMapper.selectOne(
                    new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<MailTemplateDO>()
                            .eq(MailTemplateDO::getCode, templateCode));
            return mail != null ? mail.getContent() : null;
        }
    }

}
