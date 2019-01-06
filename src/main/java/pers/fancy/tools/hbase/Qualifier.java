package pers.fancy.tools.hbase;

import java.lang.annotation.*;


@Documented
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {

    String value() default "";

    String family() default "";

    boolean exclude() default false;
}
