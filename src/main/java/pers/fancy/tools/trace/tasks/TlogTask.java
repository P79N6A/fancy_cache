package pers.fancy.tools.trace.tasks;

import pers.fancy.tools.job.JobTask;
import pers.fancy.tools.trace.TraceJobContext;
import pers.fancy.tools.trace.tlog.LogEvent;
import pers.fancy.tools.trace.tlog.TlogManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


@Data
@Slf4j
public class TlogTask implements JobTask<TraceJobContext> {

    private static final long serialVersionUID = -3619644271378328443L;

    private List<TlogManager> tlogManagers = new CopyOnWriteArrayList<>();

    private String configKeyPattern = "%s:%s";

    @Override
    public void invoke(TraceJobContext context) throws Throwable {
        LogEvent logEvent = new LogEvent();
        try {
            logEvent.setMethod(context.getMethod());
            logEvent.setClassName(context.getClazz().getName());
            logEvent.setMethodName(context.getMethod().getName());
            logEvent.setConfigKey(getConfigKey(context));
            logEvent.setArgs(context.getArgs());

            logEvent.setStartTime(System.currentTimeMillis());
            context.next();
            logEvent.setRt(System.currentTimeMillis() - logEvent.getStartTime());

            logEvent.setResult(context.getResult());
        } catch (Throwable t) {
            logEvent.setException(t);
            throw t;
        } finally {
            sendLogEvent(logEvent);
        }
    }

    private AtomicBoolean isFirst = new AtomicBoolean(false);

    private void sendLogEvent(LogEvent event) {
        if (CollectionUtils.isEmpty(tlogManagers)) {
            if (isFirst.compareAndSet(false, true)) {
                log.warn("tlogManagers is empty.");
            }

            return;
        }

        event.init();
        for (TlogManager tlogManager : tlogManagers) {
            tlogManager.postLogEvent(event);
        }
    }

    private String getConfigKey(TraceJobContext context) {
        String className = context.getClazz().getName();
        String methodName = context.getMethod().getName();
        return String.format(configKeyPattern, className, methodName);
    }
}
