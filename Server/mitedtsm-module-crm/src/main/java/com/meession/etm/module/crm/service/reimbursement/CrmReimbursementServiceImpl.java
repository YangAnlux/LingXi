// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销Service实现 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.service.reimbursement;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.dal.mysql.reimbursement.CrmReimbursementMapper;
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
 * CRM 报销 Service 实现类
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@Service
@Validated
@Slf4j
public class CrmReimbursementServiceImpl implements CrmReimbursementService {

    /**
     * BPM 报销审批流程标识
     */
    public static final String BPM_PROCESS_DEFINITION_KEY = "crm-reimbursement-audit";

    @Resource
    private CrmReimbursementMapper reimbursementMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService permissionService;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_CREATE_SUB_TYPE,
            bizNo = "{{#reimbursement.id}}", success = CRM_REIMBURSEMENT_CREATE_SUCCESS)
    public Long createReimbursement(CrmReimbursementSaveReqVO createReqVO) {
        // 1. 插入
        CrmReimbursementDO reimbursement = BeanUtils.toBean(createReqVO, CrmReimbursementDO.class);
        reimbursement.setNo(noRedisDAO.generate(CrmNoRedisDAO.REIMBURSEMENT_PREFIX));
        reimbursement.setStatus(0); // 待提交
        reimbursement.setAuditStatus(CrmAuditStatusEnum.DRAFT.getStatus());
        reimbursementMapper.insert(reimbursement);
        // 3. 插入数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO()
                .setBizType(CrmBizTypeEnum.CRM_REIMBURSEMENT.getType())
                .setBizId(reimbursement.getId())
                .setUserId(reimbursement.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));
        return reimbursement.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_UPDATE_SUB_TYPE,
            bizNo = "{{#updateReqVO.id}}", success = CRM_REIMBURSEMENT_UPDATE_SUCCESS)
    public void updateReimbursement(CrmReimbursementSaveReqVO updateReqVO) {
        // 1. 校验存在
        CrmReimbursementDO oldReimbursement = validateReimbursementExists(updateReqVO.getId());
        // 2. 只有待提交状态可以编辑
        if (ObjUtil.notEqual(oldReimbursement.getStatus(), 0)) {
            throw exception(REIMBURSEMENT_UPDATE_FAIL_NOT_DRAFT);
        }
        // 3. 更新
        CrmReimbursementDO updateObj = BeanUtils.toBean(updateReqVO, CrmReimbursementDO.class);
        reimbursementMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_DELETE_SUB_TYPE,
            bizNo = "{{#id}}", success = CRM_REIMBURSEMENT_DELETE_SUCCESS)
    public void deleteReimbursement(Long id) {
        // 1. 校验存在
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        // 2. 只有待提交状态可以删除
        if (ObjUtil.notEqual(reimbursement.getStatus(), 0)) {
            throw exception(REIMBURSEMENT_DELETE_FAIL_NOT_DRAFT);
        }
        // 3. 删除
        reimbursementMapper.deleteById(id);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_REIMBURSEMENT, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmReimbursementDO getReimbursement(Long id) {
        return reimbursementMapper.selectById(id);
    }

    @Override
    public List<CrmReimbursementDO> getReimbursementList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return reimbursementMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CrmReimbursementDO> getReimbursementPage(CrmReimbursementPageReqVO pageReqVO) {
        return reimbursementMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_REIMBURSEMENT_TYPE, subType = CRM_REIMBURSEMENT_SUBMIT_SUB_TYPE,
            bizNo = "{{#id}}", success = CRM_REIMBURSEMENT_SUBMIT_SUCCESS)
    public void submitReimbursement(Long id, Long userId) {
        // 1. 校验报销是否在待提交状态
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        if (ObjUtil.notEqual(reimbursement.getStatus(), 0)) {
            throw exception(REIMBURSEMENT_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建报销审批流程实例
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(BPM_PROCESS_DEFINITION_KEY)
                        .setBusinessKey(String.valueOf(id)));

        // 3. 更新报销状态为审批中
        reimbursementMapper.updateById(new CrmReimbursementDO()
                .setId(id)
                .setProcessInstanceId(processInstanceId)
                .setAuditStatus(CrmAuditStatusEnum.PROCESS.getStatus())
                .setStatus(1)); // 审批中

        // 4. 记录日志
        LogRecordContext.putVariable("reimbursementNo", reimbursement.getNo());
    }

    @Override
    public void updateReimbursementAuditStatus(Long id, Integer bpmResult) {
        // 1.1 校验存在
        CrmReimbursementDO reimbursement = validateReimbursementExists(id);
        // 1.2 只有审批中，可以更新审批结果
        if (ObjUtil.notEqual(reimbursement.getAuditStatus(), CrmAuditStatusEnum.PROCESS.getStatus())) {
            log.error("[updateReimbursementAuditStatus][reimbursement({}) 不处于审批中，无法更新审批结果({})]",
                    reimbursement.getId(), bpmResult);
            throw exception(REIMBURSEMENT_UPDATE_AUDIT_STATUS_FAIL_NOT_PROCESS);
        }

        // 2. 更新报销审批状态
        Integer auditStatus = convertBpmResultToAuditStatus(bpmResult);
        Integer status = CrmAuditStatusEnum.APPROVE.getStatus().equals(auditStatus) ? 2 // 已通过
                : CrmAuditStatusEnum.REJECT.getStatus().equals(auditStatus) ? 3         // 已驳回
                : reimbursement.getStatus();
        reimbursementMapper.updateById(new CrmReimbursementDO()
                .setId(id)
                .setAuditStatus(auditStatus)
                .setStatus(status));
    }

    /**
     * 校验报销是否存在
     */
    private CrmReimbursementDO validateReimbursementExists(Long id) {
        CrmReimbursementDO reimbursement = reimbursementMapper.selectById(id);
        if (reimbursement == null) {
            throw exception(REIMBURSEMENT_NOT_EXISTS);
        }
        return reimbursement;
    }

}
// [ADD END] 报销Service实现 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
