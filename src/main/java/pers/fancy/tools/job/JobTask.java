package pers.fancy.tools.job;

import java.io.Serializable;


public interface JobTask<T extends JobContext> extends Serializable {

    void invoke(T context) throws Throwable;

    default String desc() {
        return this.getClass().getName();
    }
}
