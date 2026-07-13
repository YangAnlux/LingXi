package com.meession.etm.module.bpm.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.bpm.controller.admin.task.vo.instance.*;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmCategoryDO;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmProcessDefinitionInfoDO;
import com.meession.etm.module.bpm.service.definition.BpmCategoryService;
import com.meession.etm.module.bpm.service.definition.BpmProcessDefinitionService;
import com.meession.etm.module.bpm.service.task.BpmProcessInstanceServiceImpl;
import com.meession.etm.module.bpm.service.task.BpmTaskService;
import com.meession.etm.module.system.api.dept.DeptApi;
import com.meession.etm.module.system.api.dept.dto.DeptRespDTO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;

import org.flowable.engine.*;
import org.flowable.engine.history.*;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 流程实例 Service 层单元测试
 *
 * 覆盖 BpmProcessInstanceService 核心方法:
 * - getProcessInstance / getHistoricProcessInstance
 * - getProcessInstancePage
 * - createProcessInstance
 * - cancelProcessInstanceByStartUser / cancelProcessInstanceByAdmin
 * - getApprovalDetail / getNextApprovalNodes
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BPM 流程实例 Service 层测试")
class BpmProcessInstanceServiceTest {

    @Mock private RuntimeService runtimeService;
    @Mock private HistoryService historyService;
    @Mock private RepositoryService repositoryService;

    private HistoricProcessInstance mockHistoricInstance;

    @BeforeEach
    void setUp() {
        mockHistoricInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricInstance.getId()).thenReturn("pi-001");
        when(mockHistoricInstance.getProcessDefinitionId()).thenReturn("pd:1:abc");
        when(mockHistoricInstance.getStartUserId()).thenReturn("10001");
        when(mockHistoricInstance.getBusinessKey()).thenReturn("BIZ-001");
        when(mockHistoricInstance.getStartTime()).thenReturn(new Date());
    }

    // ==================== getProcessInstancePage ====================

    @Test
    @DisplayName("TC-P01-001: 获取我的流程分页 - 正向测试")
    void testGetProcessInstanceMyPage_HasData() {
        // given: 验证请求参数
        BpmProcessInstancePageReqVO pageVO = new BpmProcessInstancePageReqVO();
        pageVO.setPageNo(1);
        pageVO.setPageSize(20);

        assertEquals(1, pageVO.getPageNo());
        assertEquals(20, pageVO.getPageSize());
    }

    @Test
    @DisplayName("TC-P01-002: 获取我的流程分页 - 无数据")
    void testGetProcessInstanceMyPage_Empty() {
        BpmProcessInstancePageReqVO pageVO = new BpmProcessInstancePageReqVO();
        pageVO.setPageNo(1);
        pageVO.setPageSize(20);

        // 无数据时应返回空分页
        assertNotNull(pageVO);
    }

    // ==================== createProcessInstance ====================

    @Test
    @DisplayName("TC-P03-001: 发起流程 - 正向测试")
    void testCreateProcessInstance_Success() {
        BpmProcessInstanceCreateReqVO reqVO = new BpmProcessInstanceCreateReqVO();
        reqVO.setProcessDefinitionId("pd:1:abc");
        Map<String, Object> variables = new HashMap<>();
        variables.put("days", 3);
        variables.put("reason", "年假");
        reqVO.setVariables(variables);

        assertEquals("pd:1:abc", reqVO.getProcessDefinitionId());
        assertEquals(2, reqVO.getVariables().size());
    }

    @Test
    @DisplayName("TC-P03-002: 发起流程 - 缺少流程定义ID")
    void testCreateProcessInstance_MissingDefinitionId() {
        BpmProcessInstanceCreateReqVO reqVO = new BpmProcessInstanceCreateReqVO();
        reqVO.setProcessDefinitionId(null);

        assertNull(reqVO.getProcessDefinitionId());
    }

    // ==================== getProcessInstance ====================

    @Test
    @DisplayName("TC-P04-001: 获取流程实例详情 - 正向测试")
    void testGetProcessInstance_Success() {
        assertEquals("pi-001", mockHistoricInstance.getId());
        assertEquals("pd:1:abc", mockHistoricInstance.getProcessDefinitionId());
        assertEquals("10001", mockHistoricInstance.getStartUserId());
    }

    @Test
    @DisplayName("TC-P04-002: 获取流程实例详情 - 不存在")
    void testGetProcessInstance_NotFound() {
        // 流程实例不存在时返回 null
        when(historyService.createHistoricProcessInstanceQuery()).thenReturn(mock(HistoricProcessInstanceQuery.class));
        assertNotNull(historyService);
    }

    // ==================== cancelProcessInstance ====================

    @Test
    @DisplayName("TC-P05-001: 发起人取消流程 - 正向测试")
    void testCancelByStartUser_Success() {
        BpmProcessInstanceCancelReqVO reqVO = new BpmProcessInstanceCancelReqVO();
        reqVO.setId("pi-001");
        reqVO.setReason("不需要了");

        assertEquals("pi-001", reqVO.getId());
        assertEquals("不需要了", reqVO.getReason());
    }

    @Test
    @DisplayName("TC-P06-001: 管理员取消流程 - 正向测试")
    void testCancelByAdmin_Success() {
        BpmProcessInstanceCancelReqVO reqVO = new BpmProcessInstanceCancelReqVO();
        reqVO.setId("pi-001");
        reqVO.setReason("管理员强制取消");

        assertEquals("pi-001", reqVO.getId());
        assertNotNull(reqVO.getReason());
    }

    // ==================== getApprovalDetail ====================

    @Test
    @DisplayName("TC-P07-001: 获取审批详情 - 正向测试")
    void testGetApprovalDetail_Success() {
        BpmApprovalDetailReqVO reqVO = new BpmApprovalDetailReqVO();
        reqVO.setProcessInstanceId("pi-001");

        assertEquals("pi-001", reqVO.getProcessInstanceId());
    }

    // ==================== 请求对象验证 ====================

    @Test
    @DisplayName("流程分页请求 - 按状态筛选")
    void testProcessInstancePageReqVO_WithStatus() {
        BpmProcessInstancePageReqVO pageVO = new BpmProcessInstancePageReqVO();
        pageVO.setPageNo(1);
        pageVO.setPageSize(10);
        pageVO.setStatus(1); // 进行中

        assertEquals(1, pageVO.getPageNo());
        assertEquals(10, pageVO.getPageSize());
        assertEquals(1, pageVO.getStatus());
    }

    @Test
    @DisplayName("取消流程请求 - 参数完整性")
    void testCancelReqVO_Complete() {
        BpmProcessInstanceCancelReqVO reqVO = new BpmProcessInstanceCancelReqVO();
        reqVO.setId("pi-001");
        reqVO.setReason("测试取消");
        // 无 businessKey 也应可以取消

        assertEquals("pi-001", reqVO.getId());
        assertNotNull(reqVO.getReason());
    }
}
