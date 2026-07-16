// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票Service实现 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.service.invoice;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO;
import com.meession.etm.module.crm.dal.mysql.invoice.CrmInvoiceMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceStatusEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

/**
 * CRM 发票 Service 实现类
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.14
 */
@Service
@Validated
@Slf4j
public class CrmInvoiceServiceImpl implements CrmInvoiceService {

    @Resource
    private CrmInvoiceMapper invoiceMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmPermissionService permissionService;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_INVOICE_TYPE, subType = CRM_INVOICE_CREATE_SUB_TYPE, bizNo = "{{#invoice.id}}",
            success = CRM_INVOICE_CREATE_SUCCESS)
    public Long createInvoice(CrmInvoiceSaveReqVO createReqVO) {
        // 1.1 校验可开票金额
        validateInvoicePriceExceedsLimit(createReqVO);
        // 1.2 校验关联数据存在
        validateRelationDataExists(createReqVO);
        // 1.3 校验发票号码唯一性
        validateInvoiceNoUnique(createReqVO.getInvoiceNo(), null);
        // 1.4 生成发票编号
        String no = noRedisDAO.generate(CrmNoRedisDAO.INVOICE_PREFIX);
        if (invoiceMapper.selectByNo(no) != null) {
            throw exception(INVOICE_NO_EXISTS);
        }

        // 2. 插入发票
        CrmInvoiceDO invoice = BeanUtils.toBean(createReqVO, CrmInvoiceDO.class)
                .setNo(no).setStatus(CrmInvoiceStatusEnum.PENDING.getStatus());
        invoiceMapper.insert(invoice);

        // 3. 创建数据权限
        permissionService.createPermission(new CrmPermissionCreateReqBO()
                .setBizType(CrmBizTypeEnum.CRM_INVOICE.getType())
                .setBizId(invoice.getId()).setUserId(createReqVO.getOwnerUserId())
                .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("invoice", invoice);
        return invoice.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_INVOICE_TYPE, subType = CRM_INVOICE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_INVOICE_UPDATE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_INVOICE, bizId = "#updateReqVO.id", level = CrmPermissionLevelEnum.WRITE)
    public void updateInvoice(CrmInvoiceSaveReqVO updateReqVO) {
        Assert.notNull(updateReqVO.getId(), "发票编号不能为空");
        // 1.1 校验存在
        CrmInvoiceDO oldInvoice = validateInvoiceExists(updateReqVO.getId());
        // 1.2 只有待开票状态可以编辑
        if (ObjUtil.notEqual(oldInvoice.getStatus(), CrmInvoiceStatusEnum.PENDING.getStatus())) {
            throw exception(INVOICE_UPDATE_FAIL_STATUS_NOT_PENDING);
        }
        // 1.3 校验发票号码唯一性
        if (ObjectUtil.notEqual(updateReqVO.getInvoiceNo(), oldInvoice.getInvoiceNo())) {
            validateInvoiceNoUnique(updateReqVO.getInvoiceNo(), updateReqVO.getId());
        }
        // 1.4 不允许修改的字段还原
        updateReqVO.setOwnerUserId(null).setCustomerId(null).setContractId(null);
        updateReqVO.setOwnerUserId(oldInvoice.getOwnerUserId()).setCustomerId(oldInvoice.getCustomerId())
                .setContractId(oldInvoice.getContractId());
        // 1.5 校验可开票金额
        validateInvoicePriceExceedsLimit(updateReqVO);

        // 2. 更新发票
        CrmInvoiceDO updateObj = BeanUtils.toBean(updateReqVO, CrmInvoiceDO.class);
        invoiceMapper.updateById(updateObj);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("invoice", oldInvoice);
        LogRecordContext.putVariable("invoiceNo", oldInvoice.getInvoiceNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_INVOICE_TYPE, subType = CRM_INVOICE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_INVOICE_DELETE_SUCCESS)
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_INVOICE, bizId = "#id", level = CrmPermissionLevelEnum.OWNER)
    public void deleteInvoice(Long id) {
        // 1. 校验存在且已开票的不能删除
        CrmInvoiceDO invoice = validateInvoiceExists(id);
        if (ObjUtil.notEqual(invoice.getStatus(), CrmInvoiceStatusEnum.PENDING.getStatus())) {
            throw exception(INVOICE_DELETE_FAIL_IS_ISSUED);
        }

        // 2. 删除发票
        invoiceMapper.deleteById(id);
        // 3. 删除数据权限
        permissionService.deletePermission(CrmBizTypeEnum.CRM_INVOICE.getType(), id);

        // 4. 记录操作日志上下文
        LogRecordContext.putVariable("invoice", invoice);
        LogRecordContext.putVariable("invoiceNo", invoice.getInvoiceNo());
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_INVOICE, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmInvoiceDO getInvoice(Long id) {
        return invoiceMapper.selectById(id);
    }

    @Override
    public List<CrmInvoiceDO> getInvoiceList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return invoiceMapper.selectByIds(ids);
    }

    @Override
    public PageResult<CrmInvoiceDO> getInvoicePage(CrmInvoicePageReqVO pageReqVO) {
        return invoiceMapper.selectPage(pageReqVO);
    }

    @Override
    public Map<Long, BigDecimal> getInvoiceAmountMapByContractId(Collection<Long> contractIds) {
        if (CollUtil.isEmpty(contractIds)) {
            return Collections.emptyMap();
        }
        Map<Long, BigDecimal> result = new HashMap<>();
        for (Long contractId : contractIds) {
            List<CrmInvoiceDO> invoices = invoiceMapper.selectListByContractId(contractId);
            BigDecimal total = invoices.stream()
                    .filter(i -> !CrmInvoiceStatusEnum.CANCELLED.getStatus().equals(i.getStatus()))
                    .map(CrmInvoiceDO::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put(contractId, total);
        }
        return result;
    }

    @Override
    public Long getInvoiceCountByContractId(Long contractId) {
        return invoiceMapper.selectCount(CrmInvoiceDO::getContractId, contractId);
    }

    // ========== 私有校验方法 ==========

    private void validateInvoicePriceExceedsLimit(CrmInvoiceSaveReqVO reqVO) {
        // 1. 计算剩余可开票金额，不包括 reqVO 自身
        CrmContractDO contract = contractService.validateContract(reqVO.getContractId());
        Set<Long> excludeIds = reqVO.getId() != null ? Collections.singleton(reqVO.getId()) : Collections.emptySet();
        BigDecimal invoicedAmount = invoiceMapper.selectSumAmountByContractId(reqVO.getContractId(), excludeIds);
        BigDecimal notInvoicedAmount = contract.getTotalPrice().subtract(invoicedAmount);
        // 2. 校验金额是否超过
        if (reqVO.getAmount().compareTo(notInvoicedAmount) > 0) {
            throw exception(INVOICE_CREATE_FAIL_PRICE_EXCEEDS_LIMIT, notInvoicedAmount);
        }
    }

    private void validateRelationDataExists(CrmInvoiceSaveReqVO reqVO) {
        if (reqVO.getOwnerUserId() != null) {
            adminUserApi.validateUser(reqVO.getOwnerUserId()); // 校验负责人存在
        }
        if (reqVO.getContractId() != null) {
            CrmContractDO contract = contractService.validateContract(reqVO.getContractId());
            reqVO.setCustomerId(contract.getCustomerId()); // 设置客户编号
        }
    }

    private void validateInvoiceNoUnique(String invoiceNo, Long excludeId) {
        CrmInvoiceDO existing = invoiceMapper.selectByInvoiceNo(invoiceNo);
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw exception(INVOICE_INVOICE_NO_EXISTS, invoiceNo);
        }
    }

    private CrmInvoiceDO validateInvoiceExists(Long id) {
        CrmInvoiceDO invoice = invoiceMapper.selectById(id);
        if (invoice == null) {
            throw exception(INVOICE_NOT_EXISTS);
        }
        return invoice;
    }

}
// [ADD END] 发票Service实现 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
