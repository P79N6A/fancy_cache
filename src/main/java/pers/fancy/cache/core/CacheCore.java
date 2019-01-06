package pers.fancy.cache.core;

import pers.fancy.cache.Cached;
import pers.fancy.cache.CachedGet;
import pers.fancy.cache.Invalid;
import pers.fancy.cache.domain.CacheAnnoHolder;
import pers.fancy.cache.domain.CacheMethodHolder;
import pers.fancy.cache.domain.Pair;
import pers.fancy.cache.invoker.Invoker;
import pers.fancy.cache.manager.CacheManager;
import pers.fancy.cache.reader.AbstractCacheReader;
import pers.fancy.cache.utils.CacheXInfoContainer;
import pers.fancy.cache.utils.CacheLogger;
import pers.fancy.cache.utils.KeyGenerator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import static pers.fancy.cache.utils.SwitcherUtil.isSwitchOn;


/**
 * 核心实现
 *
 * @author fancy
 */
@Singleton
public class CacheCore {

    @Inject
    private CacheConfig config;

    @Inject
    private CacheManager cacheManager;

    @Inject
    @Named("singleCacheReader")
    private AbstractCacheReader singleCacheReader;

    @Inject
    @Named("multiCacheReader")
    private AbstractCacheReader multiCacheReader;

    public Object read(CachedGet cachedGet, Method method, Invoker invoker) throws Throwable {
        Object result;
        if (isSwitchOn(config, cachedGet, method, invoker.getArgs())) {
            result = doReadWrite(method, invoker, false);
        } else {
            result = invoker.proceed();
        }

        return result;
    }

    public Object readWrite(Cached cached, Method method, Invoker invoker) throws Throwable {
        Object result;
        if (isSwitchOn(config, cached, method, invoker.getArgs())) {
            result = doReadWrite(method, invoker, true);
        } else {
            result = invoker.proceed();
        }

        return result;
    }


    private Object doReadWrite(Method method, Invoker invoker, boolean needWrite) throws Throwable {
        long start = System.currentTimeMillis();

        Pair<CacheAnnoHolder, CacheMethodHolder> pair = CacheXInfoContainer.getCacheXInfo(method);
        CacheAnnoHolder cacheXAnnoHolder = pair.getLeft();
        CacheMethodHolder cacheXMethodHolder = pair.getRight();

        Object result;
        if (cacheXAnnoHolder.isMulti()) {
            result = multiCacheReader.read(cacheXAnnoHolder, cacheXMethodHolder, invoker, needWrite);
        } else {
            result = singleCacheReader.read(cacheXAnnoHolder, cacheXMethodHolder, invoker, needWrite);
        }

        CacheLogger.debug("cache read total cost [{}] ms", (System.currentTimeMillis() - start));

        return result;
    }


    public void remove(Invalid invalid, Method method, Object[] args) {
        if (isSwitchOn(config, invalid, method, args)) {

            long start = System.currentTimeMillis();

            CacheAnnoHolder cacheXAnnoHolder = CacheXInfoContainer.getCacheXInfo(method).getLeft();
            if (cacheXAnnoHolder.isMulti()) {
                Map[] pair = KeyGenerator.generateMultiKey(cacheXAnnoHolder, args);
                Set<String> keys = ((Map<String, Object>) pair[1]).keySet();
                cacheManager.remove(invalid.value(), keys.toArray(new String[keys.size()]));

                CacheLogger.info("multi cache clear, keys: {}", keys);
            } else {
                String key = KeyGenerator.generateSingleKey(cacheXAnnoHolder, args);
                cacheManager.remove(invalid.value(), key);

                CacheLogger.info("single cache clear, key: {}", key);
            }

            CacheLogger.debug("cache clear total cost [{}] ms", (System.currentTimeMillis() - start));
        }
    }

    public void write() {
        // TODO on @CachedPut
    }
}
