package pers.fancy.cache.utils;

import pers.fancy.cache.Cached;
import pers.fancy.cache.CachedGet;
import pers.fancy.cache.Invalid;
import pers.fancy.cache.core.CacheConfig;
import pers.fancy.cache.enums.Expiration;

import java.lang.reflect.Method;


/**
 * @author fancy
 */
public class SwitcherUtil {

    public static boolean isSwitchOn(CacheConfig config, Cached cached, Method method, Object[] args) {
        return doIsSwitchOn(config.getSwc() == CacheConfig.Switch.ON,
                cached.expire(), cached.condition(),
                method, args);
    }


    public static boolean isSwitchOn(CacheConfig config, Invalid invalid, Method method, Object[] args) {
        return doIsSwitchOn(config.getSwc() == CacheConfig.Switch.ON,
                Expiration.FOREVER, invalid.condition(),
                method, args);
    }

    public static boolean isSwitchOn(CacheConfig config, CachedGet cachedGet, Method method, Object[] args) {
        return doIsSwitchOn(config.getSwc() == CacheConfig.Switch.ON,
                Expiration.FOREVER, cachedGet.condition(),
                method, args);
    }

    private static boolean doIsSwitchOn(boolean openStat,
                                        int expire,
                                        String condition, Method method, Object[] args) {
        if (!openStat) {
            return false;
        }

        if (expire == Expiration.NO) {
            return false;
        }

        return (boolean) SpelCalculator.calcSpelValueWithContext(condition, ArgNameGenerator.getArgNames(method), args, true);
    }
}
