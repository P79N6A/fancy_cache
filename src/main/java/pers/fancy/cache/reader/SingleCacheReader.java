package pers.fancy.cache.reader;

import pers.fancy.cache.ShootingMXBean;
import pers.fancy.cache.core.CacheConfig;
import pers.fancy.cache.domain.CacheAnnoHolder;
import pers.fancy.cache.domain.CacheMethodHolder;
import pers.fancy.cache.invoker.Invoker;
import pers.fancy.cache.manager.CacheManager;
import pers.fancy.cache.utils.PatternGenerator;
import pers.fancy.cache.utils.PreventObjects;
import pers.fancy.cache.utils.CacheLogger;
import pers.fancy.cache.utils.KeyGenerator;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 *
 * @author fancy
 */
@Singleton
public class SingleCacheReader extends AbstractCacheReader {

    @Inject
    private CacheManager cacheManager;

    @Inject
    private CacheConfig config;

    @Inject(optional = true)
    private ShootingMXBean shootingMXBean;

    @Override
    public Object read(CacheAnnoHolder cacheXAnnoHolder, CacheMethodHolder cacheXMethodHolder, Invoker invoker, boolean needWrite) throws Throwable {
        String key = KeyGenerator.generateSingleKey(cacheXAnnoHolder, invoker.getArgs());
        Object readResult = cacheManager.readSingle(cacheXAnnoHolder.getCache(), key);

        //记录命中率
        doRecord(readResult, key, cacheXAnnoHolder);

        // 命中
        if (readResult != null) {
            // 释放击穿对象
            if (PreventObjects.isPrevent(readResult)) {
                return null;
            }

            return readResult;
        }


        // not hit
        // invoke method
        Object invokeResult = doLogInvoke(invoker::proceed);
        if (invokeResult != null && cacheXMethodHolder.getInnerReturnType() == null) {
            cacheXMethodHolder.setInnerReturnType(invokeResult.getClass());
        }

        if (!needWrite) {
            return invokeResult;
        }

        if (invokeResult != null) {
            cacheManager.writeSingle(cacheXAnnoHolder.getCache(), key, invokeResult, cacheXAnnoHolder.getExpire());
            return invokeResult;
        }

        // invokeResult is null
        if (config.isPreventOn()) {
            cacheManager.writeSingle(cacheXAnnoHolder.getCache(), key, PreventObjects.getPreventObject(), cacheXAnnoHolder.getExpire());
        }

        return null;
    }

    private void doRecord(Object result, String key, CacheAnnoHolder cacheXAnnoHolder) {
        CacheLogger.info("single cache hit rate: {}/1, key: {}", result == null ? 0 : 1, key);
        if (this.shootingMXBean != null) {
            String pattern = PatternGenerator.generatePattern(cacheXAnnoHolder);

            if (result != null) {
                this.shootingMXBean.hitIncr(pattern, 1);
            }
            this.shootingMXBean.reqIncr(pattern, 1);
        }
    }
}
