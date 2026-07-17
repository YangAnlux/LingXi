// 23软2张奎良-202305566305
package com.meession.etm.module.report.service.task;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.report.controller.admin.task.vo.TaskBoardRespVO;
import com.meession.etm.module.report.controller.admin.task.vo.TaskPageReqVO;
import com.meession.etm.module.report.controller.admin.task.vo.TaskRespVO;
import com.meession.etm.module.report.controller.admin.task.vo.TaskSaveReqVO;
import com.meession.etm.module.report.dal.dataobject.task.TaskDO;
import com.meession.etm.module.report.dal.mysql.task.TaskMapper;

import java.time.LocalDate;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.report.enums.ErrorCodeConstants.TASK_NOT_EXISTS;
import static com.meession.etm.module.report.enums.ErrorCodeConstants.TASK_STATUS_ERROR;

/**
 * 任务管理 Service 实现类
 * 
 * 实现任务管理的业务逻辑，包括创建、更新、删除、查询等功能。
 * 
 * @author 23软2张奎良
 */
@Service
@Validated
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskMapper taskMapper;

    /**
     * 创建任务
     * 
     * @param createReqVO 创建请求参数
     * @return 创建后的任务ID
     */
    @Override
    public Long createTask(TaskSaveReqVO createReqVO) {
        TaskDO task = BeanUtils.toBean(createReqVO, TaskDO.class);
        if (task.getStatus() == null) {
            task.setStatus(0);
        }
        if (task.getProgress() == null) {
            task.setProgress(0);
        }
        taskMapper.insert(task);
        return task.getId();
    }

    /**
     * 更新任务
     * 
     * @param updateReqVO 更新请求参数
     */
    @Override
    public void updateTask(TaskSaveReqVO updateReqVO) {
        validateTaskExists(updateReqVO.getId());
        TaskDO task = BeanUtils.toBean(updateReqVO, TaskDO.class);
        taskMapper.updateById(task);
    }

    /**
     * 删除任务
     * 
     * @param id 任务ID
     */
    @Override
    public void deleteTask(Long id) {
        validateTaskExists(id);
        taskMapper.deleteById(id);
    }

    /**
     * 获取任务详情
     * 
     * @param id 任务ID
     * @return 任务详情
     */
    @Override
    public TaskDO getTask(Long id) {
        return taskMapper.selectById(id);
    }

    /**
     * 查询任务分页
     * 
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<TaskDO> getTaskPage(TaskPageReqVO pageReqVO) {
        return taskMapper.selectPage(pageReqVO);
    }

    /**
     * 分配任务
     * 
     * 将任务状态从待分配(0)变更为进行中(1)。
     * 只有待分配状态的任务才能分配。
     * 
     * @param id 任务ID
     * @param assigneeId 负责人ID
     * @param assigneeName 负责人姓名
     */
    @Override
    public void assignTask(Long id, Long assigneeId, String assigneeName) {
        TaskDO task = taskMapper.selectById(id);
        validateTaskExists(id);
        
        if (!task.getStatus().equals(0)) {
            throw exception(TASK_STATUS_ERROR);
        }
        
        task.setAssigneeId(assigneeId);
        task.setAssigneeName(assigneeName);
        task.setStatus(1);
        taskMapper.updateById(task);
    }

    /**
     * 开始任务
     * 
     * 将任务状态从待分配(0)或已取消(3)变更为进行中(1)。
     * 
     * @param id 任务ID
     */
    @Override
    public void startTask(Long id) {
        TaskDO task = taskMapper.selectById(id);
        validateTaskExists(id);
        
        if (!task.getStatus().equals(0) && !task.getStatus().equals(3)) {
            throw exception(TASK_STATUS_ERROR);
        }
        
        task.setStatus(1);
        taskMapper.updateById(task);
    }

    /**
     * 完成任务
     * 
     * 将任务状态从进行中(1)变更为已完成(2)。
     * 只有进行中状态的任务才能完成。
     * 
     * @param id 任务ID
     */
    @Override
    public void completeTask(Long id) {
        TaskDO task = taskMapper.selectById(id);
        validateTaskExists(id);
        
        if (!task.getStatus().equals(1)) {
            throw exception(TASK_STATUS_ERROR);
        }
        
        task.setStatus(2);
        task.setProgress(100);
        task.setCompleteDate(LocalDate.now());
        taskMapper.updateById(task);
    }

    /**
     * 取消任务
     * 
     * 将任务状态从待分配(0)或进行中(1)变更为已取消(3)。
     * 
     * @param id 任务ID
     */
    @Override
    public void cancelTask(Long id) {
        TaskDO task = taskMapper.selectById(id);
        validateTaskExists(id);
        
        if (!task.getStatus().equals(0) && !task.getStatus().equals(1)) {
            throw exception(TASK_STATUS_ERROR);
        }
        
        task.setStatus(3);
        taskMapper.updateById(task);
    }

    /**
     * 校验任务是否存在
     * 
     * @param id 任务ID
     */
    private void validateTaskExists(Long id) {
        if (taskMapper.selectById(id) == null) {
            throw exception(TASK_NOT_EXISTS);
        }
    }

    /**
     * 获取任务看板数据
     * 
     * 返回待办、进行中、已完成三个状态的任务列表，以及即将到期和已逾期的任务统计。
     * 
     * @return 任务看板数据
     */
    @Override
    public TaskBoardRespVO getTaskBoard() {
        List<TaskDO> allTasks = taskMapper.selectList(null);
        
        List<TaskRespVO> todoTasks = allTasks.stream()
            .filter(task -> task.getStatus().equals(0))
            .map(task -> BeanUtils.toBean(task, TaskRespVO.class))
            .toList();
        
        List<TaskRespVO> inProgressTasks = allTasks.stream()
            .filter(task -> task.getStatus().equals(1))
            .map(task -> BeanUtils.toBean(task, TaskRespVO.class))
            .toList();
        
        List<TaskRespVO> completedTasks = allTasks.stream()
            .filter(task -> task.getStatus().equals(2))
            .map(task -> BeanUtils.toBean(task, TaskRespVO.class))
            .toList();
        
        LocalDate today = LocalDate.now();
        LocalDate threeDaysLater = today.plusDays(3);
        
        int upcomingExpiredCount = (int) allTasks.stream()
            .filter(task -> task.getStatus().equals(0) || task.getStatus().equals(1))
            .filter(task -> task.getEndDate() != null)
            .filter(task -> !task.getEndDate().isBefore(today) && task.getEndDate().isBefore(threeDaysLater))
            .count();
        
        int expiredCount = (int) allTasks.stream()
            .filter(task -> task.getStatus().equals(0) || task.getStatus().equals(1))
            .filter(task -> task.getEndDate() != null)
            .filter(task -> task.getEndDate().isBefore(today))
            .count();
        
        return TaskBoardRespVO.builder()
            .todoTasks(todoTasks)
            .inProgressTasks(inProgressTasks)
            .completedTasks(completedTasks)
            .upcomingExpiredCount(upcomingExpiredCount)
            .expiredCount(expiredCount)
            .build();
    }

}