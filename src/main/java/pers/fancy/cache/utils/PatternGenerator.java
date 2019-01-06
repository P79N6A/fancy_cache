package pers.fancy.cache.utils;

import pers.fancy.cache.CacheKey;
import pers.fancy.cache.domain.CacheAnnoHolder;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class PatternGenerator {

    private static final ConcurrentMap<Method, String> patterns = new ConcurrentHashMap<>();

    public static String generatePattern(CacheAnnoHolder cacheXAnnoHolder) {
        return patterns.computeIfAbsent(cacheXAnnoHolder.getMethod(), (method) -> doPatternCombiner(cacheXAnnoHolder));
    }

    private static String doPatternCombiner(CacheAnnoHolder cacheXAnnoHolder) {
        StringBuilder sb = new StringBuilder(cacheXAnnoHolder.getPrefix());
        Collection<CacheKey> cacheKeys = cacheXAnnoHolder.getCacheKeyMap().values();
        for (CacheKey cacheKey : cacheKeys) {
            sb.append(cacheKey.value());
        }

        return sb.toString();
    }
}
