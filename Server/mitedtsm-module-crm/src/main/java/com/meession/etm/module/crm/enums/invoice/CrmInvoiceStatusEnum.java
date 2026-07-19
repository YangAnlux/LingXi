// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票状态枚举 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.enums.invoice;

import com.meession.etm.framework.common.core.ArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 发票状态枚举
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.14
 */
@RequiredArgsConstructor
@Getter
public enum CrmInvoiceStatusEnum implements ArrayValuable<Integer> {

    PENDING(0, "待开票"),
    ISSUED(1, "已开票"),
    CANCELLED(2, "已作废");

    private final Integer status;
    private final String name;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CrmInvoiceStatusEnum::getStatus).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
// [ADD END] 发票状态枚举 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
