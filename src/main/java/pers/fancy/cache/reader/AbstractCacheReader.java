package pers.fancy.cache.reader;

import pers.fancy.cache.domain.CacheAnnoHolder;
import pers.fancy.cache.domain.CacheMethodHolder;
import pers.fancy.cache.invoker.Invoker;
import pers.fancy.cache.utils.CacheLogger;

/**
 * 什么是函数式接口
 * 所谓的函数式接口，当然首先是一个接口，然后就是在这个接口里面只能有一个抽象方法。
 * 这种类型的接口也称为SAM接口，即Single Abstract Method interfaces
 * 特点
 *
 * 接口有且仅有一个抽象方法
 * 允许定义静态方法
 * 允许定义默认方法
 * 允许java.lang.Object中的public方法
 * 该注解不是必须的，如果一个接口符合"函数式接口"定义，那么加不加该注解都没有影响。
 * 加上该注解能够更好地让编译器进行检查。如果编写的不是函数式接口，但是加上了@FunctionInterface，那么编译器会报错
 *
 * @author Administrator
 */
public abstract class AbstractCacheReader {

    /**
     * tt
     * @param cacheXAnnoHolder
     * @param cacheXMethodHolder
     * @param invoker
     * @param needWrite
     * @return
     * @throws Throwable
     */
    public abstract Object read(CacheAnnoHolder cacheXAnnoHolder, CacheMethodHolder cacheXMethodHolder, Invoker invoker, boolean needWrite) throws Throwable;

    Object doLogInvoke(ThrowableSupplier<Object> throwableSupplier) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return throwableSupplier.get();
        } finally {
            CacheLogger.debug("method invoke total cost [{}] ms", (System.currentTimeMillis() - start));
        }
    }

    @FunctionalInterface
    protected interface ThrowableSupplier<T> {
        T get() throws Throwable;
    }
}
