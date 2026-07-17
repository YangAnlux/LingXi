// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.task;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.report.controller.admin.task.vo.TaskBoardRespVO;
import com.meession.etm.module.report.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务看板 Controller
 * 
 * 提供任务看板相关接口，包括待办、进行中、已完成任务的查询，以及截止日期提醒功能。
 * 
 * @author 23软2张奎良
 */
@Tag(name = "管理后台 - 任务看板")
@RestController
@RequestMapping("/report/task-board")
public class TaskBoardController {

    @Resource
    private TaskService taskService;

    /**
     * 获取任务看板数据
     * 
     * 返回待办、进行中、已完成三个状态的任务列表，以及即将到期和已逾期的任务数量。
     * 
     * @return 任务看板数据
     */
    @GetMapping("/get-board")
    @Operation(summary = "获取任务看板数据")
    @PreAuthorize("@ss.hasPermission('report:task-board:query')")
    public CommonResult<TaskBoardRespVO> getTaskBoard() {
        return CommonResult.success(taskService.getTaskBoard());
    }

}