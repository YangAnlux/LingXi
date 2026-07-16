// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.task;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import java.util.*;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import static com.meession.etm.framework.common.pojo.CommonResult.success;

import com.meession.etm.module.report.controller.admin.task.vo.*;
import com.meession.etm.module.report.dal.dataobject.task.TaskDO;
import com.meession.etm.module.report.service.task.TaskService;

/**
 * 任务管理 Controller
 * 
 * 提供任务管理的 REST API 接口，包括创建、更新、删除、查询等操作。
 * 
 * @author 23软2张奎良
 */
@Tag(name = "管理后台 - 任务管理")
@RestController
@RequestMapping("/report/task")
@Validated
public class TaskController {

    @Resource
    private TaskService taskService;

    /**
     * 创建任务
     * 
     * @param createReqVO 创建请求参数
     * @return 创建后的任务ID
     */
    @PostMapping("/create")
    @Operation(summary = "创建任务")
    @PreAuthorize("@ss.hasPermission('report:task:create')")
    public CommonResult<Long> createTask(@Valid @RequestBody TaskSaveReqVO createReqVO) {
        return success(taskService.createTask(createReqVO));
    }

    /**
     * 更新任务
     * 
     * @param updateReqVO 更新请求参数
     * @return 更新结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新任务")
    @PreAuthorize("@ss.hasPermission('report:task:update')")
    public CommonResult<Boolean> updateTask(@Valid @RequestBody TaskSaveReqVO updateReqVO) {
        taskService.updateTask(updateReqVO);
        return success(true);
    }

    /**
     * 删除任务
     * 
     * @param id 任务ID
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('report:task:delete')")
    public CommonResult<Boolean> deleteTask(@RequestParam("id") Long id) {
        taskService.deleteTask(id);
        return success(true);
    }

    /**
     * 获取任务详情
     * 
     * @param id 任务ID
     * @return 任务详情
     */
    @GetMapping("/get")
    @Operation(summary = "获得任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('report:task:query')")
    public CommonResult<TaskRespVO> getTask(@RequestParam("id") Long id) {
        TaskDO task = taskService.getTask(id);
        return success(BeanUtils.toBean(task, TaskRespVO.class));
    }

    /**
     * 获取任务分页
     * 
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    @GetMapping("/page")
    @Operation(summary = "获得任务分页")
    @PreAuthorize("@ss.hasPermission('report:task:query')")
    public CommonResult<PageResult<TaskRespVO>> getTaskPage(@Valid TaskPageReqVO pageReqVO) {
        PageResult<TaskDO> pageResult = taskService.getTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TaskRespVO.class));
    }

    /**
     * 分配任务
     * 
     * @param id 任务ID
     * @param assigneeId 负责人ID
     * @param assigneeName 负责人姓名
     * @return 分配结果
     */
    @PutMapping("/assign")
    @Operation(summary = "分配任务")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "assigneeId", description = "负责人ID", required = true)
    @Parameter(name = "assigneeName", description = "负责人姓名", required = true)
    @PreAuthorize("@ss.hasPermission('report:task:assign')")
    public CommonResult<Boolean> assignTask(
            @RequestParam("id") Long id,
            @RequestParam("assigneeId") Long assigneeId,
            @RequestParam("assigneeName") String assigneeName) {
        taskService.assignTask(id, assigneeId, assigneeName);
        return success(true);
    }

    /**
     * 开始任务
     * 
     * @param id 任务ID
     * @return 开始结果
     */
    @PutMapping("/start")
    @Operation(summary = "开始任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('report:task:start')")
    public CommonResult<Boolean> startTask(@RequestParam("id") Long id) {
        taskService.startTask(id);
        return success(true);
    }

    /**
     * 完成任务
     * 
     * @param id 任务ID
     * @return 完成结果
     */
    @PutMapping("/complete")
    @Operation(summary = "完成任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('report:task:complete')")
    public CommonResult<Boolean> completeTask(@RequestParam("id") Long id) {
        taskService.completeTask(id);
        return success(true);
    }

    /**
     * 取消任务
     * 
     * @param id 任务ID
     * @return 取消结果
     */
    @PutMapping("/cancel")
    @Operation(summary = "取消任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('report:task:cancel')")
    public CommonResult<Boolean> cancelTask(@RequestParam("id") Long id) {
        taskService.cancelTask(id);
        return success(true);
    }

}