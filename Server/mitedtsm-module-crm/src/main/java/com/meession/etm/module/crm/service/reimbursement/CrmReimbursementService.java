// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销Service接口 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.service.reimbursement;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;

import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

/**
 * CRM 报销 Service 接口
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
public interface CrmReimbursementService {

    /**
     * 创建报销
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createReimbursement(@Valid CrmReimbursementSaveReqVO createReqVO);

    /**
     * 更新报销
     *
     * @param updateReqVO 更新信息
     */
    void updateReimbursement(@Valid CrmReimbursementSaveReqVO updateReqVO);

    /**
     * 删除报销
     *
     * @param id 编号
     */
    void deleteReimbursement(Long id);

    /**
     * 获得报销
     *
     * @param id 编号
     * @return 报销
     */
    CrmReimbursementDO getReimbursement(Long id);

    /**
     * 获得报销列表（通过 ID 集合）
     *
     * @param ids 编号集合
     * @return 列表
     */
    List<CrmReimbursementDO> getReimbursementList(Collection<Long> ids);

    /**
     * 获得报销分页
     *
     * @param pageReqVO 分页查询
     * @return 分页结果
     */
    PageResult<CrmReimbursementDO> getReimbursementPage(CrmReimbursementPageReqVO pageReqVO);

    /**
     * 提交报销审批
     *
     * @param id     编号
     * @param userId 用户编号
     */
    void submitReimbursement(Long id, Long userId);

    /**
     * 更新报销审核状态（BPM 回调）
     *
     * @param id        编号
     * @param bpmResult BPM 审批结果
     */
    void updateReimbursementAuditStatus(Long id, Integer bpmResult);

}
// [ADD END] 报销Service接口 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
