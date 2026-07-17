// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款类型枚举 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.enums.refund;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 退款类型枚举
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@RequiredArgsConstructor
@Getter
public enum CrmRefundTypeEnum implements ArrayValuable<Integer> {

    FULL(1, "全额退款"),
    PARTIAL(2, "部分退款");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmRefundTypeEnum::getType).toArray(Integer[]::new);

    private final Integer type;
    private final String name;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
// [ADD END] 退款类型枚举 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
