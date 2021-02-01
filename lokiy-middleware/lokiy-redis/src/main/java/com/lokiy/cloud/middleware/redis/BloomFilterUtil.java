package com.lokiy.cloud.middleware.redis;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: Lokiy.Lu
 * @Date: 2021/2/1 16:18
 * @Description: redisson实现的布隆过滤器
 */
@Component
public class BloomFilterUtil {

    private static final String BLOOM_FILTER_MAP = "BLOOM_FILTER_MAP";

    @Autowired
    private RedissonClient redisson;

    private RBloomFilter<String> bloomFilter;

    @PostConstruct
    private void initBloomFilter(){
        this.bloomFilter = redisson.getBloomFilter(BLOOM_FILTER_MAP);
        //初始化布隆过滤器：预计元素为10000L,误差率为3%
        this.bloomFilter.tryInit(1000000L,0.03);
    }

    /**
     * 是否包含在布隆过滤器中
     * @param key 查询的key
     * @return 存在与否
     */
    public boolean isContains(String key){
        return this.bloomFilter.contains(key);
    }

    /**
     * 把key存入布隆过滤器中
     * @param key 键
     */
    public void put(String key){
        this.bloomFilter.add(key);
    }

}
