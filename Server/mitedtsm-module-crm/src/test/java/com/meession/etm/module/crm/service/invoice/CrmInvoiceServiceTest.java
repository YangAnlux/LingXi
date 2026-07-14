// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票Service单元测试 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.service.invoice;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO;
import com.meession.etm.module.crm.dal.mysql.invoice.CrmInvoiceMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceStatusEnum;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceTypeEnum;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Import(CrmInvoiceServiceImpl.class)
public class CrmInvoiceServiceTest extends BaseDbUnitTest {

    @Resource
    private CrmInvoiceServiceImpl invoiceService;

    @Resource
    private CrmInvoiceMapper invoiceMapper;

    @MockitoBean
    private CrmNoRedisDAO noRedisDAO;
    @MockitoBean
    private CrmContractService contractService;
    @MockitoBean
    private CrmPermissionService permissionService;
    @MockitoBean
    private AdminUserApi adminUserApi;

    // ======================= 创建发票 =======================

    // 测试目的：验证正常创建发票时能够成功持久化并返回ID
    @Test
    public void testCreateInvoice_success() {
        // 准备 mock
        when(noRedisDAO.generate(CrmNoRedisDAO.INVOICE_PREFIX)).thenReturn("FP20260714000001");
        CrmContractDO contract = randomPojo(CrmContractDO.class, o -> o.setId(1L).setTotalPrice(new BigDecimal("10000.00")));
        when(contractService.validateContract(eq(1L))).thenReturn(contract);

        CrmInvoiceSaveReqVO reqVO = randomPojo(CrmInvoiceSaveReqVO.class, o -> {
            o.setId(null);
            o.setInvoiceNo("INV-TEST-001");
            o.setContractId(1L);
            o.setOwnerUserId(1L);
            o.setType(CrmInvoiceTypeEnum.GENERAL.getType());
            o.setAmount(new BigDecimal("5000.00"));
        });

        // 执行
        Long id = invoiceService.createInvoice(reqVO);

        // 断言
        assertNotNull(id);
        CrmInvoiceDO saved = invoiceMapper.selectById(id);
        assertNotNull(saved);
        assertEquals("FP20260714000001", saved.getNo());
        assertEquals("INV-TEST-001", saved.getInvoiceNo());
        assertEquals(CrmInvoiceStatusEnum.PENDING.getStatus(), saved.getStatus());
    }

    // 测试目的：验证发票号码重复时抛出 INVOICE_INVOICE_NO_EXISTS 异常
    @Test
    public void testCreateInvoice_invoiceNoDuplicate() {
        // 准备：先插入一条记录，再尝试创建相同 invoiceNo
        when(noRedisDAO.generate(CrmNoRedisDAO.INVOICE_PREFIX)).thenReturn("FP20260714000002");
        CrmContractDO contract = randomPojo(CrmContractDO.class, o -> o.setId(1L).setTotalPrice(new BigDecimal("10000.00")));
        when(contractService.validateContract(eq(1L))).thenReturn(contract);

        // 先通过真实 mapper 插入一条发票（模拟已存在的号码）
        CrmInvoiceDO existing = randomPojo(CrmInvoiceDO.class, o -> o.setInvoiceNo("INV-DUP"));
        invoiceMapper.insert(existing);

        CrmInvoiceSaveReqVO reqVO = randomPojo(CrmInvoiceSaveReqVO.class, o -> {
            o.setId(null);
            o.setInvoiceNo("INV-DUP");
            o.setContractId(1L);
            o.setOwnerUserId(1L);
            o.setAmount(new BigDecimal("1000.00"));
        });

        // 执行 & 断言
        assertServiceException(() -> invoiceService.createInvoice(reqVO), INVOICE_INVOICE_NO_EXISTS, "INV-DUP");
    }

    // 测试目的：验证开票金额超出合同剩余可开票金额时拒绝创建
    @Test
    public void testCreateInvoice_amountExceedsLimit() {
        // 准备：合同总额 10000，先插入一笔已开票 8000，再创建 3000 触发超限
        when(noRedisDAO.generate(CrmNoRedisDAO.INVOICE_PREFIX)).thenReturn("FP20260714000003");
        CrmContractDO contract = randomPojo(CrmContractDO.class, o -> o.setId(1L).setTotalPrice(new BigDecimal("10000.00")));
        when(contractService.validateContract(eq(1L))).thenReturn(contract);

        // 先插入一条已开票 8000 的记录
        CrmInvoiceDO existing = randomPojo(CrmInvoiceDO.class, o -> {
            o.setContractId(1L);
            o.setAmount(new BigDecimal("8000.00"));
            o.setStatus(CrmInvoiceStatusEnum.ISSUED.getStatus());
        });
        invoiceMapper.insert(existing);

        CrmInvoiceSaveReqVO reqVO = randomPojo(CrmInvoiceSaveReqVO.class, o -> {
            o.setId(null);
            o.setInvoiceNo("INV-OVER");
            o.setContractId(1L);
            o.setOwnerUserId(1L);
            o.setAmount(new BigDecimal("3000.00")); // 8000+3000=11000 > 10000
        });

        // 执行 & 断言
        assertServiceException(() -> invoiceService.createInvoice(reqVO), INVOICE_CREATE_FAIL_PRICE_EXCEEDS_LIMIT, new BigDecimal("2000.000000"));
    }

    // 测试目的：验证部分开票场景——合同剩余金额足够时成功创建
    @Test
    public void testCreateInvoice_partialInvoicing_success() {
        // 准备：合同总额 10000，本次开 2000
        when(noRedisDAO.generate(CrmNoRedisDAO.INVOICE_PREFIX)).thenReturn("FP20260714000004");
        CrmContractDO contract = randomPojo(CrmContractDO.class, o -> o.setId(1L).setTotalPrice(new BigDecimal("10000.00")));
        when(contractService.validateContract(eq(1L))).thenReturn(contract);

        CrmInvoiceSaveReqVO reqVO = randomPojo(CrmInvoiceSaveReqVO.class, o -> {
            o.setId(null);
            o.setInvoiceNo("INV-PARTIAL");
            o.setContractId(1L);
            o.setOwnerUserId(1L);
            o.setType(CrmInvoiceTypeEnum.SPECIAL.getType());
            o.setAmount(new BigDecimal("2000.00"));
        });

        // 执行
        Long id = invoiceService.createInvoice(reqVO);
        assertNotNull(id);
        CrmInvoiceDO saved = invoiceMapper.selectById(id);
        assertEquals(0, new BigDecimal("2000.00").compareTo(saved.getAmount()));
    }

    // 测试目的：验证基础字段缺失导致的创建失败
    @Test
    public void testCreateInvoice_missingRequiredFields_validationError() {
        CrmInvoiceSaveReqVO reqVO = new CrmInvoiceSaveReqVO();
        reqVO.setOwnerUserId(1L);
        reqVO.setAmount(new BigDecimal("100.00"));
        // contractId 和 invoiceNo 均为 null

        assertThrows(Exception.class, () -> invoiceService.createInvoice(reqVO));
    }

    // ======================= 更新发票 =======================

    // 测试目的：验证更新已存在的待开票发票成功
    @Test
    public void testUpdateInvoice_success() {
        // 准备：插入待开票发票
        CrmInvoiceDO dbInvoice = randomPojo(CrmInvoiceDO.class, o -> {
            o.setStatus(CrmInvoiceStatusEnum.PENDING.getStatus());
            o.setInvoiceNo("INV-OLD");
            o.setContractId(1L);
            o.setOwnerUserId(1L);
            o.setAmount(new BigDecimal("1000.00"));
        });
        invoiceMapper.insert(dbInvoice);

        CrmContractDO contract = randomPojo(CrmContractDO.class, o -> o.setId(1L).setTotalPrice(new BigDecimal("10000.00")));
        when(contractService.validateContract(eq(1L))).thenReturn(contract);

        CrmInvoiceSaveReqVO reqVO = randomPojo(CrmInvoiceSaveReqVO.class, o -> {
            o.setId(dbInvoice.getId());
            o.setInvoiceNo("INV-UPDATED");
            o.setContractId(1L);
            o.setOwnerUserId(1L);
            o.setAmount(new BigDecimal("2000.00"));
        });

        // 执行
        invoiceService.updateInvoice(reqVO);

        // 断言
        CrmInvoiceDO updated = invoiceMapper.selectById(dbInvoice.getId());
        assertEquals("INV-UPDATED", updated.getInvoiceNo());
        assertEquals(0, new BigDecimal("2000.00").compareTo(updated.getAmount()));
    }

    // 测试目的：验证已开票的发票不能编辑
    @Test
    public void testUpdateInvoice_notPendingStatus_error() {
        // 准备：插入已开票的发票
        CrmInvoiceDO dbInvoice = randomPojo(CrmInvoiceDO.class, o -> {
            o.setStatus(CrmInvoiceStatusEnum.ISSUED.getStatus());
            o.setInvoiceNo("INV-ISSUED");
        });
        invoiceMapper.insert(dbInvoice);

        CrmInvoiceSaveReqVO reqVO = randomPojo(CrmInvoiceSaveReqVO.class, o -> {
            o.setId(dbInvoice.getId());
            o.setInvoiceNo("INV-NEW");
            o.setAmount(new BigDecimal("100.00"));
            o.setContractId(1L);
        });

        // 执行 & 断言
        assertServiceException(() -> invoiceService.updateInvoice(reqVO), INVOICE_UPDATE_FAIL_STATUS_NOT_PENDING);
    }

    // ======================= 删除发票 =======================

    // 测试目的：验证删除待开票状态发票成功
    @Test
    public void testDeleteInvoice_success() {
        CrmInvoiceDO dbInvoice = randomPojo(CrmInvoiceDO.class, o ->
                o.setStatus(CrmInvoiceStatusEnum.PENDING.getStatus()));
        invoiceMapper.insert(dbInvoice);

        invoiceService.deleteInvoice(dbInvoice.getId());

        CrmInvoiceDO deleted = invoiceMapper.selectById(dbInvoice.getId());
        assertNull(deleted);
    }

    // 测试目的：验证已开票的发票不能删除
    @Test
    public void testDeleteInvoice_issuedStatus_error() {
        CrmInvoiceDO dbInvoice = randomPojo(CrmInvoiceDO.class, o ->
                o.setStatus(CrmInvoiceStatusEnum.ISSUED.getStatus()));
        invoiceMapper.insert(dbInvoice);

        assertServiceException(() -> invoiceService.deleteInvoice(dbInvoice.getId()), INVOICE_DELETE_FAIL_IS_ISSUED);
    }

    // ======================= 查询发票 =======================

    // 测试目的：验证通过ID获取发票详情
    @Test
    public void testGetInvoice_exists() {
        CrmInvoiceDO dbInvoice = randomPojo(CrmInvoiceDO.class);
        invoiceMapper.insert(dbInvoice);

        CrmInvoiceDO result = invoiceService.getInvoice(dbInvoice.getId());

        assertNotNull(result);
        assertEquals(dbInvoice.getInvoiceNo(), result.getInvoiceNo());
    }

    // 测试目的：验证查询不存在的发票返回 null
    @Test
    public void testGetInvoice_notExists() {
        CrmInvoiceDO result = invoiceService.getInvoice(-1L);
        assertNull(result);
    }

    // 测试目的：验证分页查询发票列表
    @Test
    public void testGetInvoicePage_success() {
        for (int i = 0; i < 3; i++) {
            final int idx = i;
            CrmInvoiceDO invoice = randomPojo(CrmInvoiceDO.class, o -> {
                o.setInvoiceNo("INV-PAGE-" + idx);
                o.setStatus(CrmInvoiceStatusEnum.PENDING.getStatus());
            });
            invoiceMapper.insert(invoice);
        }

        CrmInvoicePageReqVO pageReqVO = new CrmInvoicePageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(10);

        PageResult<CrmInvoiceDO> pageResult = invoiceService.getInvoicePage(pageReqVO);

        assertNotNull(pageResult);
        assertTrue(pageResult.getTotal() >= 3);
        assertTrue(pageResult.getList().size() >= 3);
    }

}
// [ADD END] 发票Service单元测试 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
