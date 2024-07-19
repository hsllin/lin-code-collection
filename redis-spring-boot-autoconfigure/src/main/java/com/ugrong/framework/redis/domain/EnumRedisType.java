package com.ugrong.framework.redis.domain;

public enum EnumRedisType implements IRedisCacheType {

    /**
     * list信息缓存
     */
    LIST_CACHE("LIST"),

    /**
     * HASH信息缓存
     */
    HASH_CACHE("HASH");

    private final String value;

    EnumRedisType(String value) {
        this.value = value;
    }

    @Override
    public String getType() {
        return IRedisCacheType.super.getType();
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
