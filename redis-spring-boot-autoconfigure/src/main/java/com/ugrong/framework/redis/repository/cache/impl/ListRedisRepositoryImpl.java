package com.ugrong.framework.redis.repository.cache.impl;

import com.ugrong.framework.redis.domain.EnumRedisType;
import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.IListRedisRepository;
import org.springframework.data.redis.core.ListOperations;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ListRedisRepositoryImpl<T extends Serializable> extends AbstractListRedisRepository<T> implements IListRedisRepository<T> {
    private ListOperations<String, T> getListOperation() {
        return this.geTemplate().opsForList();
    }


    @Override
    public void addWithDefaultTimeout(String keySuffix, T value) {
        this.getListOperation().rightPush(keySuffix, value);
        //默认30秒
        geTemplate().expire(keySuffix, 30, TimeUnit.SECONDS);
    }


    @Override
    public Long addAllWithDefaultTimeout(String keySuffix, T[] values) {
        //默认30秒
        geTemplate().expire(keySuffix, 30, TimeUnit.SECONDS);
        return this.getListOperation().rightPushAll(keySuffix, values);
    }


    @Override
    public void expireWithDefaultTimeout(String keySuffix) {
        geTemplate().expire(keySuffix, 20, TimeUnit.SECONDS);
    }


    @Override
    public List<T> get(String keySuffix) {
        return geTemplate().opsForList().range(keySuffix, 0, -1);
    }

    @Override
    public Long size(String keySuffix) {
        return geTemplate().opsForList().size(keySuffix);
    }

    @Override
    public IRedisCacheType getCacheType() {
        return EnumRedisType.LIST_CACHE;
    }


    @Override
    public Set<String> keys() {
        return super.keys(getCacheType());
    }
}
