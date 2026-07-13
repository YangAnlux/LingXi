package com.meession.etm.module.bpm.definition;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.framework.test.core.util.AssertUtils;
import com.meession.etm.framework.test.core.util.RandomUtils;
import com.meession.etm.module.bpm.controller.admin.definition.vo.form.BpmFormPageReqVO;
import com.meession.etm.module.bpm.controller.admin.definition.vo.form.BpmFormSaveReqVO;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.meession.etm.module.bpm.dal.mysql.definition.BpmFormMapper;
import com.meession.etm.module.bpm.service.definition.BpmFormServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import jakarta.annotation.Resource;

import static com.meession.etm.module.bpm.enums.ErrorCodeConstants.FORM_NOT_EXISTS;

/**
 * 动态表单 Service 层单元测试
 */
@Import(BpmFormServiceImpl.class)
public class BpmFormServiceTest extends BaseDbUnitTest {

    @Resource
    private BpmFormServiceImpl formService;

    @Resource
    private BpmFormMapper formMapper;

    @Test
    @DisplayName("TC-F01-001: 创建表单 - 正向测试")
    public void testCreateForm_Success() {
        BpmFormSaveReqVO reqVO = RandomUtils.randomPojo(BpmFormSaveReqVO.class);

        Long formId = formService.createForm(reqVO);

        Assertions.assertNotNull(formId);
        BpmFormDO form = formMapper.selectById(formId);
        AssertUtils.assertPojoEquals(reqVO, form);
    }

    @Test
    @DisplayName("TC-F02-001: 更新表单 - 正向测试")
    public void testUpdateForm_Success() {
        BpmFormDO dbForm = RandomUtils.randomPojo(BpmFormDO.class);
        formMapper.insert(dbForm);

        BpmFormSaveReqVO reqVO = RandomUtils.randomPojo(BpmFormSaveReqVO.class, o -> {
            o.setId(dbForm.getId());
        });

        formService.updateForm(reqVO);

        BpmFormDO updated = formMapper.selectById(reqVO.getId());
        AssertUtils.assertPojoEquals(reqVO, updated);
    }

    @Test
    @DisplayName("更新表单 - 表单不存在")
    public void testUpdateForm_NotFound() {
        BpmFormSaveReqVO reqVO = RandomUtils.randomPojo(BpmFormSaveReqVO.class);

        AssertUtils.assertServiceException(
                () -> formService.updateForm(reqVO),
                FORM_NOT_EXISTS);
    }

    @Test
    @DisplayName("TC-F03-001: 删除表单 - 正向测试")
    public void testDeleteForm_Success() {
        BpmFormDO dbForm = RandomUtils.randomPojo(BpmFormDO.class);
        formMapper.insert(dbForm);

        formService.deleteForm(dbForm.getId());

        BpmFormDO deleted = formMapper.selectById(dbForm.getId());
        Assertions.assertNull(deleted);
    }

    @Test
    @DisplayName("获取表单 - 正向测试")
    public void testGetForm_Success() {
        BpmFormDO dbForm = RandomUtils.randomPojo(BpmFormDO.class);
        formMapper.insert(dbForm);

        BpmFormDO result = formService.getForm(dbForm.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(dbForm.getName(), result.getName());
    }

    @Test
    @DisplayName("获取表单 - 不存在")
    public void testGetForm_NotFound() {
        BpmFormDO result = formService.getForm(99999L);
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("获取表单列表 - 正向测试")
    public void testGetFormList_Success() {
        BpmFormDO dbForm = RandomUtils.randomPojo(BpmFormDO.class);
        formMapper.insert(dbForm);

        var result = formService.getFormList();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() >= 1);
    }

    @Test
    @DisplayName("获取表单分页 - 正向测试")
    public void testGetFormPage_Success() {
        BpmFormDO dbForm = RandomUtils.randomPojo(BpmFormDO.class);
        formMapper.insert(dbForm);

        BpmFormPageReqVO pageReqVO = new BpmFormPageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(20);

        var result = formService.getFormPage(pageReqVO);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotal() >= 1);
    }
}
