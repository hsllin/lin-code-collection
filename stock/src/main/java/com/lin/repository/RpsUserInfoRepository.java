package com.lin.repository;

import com.lin.bean.rps.RpsUserInfo;
import com.lin.type.EnumRpsUserInfoCacheType;
import com.ugrong.framework.redis.domain.IRedisCacheType;
import com.ugrong.framework.redis.repository.cache.impl.AbstractSimpleRedisRepository;

/**
 * 佛祖保佑此代码没有bug，即使有也一眼看出
 *
 * @author lin
 * @Description
 * @create 2025-05-15 15:47
 */
public class RpsUserInfoRepository extends AbstractSimpleRedisRepository<RpsUserInfo> {
    @Override
    public IRedisCacheType getCacheType() {
        return EnumRpsUserInfoCacheType.RPS_USER_INFO_CACHE_TYPE;
    }
}