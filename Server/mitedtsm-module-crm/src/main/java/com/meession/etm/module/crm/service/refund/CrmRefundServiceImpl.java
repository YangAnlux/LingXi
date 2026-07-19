// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款Service实现 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.service.refund;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.dal.mysql.refund.CrmRefundMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.LogRecordConstants;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;
import static com.meession.etm.module.crm.util.CrmAuditStatusUtils.convertBpmResultToAuditStatus;

/**
 * CRM 退款 Service 实现类
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@Service
@Validated
@Slf4j
public class CrmRefundServiceImpl implements CrmRefundService {

    /**
     * BPM 退款审批流程标识
     */
    public static final String BPM_PROCESS_DEFINITION_KEY = "crm-refund-audit";

    @Resource
    private CrmRefundMapper refundMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService permissionService;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_CREATE_SUB_TYPE,
            bizNo = "{{#refund.id}}", success = CRM_REFUND_CREATE_SUCCESS)
    public Long createRefund(CrmRefundSaveReqVO createReqVO) {
        // 1. 插入
        CrmRefundDO refund = BeanUtils.toBean(createReqVO, CrmRefundDO.class);
        refund.setNo(noRedisDAO.generate(CrmNoRedisDAO.REFUND_PREFIX));
        refund.setStatus(0); // 待提交
        refund.setAuditStatus(CrmAuditStatusEnum.DRAFT.getStatus());
        refundMapper.insert(refund);
        // 2. 插入数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO()
                .setBizType(CrmBizTypeEnum.CRM_REFUND.getType())
                .setBizId(refund.getId())
                .setUserId(refund.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));
        // [ADD START] 将refund放入上下文供@LogRecord使用 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
        LogRecordContext.putVariable("refund", refund);
        // [ADD END] 将refund放入上下文供@LogRecord使用 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
        return refund.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_UPDATE_SUB_TYPE,
            bizNo = "{{#updateReqVO.id}}", success = CRM_REFUND_UPDATE_SUCCESS)
    public void updateRefund(CrmRefundSaveReqVO updateReqVO) {
        // 1. 校验存在
        CrmRefundDO oldRefund = validateRefundExists(updateReqVO.getId());
        // 2. 只有待提交状态可以编辑
        if (ObjUtil.notEqual(oldRefund.getStatus(), 0)) {
            throw exception(REFUND_UPDATE_FAIL_NOT_DRAFT);
        }
        // 3. 更新
        CrmRefundDO updateObj = BeanUtils.toBean(updateReqVO, CrmRefundDO.class);
        refundMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_DELETE_SUB_TYPE,
            bizNo = "{{#id}}", success = CRM_REFUND_DELETE_SUCCESS)
    public void deleteRefund(Long id) {
        // 1. 校验存在
        CrmRefundDO refund = validateRefundExists(id);
        // 2. 只有待提交状态可以删除
        if (ObjUtil.notEqual(refund.getStatus(), 0)) {
            throw exception(REFUND_DELETE_FAIL_NOT_DRAFT);
        }
        // 3. 删除
        refundMapper.deleteById(id);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REFUND, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmRefundDO getRefund(Long id) {
        return refundMapper.selectById(id);
    }

    @Override
    public List<CrmRefundDO> getRefundList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return refundMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CrmRefundDO> getRefundPage(CrmRefundPageReqVO pageReqVO) {
        return refundMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REFUND_TYPE, subType = CRM_REFUND_SUBMIT_SUB_TYPE,
            bizNo = "{{#id}}", success = CRM_REFUND_SUBMIT_SUCCESS)
    public void submitRefund(Long id, Long userId) {
        // 1. 校验退款是否在待提交状态
        CrmRefundDO refund = validateRefundExists(id);
        if (ObjUtil.notEqual(refund.getStatus(), 0)) {
            throw exception(REFUND_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建退款审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(BPM_PROCESS_DEFINITION_KEY)
                        .setBusinessKey(String.valueOf(id)));

        // 3. 更新退款状态为审批中
        refundMapper.updateById(new CrmRefundDO()
                .setId(id)
                .setProcessInstanceId(processInstanceId)
                .setAuditStatus(CrmAuditStatusEnum.PROCESS.getStatus())
                .setStatus(1)); // 审批中

        // 4. 记录日志
        LogRecordContext.putVariable("refundNo", refund.getNo());
    }

    @Override
    public void updateRefundAuditStatus(Long id, Integer bpmResult) {
        // 1.1 校验存在
        CrmRefundDO refund = validateRefundExists(id);
        // 1.2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(refund.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            log.error("[updateRefundAuditStatus][refund({}) 不处于审批中，无法更新审批结果({})]",
                    refund.getId(), bpmResult);
            throw exception(REFUND_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        // 2. 更新退款审批状态
        Integer auditStatus = convertBpmResultToAuditStatus(bpmResult);
        Integer status = CrmAuditStatusEnum.APPROVE.getStatus().equals(auditStatus) ? 2 // 已通过
                : CrmAuditStatusEnum.REJECT.getStatus().equals(auditStatus) ? 3         // 已驳回
                : refund.getStatus();
        refundMapper.updateById(new CrmRefundDO()
                .setId(id)
                .setAuditStatus(auditStatus)
                .setStatus(status));
    }

    /**
     * 校验退款是否存在
     */
    private CrmRefundDO validateRefundExists(Long id) {
        CrmRefundDO refund = refundMapper.selectById(id);
        if (refund == null) {
            throw exception(REFUND_NOT_EXISTS);
        }
        return refund;
    }

}
// [ADD END] 退款Service实现 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
