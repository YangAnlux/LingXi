// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.dal.mysql.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderStatisticsRespVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 工单 Mapper
 *
 * @author CRM Team
 */
@Mapper
public interface CrmWorkOrderMapper extends BaseMapperX<CrmWorkOrderDO> {

    default PageResult<CrmWorkOrderDO> selectPage(CrmWorkOrderPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmWorkOrderDO>()
                .likeIfPresent(CrmWorkOrderDO::getTitle, pageReqVO.getTitle())
                .eqIfPresent(CrmWorkOrderDO::getType, pageReqVO.getType())
                .eqIfPresent(CrmWorkOrderDO::getPriority, pageReqVO.getPriority())
                .eqIfPresent(CrmWorkOrderDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmWorkOrderDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmWorkOrderDO::getAssigneeId, pageReqVO.getAssigneeId())
                .orderByDesc(CrmWorkOrderDO::getId));
    }

    // 23软件工程4班蔡磊202305566515
    @Update("UPDATE crm_work_order SET is_sla_breached = 1 WHERE sla_deadline < NOW() AND is_sla_breached = 0 AND status != '已完结'")
    int updateSlaBreached();

    // 23软件工程4班蔡磊202305566515
    @Select("SELECT `type` AS name, COUNT(*) AS count FROM crm_work_order WHERE deleted = 0 GROUP BY `type` ORDER BY count DESC")
    List<CrmWorkOrderStatisticsRespVO> countByType();

    // 23软件工程4班蔡磊202305566515
    @Select("SELECT `status` AS name, COUNT(*) AS count FROM crm_work_order WHERE deleted = 0 GROUP BY `status` ORDER BY FIELD(`status`, '待处理', '处理中', '已完结', '已退回')")
    List<CrmWorkOrderStatisticsRespVO> countByStatus();

    // 23软件工程4班蔡磊202305566515
    @Select("SELECT IFNULL(u.nickname, CONCAT('用户', wo.assignee_id)) AS name, COUNT(*) AS count FROM crm_work_order wo LEFT JOIN system_users u ON wo.assignee_id = u.id WHERE wo.deleted = 0 AND wo.assignee_id IS NOT NULL GROUP BY wo.assignee_id ORDER BY count DESC")
    List<CrmWorkOrderStatisticsRespVO> countByAssignee();

}
