package com.meession.etm.module.crm.service.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskPageReqVO;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.task.CrmTaskDO;
import com.meession.etm.module.crm.dal.mysql.task.CrmTaskMapper;
import com.meession.etm.module.crm.service.notify.CrmNotifyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.TASK_NOT_EXISTS;

@Service
@Slf4j
public class CrmTaskServiceImpl implements CrmTaskService {

    // Task status constants
    private static final int STATUS_TODO = 0;
    private static final int STATUS_IN_PROGRESS = 1;
    private static final int STATUS_COMPLETED = 2;
    private static final int STATUS_CANCELLED = 3;

    @Resource
    private CrmTaskMapper taskMapper;

    @Resource
    private CrmNotifyService crmNotifyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTask(CrmTaskSaveReqVO createReqVO, Long userId) {
        CrmTaskDO task = BeanUtils.toBean(createReqVO, CrmTaskDO.class);
        task.setStatus(STATUS_TODO);
        task.setOwnerUserId(userId);
        taskMapper.insert(task);
        // 发送任务分配站内信通知
        crmNotifyService.sendTaskAssignNotify(task);
        return task.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(CrmTaskSaveReqVO updateReqVO) {
        CrmTaskDO task = validateTaskExists(updateReqVO.getId());
        Long oldOwner = task.getOwnerUserId();
        BeanUtils.copyProperties(updateReqVO, task);
        taskMapper.updateById(task);
        // 如果负责人发生变化，发送站内信通知
        if (updateReqVO.getOwnerUserId() != null
                && !updateReqVO.getOwnerUserId().equals(oldOwner)) {
            crmNotifyService.sendTaskAssignNotify(task);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Long id) {
        validateTaskExists(id);
        taskMapper.deleteById(id);
    }

    @Override
    public CrmTaskDO getTask(Long id) {
        return validateTaskExists(id);
    }

    @Override
    public PageResult<CrmTaskDO> getTaskPage(CrmTaskPageReqVO pageReqVO) {
        return taskMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskStatus(Long id, Integer status) {
        CrmTaskDO task = validateTaskExists(id);
        int currentStatus = task.getStatus() != null ? task.getStatus() : 0;
        // Status flow: 待办(0) → 进行中(1) → 已完成(2); 任何非完成状态可取消(3)
        switch (status) {
            case STATUS_IN_PROGRESS:
                if (currentStatus != STATUS_TODO) {
                    throw exception(TASK_NOT_EXISTS);
                }
                break;
            case STATUS_COMPLETED:
                if (currentStatus != STATUS_IN_PROGRESS) {
                    throw exception(TASK_NOT_EXISTS);
                }
                task.setCompletedTime(LocalDateTime.now());
                break;
            case STATUS_CANCELLED:
                if (currentStatus == STATUS_COMPLETED || currentStatus == STATUS_CANCELLED) {
                    throw exception(TASK_NOT_EXISTS);
                }
                break;
            default:
                break;
        }
        task.setStatus(status);
        taskMapper.updateById(task);
    }

    private CrmTaskDO validateTaskExists(Long id) {
        CrmTaskDO task = taskMapper.selectById(id);
        if (task == null) {
            throw exception(TASK_NOT_EXISTS);
        }
        return task;
    }

}
