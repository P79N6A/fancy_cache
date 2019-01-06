package pers.fancy.tools.helpers;


/**
 * @author fancy
 */
@FunctionalInterface
public interface ExceptionableSupplier<T> {

    /**
     * tt
     * @return
     * @throws Exception
     */
    T get() throws Exception;
}
