package pers.fancy.tools.trace;

import pers.fancy.tools.job.JobTask;
import pers.fancy.tools.job.JobContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class TraceJobContext extends JobContext {

    private static final long serialVersionUID = 3545382785877769347L;

    private ProceedingJoinPoint joinPoint;

    private Class<?> clazz;

    private Method method;

    private Object target;

    private Object[] args;

    public TraceJobContext() {
    }

    public TraceJobContext(String jobName, List<JobTask> jobTasks) {
        super(jobName, jobTasks);
    }
}
