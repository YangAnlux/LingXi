// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票Service接口 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.service.invoice;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * CRM 发票 Service 接口
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.14
 */
public interface CrmInvoiceService {

    /**
     * 创建发票
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInvoice(@Valid CrmInvoiceSaveReqVO createReqVO);

    /**
     * 更新发票
     *
     * @param updateReqVO 更新信息
     */
    void updateInvoice(@Valid CrmInvoiceSaveReqVO updateReqVO);

    /**
     * 删除发票
     *
     * @param id 编号
     */
    void deleteInvoice(Long id);

    /**
     * 获得发票
     *
     * @param id 编号
     * @return 发票
     */
    CrmInvoiceDO getInvoice(Long id);

    /**
     * 获得发票列表
     *
     * @param ids 编号
     * @return 发票列表
     */
    List<CrmInvoiceDO> getInvoiceList(Collection<Long> ids);

    /**
     * 获得发票 Map
     *
     * @param ids 编号
     * @return 发票 Map
     */
    default Map<Long, CrmInvoiceDO> getInvoiceMap(Collection<Long> ids) {
        return convertMap(getInvoiceList(ids), CrmInvoiceDO::getId);
    }

    /**
     * 获得发票分页
     *
     * @param pageReqVO 分页查询
     * @return 发票分页
     */
    PageResult<CrmInvoiceDO> getInvoicePage(CrmInvoicePageReqVO pageReqVO);

    /**
     * 获得合同已开票金额 Map
     *
     * @param contractIds 合同编号
     * @return 开票金额 Map
     */
    Map<Long, BigDecimal> getInvoiceAmountMapByContractId(Collection<Long> contractIds);

    /**
     * 根据合同编号查询发票数量
     *
     * @param contractId 合同编号
     * @return 发票数量
     */
    Long getInvoiceCountByContractId(Long contractId);

}
// [ADD END] 发票Service接口 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
