package com.meession.etm.module.crm.controller.admin.care;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidayPageReqVO;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidayRespVO;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidaySaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmHolidayDO;
import com.meession.etm.module.crm.service.care.CrmHolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 节日配置")
@RestController
@RequestMapping("/crm/holiday")
@Validated
public class CrmHolidayController {

    @Resource
    private CrmHolidayService holidayService;

    @PostMapping("/create")
    @Operation(summary = "创建节日配置")
    @PreAuthorize("@ss.hasPermission('crm:holiday:create')")
    public CommonResult<Long> createHoliday(@Valid @RequestBody CrmHolidaySaveReqVO createReqVO) {
        return success(holidayService.createHoliday(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新节日配置")
    @PreAuthorize("@ss.hasPermission('crm:holiday:update')")
    public CommonResult<Boolean> updateHoliday(@Valid @RequestBody CrmHolidaySaveReqVO updateReqVO) {
        holidayService.updateHoliday(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除节日配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:holiday:delete')")
    public CommonResult<Boolean> deleteHoliday(@RequestParam("id") Long id) {
        holidayService.deleteHoliday(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得节日配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:holiday:query')")
    public CommonResult<CrmHolidayRespVO> getHoliday(@RequestParam("id") Long id) {
        CrmHolidayDO holiday = holidayService.getHoliday(id);
        return success(BeanUtils.toBean(holiday, CrmHolidayRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得节日配置分页")
    @PreAuthorize("@ss.hasPermission('crm:holiday:query')")
    public CommonResult<PageResult<CrmHolidayRespVO>> getHolidayPage(@Valid CrmHolidayPageReqVO pageVO) {
        PageResult<CrmHolidayDO> pageResult = holidayService.getHolidayPage(pageVO);
        return success(new PageResult<>(
                BeanUtils.toBean(pageResult.getList(), CrmHolidayRespVO.class),
                pageResult.getTotal()));
    }

    @GetMapping("/list-all")
    @Operation(summary = "获取全部启用的节日列表")
    @PreAuthorize("@ss.hasPermission('crm:holiday:query')")
    public CommonResult<List<CrmHolidayRespVO>> listAllEnabled() {
        List<CrmHolidayDO> list = holidayService.listAllEnabled();
        return success(BeanUtils.toBean(list, CrmHolidayRespVO.class));
    }

}
