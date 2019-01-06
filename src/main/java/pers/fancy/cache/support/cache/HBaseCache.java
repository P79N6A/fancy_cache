package pers.fancy.cache.support.cache;

import pers.fancy.cache.ICache;
import pers.fancy.tools.hbase.HBaseBatis;
import pers.fancy.tools.hbase.HBaseMode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * HBase是一个分布式的、面向列的开源数据库
 * @author fancy
 */
public class HBaseCache<T extends HBaseMode> implements ICache {

    private HBaseBatis<T> hBaseBatis;

    public HBaseCache(HBaseBatis<T> hBaseBatis) {
        this.hBaseBatis = hBaseBatis;
    }

    @Override
    public Object read(String key) {
        return hBaseBatis.get(key);
    }

    @Override
    public void write(String key, Object value, long expire) {
        T model = (T) value;
        model.setRowKey(key);
        hBaseBatis.put(model);
    }

    @Override
    public Map<String, Object> read(Collection<String> keys) {
        List<T> results = hBaseBatis.gets(new ArrayList<>(keys));
        return results.stream().collect(Collectors.toMap(T::getRowKey, Function.identity()));
    }

    @Override
    public void write(Map<String, Object> keyValueMap, long expire) {
        List<T> models = keyValueMap.entrySet().stream().map(entry -> {
            T model = (T) entry.getValue();
            model.setRowKey(entry.getKey());

            return model;
        }).collect(Collectors.toList());

        hBaseBatis.puts(models);
    }

    @Override
    public void remove(String... keys) {
        hBaseBatis.deletes(Arrays.asList(keys));
    }
}
