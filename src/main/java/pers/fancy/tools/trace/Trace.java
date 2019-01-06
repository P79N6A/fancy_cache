package pers.fancy.tools.trace;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Trace {

    /**
     * determine the 'method invoke cost time' logger(type {@code org.slf4j.Logger}) used.
     */
    String value() default "";

    /**
     * method invoke total cost threshold, dependent logger config.
     * if ${method invoke cost time} > ${threshold} then append an 'cost time' log.
     */
    long threshold() default 0;
}
