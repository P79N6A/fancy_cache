package pers.fancy.cache.domain;


/**
 *
 * @author fancy
 */
public class CacheMethodHolder {

    private Class<?> innerReturnType;

    private Class<?> returnType;

    private boolean collection;

    public CacheMethodHolder(boolean collection) {
        this.collection = collection;
    }

    public boolean isCollection() {
        return collection;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Class<?> getInnerReturnType() {
        return innerReturnType;
    }

    public void setInnerReturnType(Class<?> innerReturnType) {
        this.innerReturnType = innerReturnType;
    }
}
