// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票类型枚举 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.enums.invoice;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 发票类型枚举
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.14
 */
@RequiredArgsConstructor
@Getter
public enum CrmInvoiceTypeEnum implements ArrayValuable<Integer> {

    GENERAL(1, "增值税普通发票"),
    SPECIAL(2, "增值税专用发票");

    private final Integer type;
    private final String name;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmInvoiceTypeEnum::getType).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
// [ADD END] 发票类型枚举 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
