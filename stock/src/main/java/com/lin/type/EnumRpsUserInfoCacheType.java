package com.lin.type;

import com.ugrong.framework.redis.domain.IRedisCacheType;

import java.io.Serializable;

public enum EnumRpsUserInfoCacheType implements IRedisCacheType {
    /**
     * 学生信息缓存
     */
    RPS_USER_INFO_CACHE_TYPE(" RPS_USER_INFO");

    private final String value;

    EnumRpsUserInfoCacheType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
