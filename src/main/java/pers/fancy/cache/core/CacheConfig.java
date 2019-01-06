package pers.fancy.cache.core;

import pers.fancy.cache.ICache;
import pers.fancy.cache.ShootingMXBean;
import lombok.Data;

import java.util.Map;


/**
 * 缓存配置
 * @author
 */
@Data
public class CacheConfig {

    /**
     * ICache接口实现
     */
    private Map<String, ICache> caches;

    /**
     * 缓存分组命中率统计
     */
    private ShootingMXBean shootingMXBean;

    /**
     * 是否开启Cache(全局开关)
     */
    private Switch swc;

    /**
     * 是否开启缓存防击穿
     */
    private Switch prevent;

    public Switch getSwc(){
        return swc;
    }
    public boolean isPreventOn() {
        return prevent != null && prevent == Switch.ON;
    }

    public static CacheConfig newConfig(Map<String, ICache> caches) {
        CacheConfig config = new CacheConfig();
        config.caches = caches;
        config.swc = Switch.ON;
        config.prevent = Switch.OFF;
        config.shootingMXBean = null;

        return config;
    }




    public enum Switch {
        ON,
        OFF
    }
}
