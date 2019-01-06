package pers.fancy.cache;

import java.lang.annotation.*;


@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheKey {

    /**
     * @return use a part of param as a cache key part
     */
    String value() default "";

    /**
     * @return used multi model(value has `#i` index) and method return {@code Collection},
     * the {@code field} indicate which of the {@code Collection}'s entity field related with this param
     */
    String field() default "";
}
