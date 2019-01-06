package pers.fancy.cache.support.cache;

import pers.fancy.cache.ICache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Google开源库，相当于apache commons
 * @author fancy
 */
public class GuavaCache implements ICache {

    private LoadingCache<String, Object> guavaCache;

    public GuavaCache(long size, long expire) {
        guavaCache = CacheBuilder
                .newBuilder()
                .maximumSize(size)
                .expireAfterWrite(expire, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) {
                        return null;
                    }
                });
    }

    @Override
    public Object read(String key) {
        return guavaCache.getIfPresent(key);
    }

    @Override
    public Map<String, Object> read(Collection<String> keys) {
        return guavaCache.getAllPresent(keys);
    }

    @Override
    public void write(String key, Object value, long expire) {
        guavaCache.put(key, value);
    }

    @Override
    public void write(Map<String, Object> keyValueMap, long expire) {
        guavaCache.putAll(keyValueMap);
    }

    @Override
    public void remove(String... keys) {
        guavaCache.invalidateAll(Arrays.asList(keys));
    }
}
