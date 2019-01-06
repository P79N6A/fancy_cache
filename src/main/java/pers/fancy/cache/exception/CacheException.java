package pers.fancy.cache.exception;


/**
 * @author fancy
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = 6582898095150572577L;

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
