package pers.fancy.tools.trace.tlog;

import pers.fancy.tools.utils.IPv4;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class LogEvent {

    /**
     * obtain from context
     */
    private String serverIp;

    private String traceId;

    private String clientName;

    private String clientIp;

    private String invokeThread;

    /**
     * need generate from TraceLauncher invoke
     */
    private long startTime;

    private String className;

    private String methodName;

    private Object[] args;

    private long rt;

    private Object result;

    private Throwable exception;

    public void init() {
        this.serverIp = IPv4.getLocalIp();
        /*提供开放能力插件化注入
        this.traceId = EagleEye.getTraceId();
        this.clientName = RequestCtxUtil.getAppNameOfClient();
        this.clientIp = RequestCtxUtil.getClientIp();
        */
        this.invokeThread = Thread.currentThread().getName();
    }

    /**
     * don't need record
     */
    String configKey;

    Method method;
}
