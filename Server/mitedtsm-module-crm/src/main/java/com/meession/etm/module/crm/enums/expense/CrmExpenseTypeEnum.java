// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用类型枚举 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.enums.expense;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 费用类型枚举
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@RequiredArgsConstructor
@Getter
public enum CrmExpenseTypeEnum implements ArrayValuable<Integer> {

    TRAVEL(1, "差旅"),
    TRANSPORT(2, "交通"),
    MEAL(3, "餐饮"),
    OFFICE(4, "办公"),
    OTHER(5, "其他");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmExpenseTypeEnum::getType).toArray(Integer[]::new);

    private final Integer type;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
// [ADD END] 费用类型枚举 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
