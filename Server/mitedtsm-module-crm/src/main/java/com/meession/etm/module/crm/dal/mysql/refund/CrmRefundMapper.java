// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款Mapper - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.dal.mysql.refund;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退款 Mapper
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@Mapper
public interface CrmRefundMapper extends BaseMapperX<CrmRefundDO> {

    default CrmRefundDO selectByNo(String no) {
        return selectOne(CrmRefundDO::getNo, no);
    }

    default PageResult<CrmRefundDO> selectPage(CrmRefundPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmRefundDO>()
                .eqIfPresent(CrmRefundDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmRefundDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmRefundDO::getContractId, pageReqVO.getContractId())
                .eqIfPresent(CrmRefundDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(CrmRefundDO::getId));
    }

}
// [ADD END] 退款Mapper - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
