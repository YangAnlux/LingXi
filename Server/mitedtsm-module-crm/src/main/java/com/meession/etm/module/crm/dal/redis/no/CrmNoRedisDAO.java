package com.meession.etm.module.crm.dal.redis.no;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.meession.etm.module.crm.dal.redis.RedisKeyConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;


/**
 * Crm 订单序号的 Redis DAO
 *
 * @author HUIHUI
 */
@Repository
public class CrmNoRedisDAO {

    /**
     * 合同 {@link com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO}
     */
    public static final String CONTRACT_NO_PREFIX = "HT";

    /**
     * 回款 {@link com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO}
     */
    public static final String RECEIVABLE_PREFIX = "HK";

    // [ADD START] 发票序号前缀 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
    /**
     * 发票 {@link com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO}
     */
    public static final String INVOICE_PREFIX = "FP";
    // [ADD END] 发票序号前缀 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14

    // [ADD START] 费用序号前缀 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
    /**
     * 费用 {@link com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO}
     */
    public static final String EXPENSE_PREFIX = "FY";
    // [ADD END] 费用序号前缀 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16

    // [ADD START] 报销序号前缀 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
    /**
     * 报销 {@link com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO}
     */
    public static final String REIMBURSEMENT_PREFIX = "BX";
    // [ADD END] 报销序号前缀 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16

    // [ADD START] 退款序号前缀 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
    /**
     * 退款 {@link com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO}
     */
    public static final String REFUND_PREFIX = "TK";
    // [ADD END] 退款序号前缀 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成序号，使用当前日期，格式为 {PREFIX} + yyyyMMdd + 6 位自增
     * 例如说：QTRK 202109 000001 （没有中间空格）
     *
     * @param prefix 前缀
     * @return 序号
     */
    public String generate(String prefix) {
        // 递增序号
        String noPrefix = prefix + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        String key = RedisKeyConstants.NO + noPrefix;
        Long no = stringRedisTemplate.opsForValue().increment(key);
        // 设置过期时间
        stringRedisTemplate.expire(key, Duration.ofDays(1L));
        return noPrefix + String.format("%06d", no);
    }

}
