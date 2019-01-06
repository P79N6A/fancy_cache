package pers.fancy.tools.trace.tasks;

import pers.fancy.tools.job.JobTask;
import pers.fancy.tools.trace.TraceJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static pers.fancy.tools.utils.JboxUtils.getAbstractMethod;
import static pers.fancy.tools.utils.JboxUtils.getSimplifiedMethodName;


public class LogRootErrorTask implements JobTask<TraceJobContext> {

    private static final long serialVersionUID = -9004722041244327806L;

    private static final Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @Override
    public void invoke(TraceJobContext context) throws Throwable {
        try {
            context.next();
        } catch (Throwable t) {
            rootLogger.error("method: [{}] invoke failed", getSimplifiedMethodName(getAbstractMethod(context.getJoinPoint())), t);
            throw t;
        }
    }
}
