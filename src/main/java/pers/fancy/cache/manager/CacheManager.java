package pers.fancy.cache.manager;

import pers.fancy.cache.ICache;
import pers.fancy.cache.domain.CacheReadResult;
import pers.fancy.cache.domain.Pair;
import pers.fancy.cache.exception.CacheException;
import pers.fancy.cache.utils.CacheLogger;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author fancy
 */
@Singleton
public class CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

    /** defaultCache和cachePool直接使用Pair实现, 减小new Object的损耗*/
    private Pair<String, ICache> defaultCache;

    private Map<String, Pair<String, ICache>> cachePool = new ConcurrentHashMap<>();

    @Inject
    public void setCachePool(Map<String, ICache> caches) {
        // default cache impl
        Map.Entry<String, ICache> entry = caches.entrySet().iterator().next();
        this.defaultCache = Pair.of(entry.getKey(), entry.getValue());

        caches.forEach((name, cache) -> this.cachePool.put(name, Pair.of(name, cache)));
    }

    public Object readSingle(String cache, String key) {
        try {
            Pair<String, ICache> cacheImpl = getCacheImpl(cache);

            long start = System.currentTimeMillis();
            Object result = cacheImpl.getRight().read(key);
            CacheLogger.info("cache [{}] read single cost: [{}] ms",
                    cacheImpl.getLeft(),
                    (System.currentTimeMillis() - start));

            return result;
        } catch (Throwable e) {
            logger.error("read single cache failed, key: {} ", key, e);
            CacheLogger.error("read single cache failed, key: {} ", key, e);
            return null;
        }
    }

    public void writeSingle(String cache, String key, Object value, int expire) {
        if (value != null) {
            try {
                Pair<String, ICache> cacheImpl = getCacheImpl(cache);

                long start = System.currentTimeMillis();
                cacheImpl.getRight().write(key, value, expire);
                CacheLogger.info("cache [{}] write single cost: [{}] ms",
                        cacheImpl.getLeft(),
                        (System.currentTimeMillis() - start));

            } catch (Throwable e) {
                logger.error("write single cache failed, key: {} ", key, e);
                CacheLogger.error("write single cache failed, key: {} ", key, e);
            }
        }
    }

    public CacheReadResult readBatch(String cache, Collection<String> keys) {
        CacheReadResult cacheReadResult;
        if (keys.isEmpty()) {
            cacheReadResult = new CacheReadResult();
        } else {
            try {
                Pair<String, ICache> cacheImpl = getCacheImpl(cache);

                long start = System.currentTimeMillis();
                Map<String, Object> cacheMap = cacheImpl.getRight().read(keys);
                CacheLogger.info("cache [{}] read batch cost: [{}] ms",
                        cacheImpl.getLeft(),
                        (System.currentTimeMillis() - start));

                // collect not nit keys, keep order when full shooting
                Map<String, Object> hitValueMap = new LinkedHashMap<>();
                Set<String> notHitKeys = new LinkedHashSet<>();
                for (String key : keys) {
                    Object value = cacheMap.get(key);

                    if (value == null) {
                        notHitKeys.add(key);
                    } else {
                        hitValueMap.put(key, value);
                    }
                }

                cacheReadResult = new CacheReadResult(hitValueMap, notHitKeys);
            } catch (Throwable e) {
                logger.error("read multi cache failed, keys: {}", keys, e);
                CacheLogger.error("read multi cache failed, keys: {}", keys, e);
                cacheReadResult = new CacheReadResult();
            }
        }

        return cacheReadResult;
    }

    public void writeBatch(String cache, Map<String, Object> keyValueMap, int expire) {
        try {
            Pair<String, ICache> cacheImpl = getCacheImpl(cache);

            long start = System.currentTimeMillis();
            cacheImpl.getRight().write(keyValueMap, expire);
            CacheLogger.info("cache [{}] write batch cost: [{}] ms",
                    cacheImpl.getLeft(),
                    (System.currentTimeMillis() - start));

        } catch (Exception e) {
            logger.error("write map multi cache failed, keys: {}", keyValueMap.keySet(), e);
            CacheLogger.error("write map multi cache failed, keys: {}", keyValueMap.keySet(), e);
        }
    }

    public void remove(String cache, String... keys) {
        if (keys != null && keys.length != 0) {
            try {
                Pair<String, ICache> cacheImpl = getCacheImpl(cache);

                long start = System.currentTimeMillis();
                cacheImpl.getRight().remove(keys);
                CacheLogger.info("cache [{}] remove cost: [{}] ms",
                        cacheImpl.getLeft(),
                        (System.currentTimeMillis() - start));

            } catch (Throwable e) {
                logger.error("remove cache failed, keys: {}: ", keys, e);
                CacheLogger.error("remove cache failed, keys: {}: ", keys, e);
            }
        }
    }

    private Pair<String, ICache> getCacheImpl(String cacheName) {
        if (Strings.isNullOrEmpty(cacheName)) {
            return defaultCache;
        } else {
            return cachePool.computeIfAbsent(cacheName, (key) -> {
                throw new CacheException(String.format("no cache implementation named [%s].", key));
            });
        }
    }
}
