package com.meession.etm.module.bpm.definition;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.framework.test.core.util.AssertUtils;
import com.meession.etm.framework.test.core.util.RandomUtils;
import com.meession.etm.module.bpm.controller.admin.definition.vo.expression.BpmProcessExpressionPageReqVO;
import com.meession.etm.module.bpm.controller.admin.definition.vo.expression.BpmProcessExpressionSaveReqVO;
import com.meession.etm.module.bpm.controller.admin.definition.vo.listener.BpmProcessListenerPageReqVO;
import com.meession.etm.module.bpm.controller.admin.definition.vo.listener.BpmProcessListenerSaveReqVO;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmProcessExpressionDO;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmProcessListenerDO;
import com.meession.etm.module.bpm.dal.mysql.definition.BpmProcessExpressionMapper;
import com.meession.etm.module.bpm.dal.mysql.definition.BpmProcessListenerMapper;
import com.meession.etm.module.bpm.service.definition.BpmProcessExpressionServiceImpl;
import com.meession.etm.module.bpm.service.definition.BpmProcessListenerServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import jakarta.annotation.Resource;

import static com.meession.etm.module.bpm.enums.ErrorCodeConstants.*;

/**
 * 流程监听器 + 流程表达式 Service 层单元测试 (P2 模块)
 */
public class BpmProcessConfigServiceTest {

    // ========== 流程监听器 ==========

    @Import(BpmProcessListenerServiceImpl.class)
    @Nested
    @DisplayName("流程监听器 Service")
    class ProcessListenerTest extends BaseDbUnitTest {

        @Resource
        private BpmProcessListenerServiceImpl listenerService;

        @Resource
        private BpmProcessListenerMapper listenerMapper;

        @Test
        @DisplayName("TC-L01-001: 创建监听器 - 正向测试")
        public void testCreateListener_Success() {
            BpmProcessListenerSaveReqVO reqVO = RandomUtils.randomPojo(BpmProcessListenerSaveReqVO.class);

            Long id = listenerService.createProcessListener(reqVO);

            Assertions.assertNotNull(id);
            BpmProcessListenerDO entity = listenerMapper.selectById(id);
            AssertUtils.assertPojoEquals(reqVO, entity);
        }

        @Test
        @DisplayName("更新监听器 - 正向测试")
        public void testUpdateListener_Success() {
            BpmProcessListenerDO dbEntity = RandomUtils.randomPojo(BpmProcessListenerDO.class);
            listenerMapper.insert(dbEntity);

            BpmProcessListenerSaveReqVO reqVO = RandomUtils.randomPojo(BpmProcessListenerSaveReqVO.class, o -> {
                o.setId(dbEntity.getId());
            });

            listenerService.updateProcessListener(reqVO);

            BpmProcessListenerDO updated = listenerMapper.selectById(reqVO.getId());
            AssertUtils.assertPojoEquals(reqVO, updated);
        }

        @Test
        @DisplayName("删除监听器 - 正向测试")
        public void testDeleteListener_Success() {
            BpmProcessListenerDO dbEntity = RandomUtils.randomPojo(BpmProcessListenerDO.class);
            listenerMapper.insert(dbEntity);

            listenerService.deleteProcessListener(dbEntity.getId());

            BpmProcessListenerDO deleted = listenerMapper.selectById(dbEntity.getId());
            Assertions.assertNull(deleted);
        }

        @Test
        @DisplayName("获取监听器 - 正向测试")
        public void testGetListener_Success() {
            BpmProcessListenerDO dbEntity = RandomUtils.randomPojo(BpmProcessListenerDO.class);
            listenerMapper.insert(dbEntity);

            BpmProcessListenerDO result = listenerService.getProcessListener(dbEntity.getId());
            Assertions.assertNotNull(result);
            Assertions.assertEquals(dbEntity.getName(), result.getName());
        }

        @Test
        @DisplayName("获取监听器 - 不存在")
        public void testGetListener_NotFound() {
            BpmProcessListenerDO result = listenerService.getProcessListener(99999L);
            Assertions.assertNull(result);
        }

        @Test
        @DisplayName("获取监听器分页 - 正向测试")
        public void testGetListenerPage_Success() {
            BpmProcessListenerDO dbEntity = RandomUtils.randomPojo(BpmProcessListenerDO.class);
            listenerMapper.insert(dbEntity);

            BpmProcessListenerPageReqVO pageVO = new BpmProcessListenerPageReqVO();
            pageVO.setPageNo(1);
            pageVO.setPageSize(20);

            var result = listenerService.getProcessListenerPage(pageVO);
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.getTotal() >= 1);
        }

        @Test
        @DisplayName("更新监听器 - 不存在")
        public void testUpdateListener_NotFound() {
            BpmProcessListenerSaveReqVO reqVO = RandomUtils.randomPojo(BpmProcessListenerSaveReqVO.class);

            AssertUtils.assertServiceException(
                    () -> listenerService.updateProcessListener(reqVO),
                    PROCESS_LISTENER_NOT_EXISTS);
        }
    }

    // ========== 流程表达式 ==========

    @Import(BpmProcessExpressionServiceImpl.class)
    @Nested
    @DisplayName("流程表达式 Service")
    class ProcessExpressionTest extends BaseDbUnitTest {

        @Resource
        private BpmProcessExpressionServiceImpl expressionService;

        @Resource
        private BpmProcessExpressionMapper expressionMapper;

        @Test
        @DisplayName("TC-E01-001: 创建表达式 - 正向测试")
        public void testCreateExpression_Success() {
            BpmProcessExpressionSaveReqVO reqVO = RandomUtils.randomPojo(BpmProcessExpressionSaveReqVO.class);

            Long id = expressionService.createProcessExpression(reqVO);

            Assertions.assertNotNull(id);
            BpmProcessExpressionDO entity = expressionMapper.selectById(id);
            AssertUtils.assertPojoEquals(reqVO, entity);
        }

        @Test
        @DisplayName("更新表达式 - 正向测试")
        public void testUpdateExpression_Success() {
            BpmProcessExpressionDO dbEntity = RandomUtils.randomPojo(BpmProcessExpressionDO.class);
            expressionMapper.insert(dbEntity);

            BpmProcessExpressionSaveReqVO reqVO = RandomUtils.randomPojo(BpmProcessExpressionSaveReqVO.class, o -> {
                o.setId(dbEntity.getId());
            });

            expressionService.updateProcessExpression(reqVO);

            BpmProcessExpressionDO updated = expressionMapper.selectById(reqVO.getId());
            AssertUtils.assertPojoEquals(reqVO, updated);
        }

        @Test
        @DisplayName("删除表达式 - 正向测试")
        public void testDeleteExpression_Success() {
            BpmProcessExpressionDO dbEntity = RandomUtils.randomPojo(BpmProcessExpressionDO.class);
            expressionMapper.insert(dbEntity);

            expressionService.deleteProcessExpression(dbEntity.getId());

            BpmProcessExpressionDO deleted = expressionMapper.selectById(dbEntity.getId());
            Assertions.assertNull(deleted);
        }

        @Test
        @DisplayName("获取表达式 - 正向测试")
        public void testGetExpression_Success() {
            BpmProcessExpressionDO dbEntity = RandomUtils.randomPojo(BpmProcessExpressionDO.class);
            expressionMapper.insert(dbEntity);

            BpmProcessExpressionDO result = expressionService.getProcessExpression(dbEntity.getId());
            Assertions.assertNotNull(result);
            Assertions.assertEquals(dbEntity.getName(), result.getName());
        }

        @Test
        @DisplayName("获取表达式 - 不存在")
        public void testGetExpression_NotFound() {
            BpmProcessExpressionDO result = expressionService.getProcessExpression(99999L);
            Assertions.assertNull(result);
        }

        @Test
        @DisplayName("获取表达式分页 - 正向测试")
        public void testGetExpressionPage_Success() {
            BpmProcessExpressionDO dbEntity = RandomUtils.randomPojo(BpmProcessExpressionDO.class);
            expressionMapper.insert(dbEntity);

            BpmProcessExpressionPageReqVO pageVO = new BpmProcessExpressionPageReqVO();
            pageVO.setPageNo(1);
            pageVO.setPageSize(20);

            var result = expressionService.getProcessExpressionPage(pageVO);
            Assertions.assertNotNull(result);
            Assertions.assertTrue(result.getTotal() >= 1);
        }
    }
}
