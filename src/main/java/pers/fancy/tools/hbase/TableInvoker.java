package pers.fancy.tools.hbase;

import org.apache.hadoop.hbase.client.HTableInterface;


@FunctionalInterface
public interface TableInvoker<T> {

    T invoke(HTableInterface table) throws Exception;
}