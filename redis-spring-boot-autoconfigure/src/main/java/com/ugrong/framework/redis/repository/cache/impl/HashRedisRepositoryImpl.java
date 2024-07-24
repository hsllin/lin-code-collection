package com.ugrong.framework.redis.repository.cache.impl;

import com.ugrong.framework.redis.domain.EnumRedisType;
import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.IHashRedisRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HashRedisRepositoryImpl<K, V extends Serializable> extends AbstractHashRedisRepository<K, V> implements IHashRedisRepository<K, V> {

    public HashOperations getHashOperation(String key) {
        return geTemplate().opsForHash();
    }

    @Override
    public IRedisCacheType getCacheType() {
        return EnumRedisType.HASH_CACHE;
    }
}
