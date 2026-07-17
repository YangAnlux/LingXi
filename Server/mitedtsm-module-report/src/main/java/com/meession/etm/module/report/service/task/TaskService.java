// 23软2张奎良-202305566305
package com.meession.etm.module.report.service.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.report.controller.admin.task.vo.TaskBoardRespVO;
import com.meession.etm.module.report.controller.admin.task.vo.TaskPageReqVO;
import com.meession.etm.module.report.controller.admin.task.vo.TaskSaveReqVO;
import com.meession.etm.module.report.dal.dataobject.task.TaskDO;

import jakarta.validation.Valid;

/**
 * 任务管理 Service 接口
 * 
 * 定义任务管理的业务操作方法，包括创建、更新、删除、查询等功能。
 * 
 * @author 23软2张奎良
 */
public interface TaskService {

    /**
     * 创建任务
     * 
     * @param createReqVO 创建请求参数
     * @return 创建后的任务ID
     */
    Long createTask(@Valid TaskSaveReqVO createReqVO);

    /**
     * 更新任务
     * 
     * @param updateReqVO 更新请求参数
     */
    void updateTask(@Valid TaskSaveReqVO updateReqVO);

    /**
     * 删除任务
     * 
     * @param id 任务ID
     */
    void deleteTask(Long id);

    /**
     * 获取任务详情
     * 
     * @param id 任务ID
     * @return 任务详情
     */
    TaskDO getTask(Long id);

    /**
     * 查询任务分页
     * 
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    PageResult<TaskDO> getTaskPage(TaskPageReqVO pageReqVO);

    /**
     * 分配任务
     * 
     * @param id 任务ID
     * @param assigneeId 负责人ID
     * @param assigneeName 负责人姓名
     */
    void assignTask(Long id, Long assigneeId, String assigneeName);

    /**
     * 开始任务
     * 
     * @param id 任务ID
     */
    void startTask(Long id);

    /**
     * 完成任务
     * 
     * @param id 任务ID
     */
    void completeTask(Long id);

    /**
     * 取消任务
     * 
     * @param id 任务ID
     */
    void cancelTask(Long id);

    /**
     * 获取任务看板数据
     * 
     * 返回待办、进行中、已完成三个状态的任务列表，以及即将到期和已逾期的任务统计。
     * 
     * @return 任务看板数据
     */
    TaskBoardRespVO getTaskBoard();

}