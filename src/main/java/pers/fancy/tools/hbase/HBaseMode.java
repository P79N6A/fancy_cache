package pers.fancy.tools.hbase;

import java.io.Serializable;


public interface HBaseMode extends Serializable {

    String getRowKey();

    void setRowKey(String rowKey);
}
