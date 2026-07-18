package com.meession.etm.module.crm.controller.admin.schedule;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmSchedulePageReqVO;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmScheduleRespVO;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmScheduleSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.schedule.CrmScheduleDO;
import com.meession.etm.module.crm.service.schedule.CrmScheduleService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "管理后台 - 日程管理")
@RestController
@RequestMapping("/crm/schedule")
@Validated
public class CrmScheduleController {

    @Resource
    private CrmScheduleService scheduleService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建日程")
    @PreAuthorize("@ss.hasPermission('crm:schedule:create')")
    public CommonResult<Long> createSchedule(@Valid @RequestBody CrmScheduleSaveReqVO createReqVO) {
        return success(scheduleService.createSchedule(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新日程")
    @PreAuthorize("@ss.hasPermission('crm:schedule:update')")
    public CommonResult<Boolean> updateSchedule(@Valid @RequestBody CrmScheduleSaveReqVO updateReqVO) {
        scheduleService.updateSchedule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除日程")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:schedule:delete')")
    public CommonResult<Boolean> deleteSchedule(@RequestParam("id") Long id) {
        scheduleService.deleteSchedule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得日程")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:schedule:query')")
    public CommonResult<CrmScheduleRespVO> getSchedule(@RequestParam("id") Long id) {
        CrmScheduleDO schedule = scheduleService.getSchedule(id);
        return success(buildScheduleDetail(schedule));
    }

    @GetMapping("/page")
    @Operation(summary = "获得日程分页")
    @PreAuthorize("@ss.hasPermission('crm:schedule:query')")
    public CommonResult<PageResult<CrmScheduleRespVO>> getSchedulePage(@Valid CrmSchedulePageReqVO pageVO) {
        PageResult<CrmScheduleDO> pageResult = scheduleService.getSchedulePage(pageVO);
        return success(new PageResult<>(buildScheduleDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/list-by-range")
    @Operation(summary = "根据时间范围获取日程列表(用于日历展示)")
    @PreAuthorize("@ss.hasPermission('crm:schedule:query')")
    public CommonResult<List<CrmScheduleRespVO>> getScheduleListByTimeRange(
            @RequestParam("start") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND) LocalDateTime end) {
        List<CrmScheduleDO> list = scheduleService.getScheduleListByTimeRange(start, end);
        return success(buildScheduleDetailList(list));
    }

    private CrmScheduleRespVO buildScheduleDetail(CrmScheduleDO schedule) {
        if (schedule == null) return null;
        return buildScheduleDetailList(singletonList(schedule)).get(0);
    }

    private List<CrmScheduleRespVO> buildScheduleDetailList(List<CrmScheduleDO> list) {
        if (CollUtil.isEmpty(list)) return Collections.emptyList();
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                schedule -> Stream.of(schedule.getOwnerUserId(), NumberUtils.parseLong(schedule.getCreator()))));
        return BeanUtils.toBean(list, CrmScheduleRespVO.class, vo -> {
            MapUtils.findAndThen(userMap, vo.getOwnerUserId(),
                    user -> vo.setOwnerUserName(user.getNickname()));
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(vo.getCreator()),
                    user -> vo.setCreatorName(user.getNickname()));
        });
    }

}
