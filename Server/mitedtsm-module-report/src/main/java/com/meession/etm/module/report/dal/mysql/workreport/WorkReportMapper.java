// 23软2张奎良-202305566305
package com.meession.etm.module.report.dal.mysql.workreport;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.report.controller.admin.workreport.vo.WorkReportPageReqVO;
import com.meession.etm.module.report.dal.dataobject.workreport.WorkReportDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作报表 Mapper
 * 
 * 提供工作报表的数据库访问能力，包括分页查询、条件查询等。
 * 
 * @author 23软2张奎良
 */
@Mapper
public interface WorkReportMapper extends BaseMapperX<WorkReportDO> {

    /**
     * 查询工作报表分页
     * 
     * @param reqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<WorkReportDO> selectPage(WorkReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkReportDO>()
                .eqIfPresent(WorkReportDO::getType, reqVO.getType())
                .likeIfPresent(WorkReportDO::getTitle, reqVO.getTitle())
                .eqIfPresent(WorkReportDO::getReporterId, reqVO.getReporterId())
                .eqIfPresent(WorkReportDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(WorkReportDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WorkReportDO::getReportDate, reqVO.getDateRange())
                .orderByDesc(WorkReportDO::getId));
    }

}