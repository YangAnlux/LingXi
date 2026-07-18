// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款Service接口 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.service.refund;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;

import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

/**
 * CRM 退款 Service 接口
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
public interface CrmRefundService {

    /**
     * 创建退款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRefund(@Valid CrmRefundSaveReqVO createReqVO);

    /**
     * 更新退款
     *
     * @param updateReqVO 更新信息
     */
    void updateRefund(@Valid CrmRefundSaveReqVO updateReqVO);

    /**
     * 删除退款
     *
     * @param id 编号
     */
    void deleteRefund(Long id);

    /**
     * 获得退款
     *
     * @param id 编号
     * @return 退款
     */
    CrmRefundDO getRefund(Long id);

    /**
     * 获得退款列表（通过 ID 集合）
     *
     * @param ids 编号集合
     * @return 列表
     */
    List<CrmRefundDO> getRefundList(Collection<Long> ids);

    /**
     * 获得退款分页
     *
     * @param pageReqVO 分页查询
     * @return 退款分页
     */
    PageResult<CrmRefundDO> getRefundPage(CrmRefundPageReqVO pageReqVO);

    /**
     * 提交退款审批
     *
     * @param id     退款编号
     * @param userId 用户编号
     */
    void submitRefund(Long id, Long userId);

    /**
     * 更新退款审批状态（BPM 回调）
     *
     * @param id        退款编号
     * @param bpmResult BPM 审批结果
     */
    void updateRefundAuditStatus(Long id, Integer bpmResult);

}
// [ADD END] 退款Service接口 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
