package com.ugrong.framework.redis.repository.cache;

import com.ugrong.framework.redis.domain.EnumRedisType;
import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.impl.AbstractListRedisRepository;

import java.awt.*;

public class ListRedisRepository extends AbstractListRedisRepository<List> {
    @Override
    public IRedisCacheType getCacheType() {
        return EnumRedisType.LIST_CACHE;
    }
}
