package ru.company.news.api.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Class for defining cache options.
 *
 * @author Anton Salazhkov
 * @version 1.0
 */
@Component
public class Cash {

    /**
     * Set hash storage time to 1800 seconds (30 minutes).
     */
    @Bean
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager() {

            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(name,
                        CacheBuilder
                                .newBuilder()
                                .expireAfterWrite(1800, TimeUnit.SECONDS)
                                .build()
                                .asMap(),
                        false);
            }
        };
    }
}
