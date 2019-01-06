package pers.fancy.cache.invoker;


/**
 * @author fancy
 */
public interface Invoker {

    /**
     * tt
     * @return
     */
    Object[] getArgs();

    /**
     * tt
     * @return
     * @throws Throwable
     */
    Object proceed() throws Throwable;

    /**
     * tt
     * @param args
     * @return
     * @throws Throwable
     */
    Object proceed(Object[] args) throws Throwable;
}
