// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销Mapper - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.dal.mysql.reimbursement;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报销 Mapper
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@Mapper
public interface CrmReimbursementMapper extends BaseMapperX<CrmReimbursementDO> {

    default CrmReimbursementDO selectByNo(String no) {
        return selectOne(CrmReimbursementDO::getNo, no);
    }

    default PageResult<CrmReimbursementDO> selectPage(CrmReimbursementPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmReimbursementDO>()
                .eqIfPresent(CrmReimbursementDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmReimbursementDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmReimbursementDO::getContractId, pageReqVO.getContractId())
                .eqIfPresent(CrmReimbursementDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(CrmReimbursementDO::getId));
    }

}
// [ADD END] 报销Mapper - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
