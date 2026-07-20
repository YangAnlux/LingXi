// 23软2张奎良-202305566305
package com.meession.etm.module.report.dal.mysql.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.report.controller.admin.task.vo.TaskPageReqVO;
import com.meession.etm.module.report.dal.dataobject.task.TaskDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务管理 Mapper
 * 
 * 提供任务管理的数据库访问能力，包括分页查询、条件查询等。
 * 
 * @author 23软2张奎良
 */
@Mapper
public interface TaskMapper extends BaseMapperX<TaskDO> {

    /**
     * 查询任务分页
     * 
     * @param reqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<TaskDO> selectPage(TaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TaskDO>()
                .likeIfPresent(TaskDO::getTitle, reqVO.getTitle())
                .eqIfPresent(TaskDO::getType, reqVO.getType())
                .eqIfPresent(TaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TaskDO::getPriority, reqVO.getPriority())
                .eqIfPresent(TaskDO::getAssigneeId, reqVO.getAssigneeId())
                .eqIfPresent(TaskDO::getDeptId, reqVO.getDeptId())
                .betweenIfPresent(TaskDO::getStartDate, reqVO.getDateRange())
                .orderByDesc(TaskDO::getId));
    }

}