package pers.fancy.tools.trace;


public class TraceException extends RuntimeException {

    public TraceException() {
        super();
    }

    public TraceException(String message) {
        super(message);
    }

    public TraceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TraceException(Throwable cause) {
        super(cause);
    }

    protected TraceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
