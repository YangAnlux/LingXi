package com.meession.etm.module.crm.dal.redis;

/**
 * CRM Redis Key 枚举类
 *
 * @author 密讯
 */
public interface RedisKeyConstants {

    /**
     * 序号的缓存
     *
     * KEY 格式：trade_no:{prefix}
     * VALUE 数据格式：编号自增
     */
    String NO = "crm:seq_no:";

    /**
     * 客户关怀去重锁
     *
     * KEY 格式：care:send:{customerId}:{yyyy-MM-dd}
     * VALUE 数据格式："1"，TTL 24 小时
     * 用途：防止同一客户在同一天收到重复的关怀消息
     */
    String CARE_SEND = "care:send:";

}
