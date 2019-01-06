package pers.fancy.tools.executor;

import lombok.Data;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;


@Data
public class AsyncContext {

    private Thread parent;

    private String group;

    private Map<String, String> mdcContext;

    private Map<String, Object> extContext;

    public static AsyncContext newContext(String group) {
        AsyncContext context = new AsyncContext();
        context.setGroup(group);
        context.setParent(Thread.currentThread());
        context.setMdcContext(MDC.getCopyOfContextMap());

        return context;
    }

    public void putExtContext(String key, Object value) {
        if (extContext == null) {
            extContext = new HashMap<>();
        }
        extContext.put(key, value);
    }

    public Map<String, Object> getExtContext() {
        return extContext == null ? extContext = new HashMap<>() : extContext;
    }

    public Map<String, String> getMdcContext() {
        return mdcContext;
    }
}
