package pers.fancy.cache;

import java.util.Collection;
import java.util.Map;


/**
 * 缓存落地存储接口定义
 * @author
 */
public interface ICache {

    /**
     * 读取缓存中对应key的对象
     * @param key----缓存对应的key
     * @return
     */
    Object read(String key);

    /**
     * 向指定存储中写入缓存
     * @param key----对应的key
     * @param value----缓存对象
     * @param expire----过期时间
     */
    void write(String key, Object value, long expire);

    /**
     * 读取多个缓存对象
     * @param keys
     * @return
     */
    Map<String, Object> read(Collection<String> keys);

    /**
     * 写入多个缓存
     * @param keyValueMap
     * @param expire
     */
    void write(Map<String, Object> keyValueMap, long expire);

    /**
     * 删除指定缓存
     * @param keys
     */
    void remove(String... keys);
}