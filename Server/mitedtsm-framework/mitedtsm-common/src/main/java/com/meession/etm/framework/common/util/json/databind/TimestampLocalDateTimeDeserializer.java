package com.meession.etm.framework.common.util.json.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 基于时间戳的 LocalDateTime 反序列化器
 *
 * @author 老五
 */
public class TimestampLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    public static final TimestampLocalDateTimeDeserializer INSTANCE = new TimestampLocalDateTimeDeserializer();

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // null 值直接返回 null
        if (p.getCurrentToken() == com.fasterxml.jackson.core.JsonToken.VALUE_NULL) {
            return null;
        }
        long epochMilli = p.getValueAsLong();
        // epoch 0 表示未设置时间，返回 null
        if (epochMilli == 0) {
            return null;
        }
        // 将 Long 时间戳，转换为 LocalDateTime 对象
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

}
