package com.meession.etm.module.bpm.oa;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.framework.test.core.util.RandomUtils;
import com.meession.etm.module.bpm.controller.admin.oa.vo.BpmOALeaveCreateReqVO;
import com.meession.etm.module.bpm.controller.admin.oa.vo.BpmOALeavePageReqVO;
import com.meession.etm.module.bpm.dal.dataobject.oa.BpmOALeaveDO;
import com.meession.etm.module.bpm.dal.mysql.oa.BpmOALeaveMapper;
import com.meession.etm.module.bpm.service.oa.BpmOALeaveServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import jakarta.annotation.Resource;

/**
 * OA 请假申请 Service 层单元测试
 *
 * BPM 工作流接入的示例模块，覆盖请假 CRUD + 流程发起
 */
@Import(BpmOALeaveServiceImpl.class)
public class BpmOALeaveServiceTest extends BaseDbUnitTest {

    @Resource
    private BpmOALeaveServiceImpl leaveService;

    @Resource
    private BpmOALeaveMapper leaveMapper;

    @Test
    @DisplayName("TC-OA-001: 创建请假申请 - 正向测试")
    public void testCreateLeave_Success() {
        BpmOALeaveCreateReqVO reqVO = RandomUtils.randomPojo(BpmOALeaveCreateReqVO.class);

        Long leaveId = leaveService.createLeave(10001L, reqVO);

        Assertions.assertNotNull(leaveId);
        BpmOALeaveDO leave = leaveMapper.selectById(leaveId);
        Assertions.assertNotNull(leave);
        Assertions.assertEquals(10001L, leave.getUserId());
    }

    @Test
    @DisplayName("TC-OA-002: 查询请假申请 - 正向测试")
    public void testGetLeave_Success() {
        BpmOALeaveDO dbLeave = RandomUtils.randomPojo(BpmOALeaveDO.class);
        dbLeave.setUserId(10001L);
        leaveMapper.insert(dbLeave);

        BpmOALeaveDO result = leaveService.getLeave(dbLeave.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dbLeave.getId(), result.getId());
    }

    @Test
    @DisplayName("查询请假申请 - 不存在返回null")
    public void testGetLeave_NotFound() {
        BpmOALeaveDO result = leaveService.getLeave(99999L);
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("获取请假分页 - 正向测试")
    public void testGetLeavePage_Success() {
        BpmOALeaveDO dbLeave = RandomUtils.randomPojo(BpmOALeaveDO.class);
        dbLeave.setUserId(10001L);
        leaveMapper.insert(dbLeave);

        BpmOALeavePageReqVO pageReqVO = new BpmOALeavePageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(20);

        PageResult<BpmOALeaveDO> result = leaveService.getLeavePage(10001L, pageReqVO);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotal() >= 1);
    }

    @Test
    @DisplayName("获取请假分页 - 空结果")
    public void testGetLeavePage_Empty() {
        BpmOALeavePageReqVO pageReqVO = new BpmOALeavePageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(20);

        PageResult<BpmOALeaveDO> result = leaveService.getLeavePage(99998L, pageReqVO);

        Assertions.assertNotNull(result);
    }
}
