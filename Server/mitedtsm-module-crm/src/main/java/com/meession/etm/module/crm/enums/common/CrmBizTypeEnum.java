package com.meession.etm.module.crm.enums.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 业务类型枚举
 *
 * @author HUIHUI
 */
@RequiredArgsConstructor
@Getter
public enum CrmBizTypeEnum implements ArrayValuable<Integer> {

    CRM_CLUE(1, "线索"),
    CRM_CUSTOMER(2, "客户"),
    CRM_CONTACT(3, "联系人"),
    CRM_BUSINESS(4, "商机"),
    CRM_CONTRACT(5, "合同"),
    CRM_PRODUCT(6, "产品"),
    CRM_RECEIVABLE(7, "回款"),
    CRM_RECEIVABLE_PLAN(8, "回款计划"),
    // [ADD START] 发票 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
    CRM_INVOICE(9, "发票"),
    // [ADD END] 发票 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
    // [ADD START] 费用 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
    CRM_EXPENSE(10, "费用"),
    // [ADD END] 费用 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmBizTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 名称
     */
    private final String name;

    public static String getNameByType(Integer type) {
        CrmBizTypeEnum typeEnum = CollUtil.findOne(CollUtil.newArrayList(CrmBizTypeEnum.values()),
                item -> ObjUtil.equal(item.type, type));
        return typeEnum == null ? null : typeEnum.getName();
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
