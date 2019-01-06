package pers.fancy.tools.hbase;

import java.lang.annotation.*;


@Documented
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value();
}
