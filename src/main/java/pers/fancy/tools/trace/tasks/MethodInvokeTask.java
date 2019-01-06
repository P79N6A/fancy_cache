package pers.fancy.tools.trace.tasks;

import pers.fancy.tools.job.JobTask;
import pers.fancy.tools.trace.TraceJobContext;
import lombok.Data;

/**
 * 方法的最终执行器, 可以覆盖该类并配置自己的执行器.
 *
 */
@Data
public class MethodInvokeTask implements JobTask<TraceJobContext> {

    private static final long serialVersionUID = 2587891236640265365L;

    @Override
    public void invoke(TraceJobContext context) throws Throwable {
        context.successOf(context.getJoinPoint().proceed(context.getArgs()));
    }
}
