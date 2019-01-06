package pers.fancy.cache.utils;

import pers.fancy.cache.CacheKey;
import pers.fancy.cache.Cached;
import pers.fancy.cache.CachedGet;
import pers.fancy.cache.Invalid;
import pers.fancy.cache.domain.CacheAnnoHolder;
import pers.fancy.cache.domain.CacheMethodHolder;
import pers.fancy.cache.domain.Pair;
import pers.fancy.cache.enums.Expiration;
import pers.fancy.cache.exception.CacheException;
import com.google.common.base.Strings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 定位: 将@Cached、@Invalid、@CachedGet、(@CachedPut未来)以及将@CacheKey整体融合到一起
 *
 * @author fancy
 */
public class CacheXInfoContainer {

    private static final ConcurrentMap<Method, Pair<CacheAnnoHolder, CacheMethodHolder>> cacheMap = new ConcurrentHashMap<>();

    public static Pair<CacheAnnoHolder, CacheMethodHolder> getCacheXInfo(Method method) {
        return cacheMap.computeIfAbsent(method, CacheXInfoContainer::doGetMethodInfo);
    }

    private static Pair<CacheAnnoHolder, CacheMethodHolder> doGetMethodInfo(Method method) {
        CacheAnnoHolder cacheXAnnoHolder = getAnnoHolder(method);
        CacheMethodHolder cacheXMethodHolder = getMethodHolder(method, cacheXAnnoHolder);

        return Pair.of(cacheXAnnoHolder, cacheXMethodHolder);
    }

    /****
     * cache key doGetMethodInfo
     ****/

    private static CacheAnnoHolder getAnnoHolder(Method method) {

        CacheAnnoHolder.Builder builder = CacheAnnoHolder.Builder.newBuilder(method);

        Annotation[][] pAnnotations = method.getParameterAnnotations();
        scanKeys(builder, pAnnotations);

        if (method.isAnnotationPresent(Cached.class)) {
            scanCached(builder, method.getAnnotation(Cached.class));
        } else if (method.isAnnotationPresent(CachedGet.class)) {
            scanCachedGet(builder, method.getAnnotation(CachedGet.class));
        } else {
            scanInvalid(builder, method.getAnnotation(Invalid.class));
        }

        return builder.build();
    }

    private static CacheAnnoHolder.Builder scanKeys(CacheAnnoHolder.Builder builder, Annotation[][] pAnnotations) {
        int multiIndex = -1;
        String id = "";
        Map<Integer, CacheKey> cacheKeyMap = new LinkedHashMap<>(pAnnotations.length);

        for (int pIndex = 0; pIndex < pAnnotations.length; ++pIndex) {

            Annotation[] annotations = pAnnotations[pIndex];
            for (Annotation annotation : annotations) {
                if (annotation instanceof CacheKey) {
                    CacheKey cacheKey = (CacheKey) annotation;
                    cacheKeyMap.put(pIndex, cacheKey);
                    if (isMulti(cacheKey)) {
                        multiIndex = pIndex;
                        id = cacheKey.field();
                    }
                }
            }
        }

        return builder
                .setCacheKeyMap(cacheKeyMap)
                .setMultiIndex(multiIndex)
                .setId(id);
    }

    private static CacheAnnoHolder.Builder scanCached(CacheAnnoHolder.Builder builder, Cached cached) {
        return builder
                .setCache(cached.value())
                .setPrefix(cached.prefix())
                .setExpire(cached.expire());
    }

    private static CacheAnnoHolder.Builder scanCachedGet(CacheAnnoHolder.Builder builder, CachedGet cachedGet) {
        return builder
                .setCache(cachedGet.value())
                .setPrefix(cachedGet.prefix())
                .setExpire(Expiration.NO);
    }

    private static CacheAnnoHolder.Builder scanInvalid(CacheAnnoHolder.Builder builder, Invalid invalid) {
        return builder
                .setCache(invalid.value())
                .setPrefix(invalid.prefix())
                .setExpire(Expiration.NO);
    }

    /***
     * cache method doGetMethodInfo
     ***/

    private static CacheMethodHolder getMethodHolder(Method method, CacheAnnoHolder cacheXAnnoHolder) {
        boolean isCollectionReturn = Collection.class.isAssignableFrom(method.getReturnType());
        boolean isMapReturn = Map.class.isAssignableFrom(method.getReturnType());

        staticAnalyze(method.getParameterTypes(),
                cacheXAnnoHolder,
                isCollectionReturn,
                isMapReturn);

        return new CacheMethodHolder(isCollectionReturn);
    }

    private static void staticAnalyze(Class<?>[] pTypes, CacheAnnoHolder cacheXAnnoHolder,
                                      boolean isCollectionReturn, boolean isMapReturn) {
        if (isInvalidParam(pTypes, cacheXAnnoHolder)) {
            throw new CacheException("cache need at least one param key");
        } else if (isInvalidMultiCount(cacheXAnnoHolder.getCacheKeyMap())) {
            throw new CacheException("only one multi key");
        } else {
            Map<Integer, CacheKey> cacheKeyMap = cacheXAnnoHolder.getCacheKeyMap();
            for (Map.Entry<Integer, CacheKey> entry : cacheKeyMap.entrySet()) {
                Integer argIndex = entry.getKey();
                CacheKey cacheKey = entry.getValue();

                if (isMulti(cacheKey) && isInvalidMulti(pTypes[argIndex])) {
                    throw new CacheException("multi need a collection instance param");
                }

                if (isMulti(cacheKey) && isInvalidResult(isCollectionReturn, cacheKey.field())) {
                    throw new CacheException("multi cache && collection method return need a result field");
                }

                if (isInvalidIdentifier(isMapReturn, isCollectionReturn, cacheKey.field())) {
                    throw new CacheException("id method a collection return method");
                }
            }
        }
    }

    private static boolean isMulti(CacheKey cacheKey) {
        if (cacheKey == null) {
            return false;
        }

        String value = cacheKey.value();
        if (Strings.isNullOrEmpty(value)) {
            return false;
        }

        return value.contains("#i");
    }

    private static boolean isInvalidParam(Class<?>[] pTypes, CacheAnnoHolder cacheXAnnoHolder) {
        Map<Integer, CacheKey> cacheKeyMap = cacheXAnnoHolder.getCacheKeyMap();
        String prefix = cacheXAnnoHolder.getPrefix();

        return (pTypes == null
                || pTypes.length == 0
                || cacheKeyMap.isEmpty())
                && Strings.isNullOrEmpty(prefix);
    }

    private static boolean isInvalidMultiCount(Map<Integer, CacheKey> keyMap) {
        int multiCount = 0;
        for (CacheKey cacheKey : keyMap.values()) {
            if (isMulti(cacheKey)) {
                ++multiCount;
                if (multiCount > 1) {
                    break;
                }
            }
        }

        return multiCount > 1;
    }

    private static boolean isInvalidIdentifier(boolean isMapReturn,
                                               boolean isCollectionReturn,
                                               String field) {
        if (isMapReturn && !Strings.isNullOrEmpty(field)) {

            CacheLogger.warn("@CacheKey's 'field = \"{}\"' is useless.", field);

            return false;
        }

        return !Strings.isNullOrEmpty(field) && !isCollectionReturn;
    }

    private static boolean isInvalidResult(boolean isCollectionReturn, String id) {
        return isCollectionReturn && Strings.isNullOrEmpty(id);
    }

    private static boolean isInvalidMulti(Class<?> paramType) {
        return !Collection.class.isAssignableFrom(paramType)
                && !paramType.isArray();
        // 永久不能放开  && !Map.class.isAssignableFrom(paramType);
    }
}
