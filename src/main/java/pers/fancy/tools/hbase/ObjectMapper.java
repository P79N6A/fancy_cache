package pers.fancy.tools.hbase;

import java.util.Map;


@FunctionalInterface
public interface ObjectMapper<T> {

    T mapObject(String rowKey, Map<String/*family*/, Map<String/*qualifier*/, Object>> columnMap) throws Exception;
}
