package com.meession.etm.module.bpm.definition;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.framework.test.core.util.AssertUtils;
import com.meession.etm.framework.test.core.util.RandomUtils;
import com.meession.etm.module.bpm.controller.admin.definition.vo.category.BpmCategoryPageReqVO;
import com.meession.etm.module.bpm.controller.admin.definition.vo.category.BpmCategorySaveReqVO;
import com.meession.etm.module.bpm.dal.dataobject.definition.BpmCategoryDO;
import com.meession.etm.module.bpm.dal.mysql.category.BpmCategoryMapper;
import com.meession.etm.module.bpm.service.definition.BpmCategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import jakarta.annotation.Resource;

import static com.meession.etm.module.bpm.enums.ErrorCodeConstants.CATEGORY_NOT_EXISTS;

/**
 * 流程分类 Service 层单元测试
 */
@Import(BpmCategoryServiceImpl.class)
public class BpmCategoryServiceTest extends BaseDbUnitTest {

    @Resource
    private BpmCategoryServiceImpl categoryService;

    @Resource
    private BpmCategoryMapper categoryMapper;

    // ==================== createCategory ====================

    @Test
    @DisplayName("TC-C01-001: 创建流程分类 - 正向测试")
    public void testCreateCategory_Success() {
        // 准备参数
        BpmCategorySaveReqVO reqVO = RandomUtils.randomPojo(BpmCategorySaveReqVO.class);

        // 调用
        Long categoryId = categoryService.createCategory(reqVO);

        // 断言
        Assertions.assertNotNull(categoryId);
        BpmCategoryDO category = categoryMapper.selectById(categoryId);
        AssertUtils.assertPojoEquals(reqVO, category);
    }

    // ==================== updateCategory ====================

    @Test
    @DisplayName("TC-C02-001: 更新流程分类 - 正向测试")
    public void testUpdateCategory_Success() {
        // 准备已有数据
        BpmCategoryDO dbCategory = RandomUtils.randomPojo(BpmCategoryDO.class);
        categoryMapper.insert(dbCategory);

        // 准备更新参数
        BpmCategorySaveReqVO reqVO = RandomUtils.randomPojo(BpmCategorySaveReqVO.class, o -> {
            o.setId(dbCategory.getId());
        });

        // 调用
        categoryService.updateCategory(reqVO);

        // 验证
        BpmCategoryDO updated = categoryMapper.selectById(reqVO.getId());
        AssertUtils.assertPojoEquals(reqVO, updated);
    }

    @Test
    @DisplayName("更新流程分类 - 分类不存在")
    public void testUpdateCategory_NotFound() {
        BpmCategorySaveReqVO reqVO = RandomUtils.randomPojo(BpmCategorySaveReqVO.class);

        AssertUtils.assertServiceException(
                () -> categoryService.updateCategory(reqVO),
                CATEGORY_NOT_EXISTS);
    }

    // ==================== deleteCategory ====================

    @Test
    @DisplayName("TC-C04-001: 删除流程分类 - 正向测试")
    public void testDeleteCategory_Success() {
        // 准备已有数据
        BpmCategoryDO dbCategory = RandomUtils.randomPojo(BpmCategoryDO.class);
        categoryMapper.insert(dbCategory);

        // 调用
        categoryService.deleteCategory(dbCategory.getId());

        // 验证
        BpmCategoryDO deleted = categoryMapper.selectById(dbCategory.getId());
        Assertions.assertNull(deleted);
    }

    @Test
    @DisplayName("删除流程分类 - 分类不存在")
    public void testDeleteCategory_NotFound() {
        AssertUtils.assertServiceException(
                () -> categoryService.deleteCategory(99999L),
                CATEGORY_NOT_EXISTS);
    }

    // ==================== getCategory ====================

    @Test
    @DisplayName("TC-C05-001: 获取流程分类 - 正向测试")
    public void testGetCategory_Success() {
        // 准备已有数据
        BpmCategoryDO dbCategory = RandomUtils.randomPojo(BpmCategoryDO.class);
        categoryMapper.insert(dbCategory);

        // 调用
        BpmCategoryDO result = categoryService.getCategory(dbCategory.getId());

        // 验证
        Assertions.assertNotNull(result);
        Assertions.assertEquals(dbCategory.getId(), result.getId());
        Assertions.assertEquals(dbCategory.getName(), result.getName());
    }

    @Test
    @DisplayName("获取流程分类 - 不存在返回null")
    public void testGetCategory_NotFound() {
        BpmCategoryDO result = categoryService.getCategory(99999L);
        Assertions.assertNull(result);
    }

    // ==================== getCategoryPage ====================

    @Test
    @DisplayName("获取流程分类分页 - 正向测试")
    public void testGetCategoryPage_Success() {
        // 准备已有数据
        BpmCategoryDO dbCategory = RandomUtils.randomPojo(BpmCategoryDO.class);
        categoryMapper.insert(dbCategory);

        BpmCategoryPageReqVO pageReqVO = new BpmCategoryPageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(20);

        // 调用
        PageResult<BpmCategoryDO> result = categoryService.getCategoryPage(pageReqVO);

        // 验证
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getTotal() >= 1);
    }

    @Test
    @DisplayName("获取流程分类分页 - 空结果")
    public void testGetCategoryPage_Empty() {
        BpmCategoryPageReqVO pageReqVO = new BpmCategoryPageReqVO();
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(20);
        pageReqVO.setName("不存在的分类");

        PageResult<BpmCategoryDO> result = categoryService.getCategoryPage(pageReqVO);
        Assertions.assertNotNull(result);
    }
}
