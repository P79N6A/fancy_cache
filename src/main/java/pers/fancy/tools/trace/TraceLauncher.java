package pers.fancy.tools.trace;

import pers.fancy.tools.job.JobTask;
import pers.fancy.tools.trace.tasks.MethodInvokeTask;
import pers.fancy.tools.utils.JboxUtils;
import com.google.common.base.Preconditions;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@Aspect
public class TraceLauncher implements Serializable {

    private static final long serialVersionUID = 1383288704716921329L;

    private List<JobTask> tasks;

    @Setter
    private boolean useAbstractMethod = false;

    public void setTasks(List<JobTask> tasks) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(tasks));

        boolean isFoundMethodInvoker = false;
        for (int i = 0; i < tasks.size(); ++i) {
            JobTask task = tasks.get(i);
            Preconditions.checkNotNull(task, "task[" + i + "] is null.");

            if (task instanceof MethodInvokeTask) {
                isFoundMethodInvoker = true;
            }
        }

        if (!isFoundMethodInvoker) {
            tasks.add(new MethodInvokeTask());
        }
        
        this.tasks = new ArrayList<>(tasks);
    }

    @Around("@annotation(pers.fancy.tools.trace.Trace)")
    public Object emit(final ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = useAbstractMethod ? JboxUtils.getAbstractMethod(joinPoint) : JboxUtils.getImplMethod(joinPoint);
        Class<?> clazz = method.getDeclaringClass();
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();

        TraceJobContext context = newContext(joinPoint, clazz, method, target, args);
        context.next();
        return context.getResult();
    }

    private TraceJobContext newContext(ProceedingJoinPoint joinPoint,
                                       Class<?> clazz, Method method,
                                       Object target, Object[] args) {

        TraceJobContext context = new TraceJobContext("TraceJob", tasks);

        context.setJoinPoint(joinPoint);
        context.setClazz(clazz);
        context.setMethod(method);
        context.setTarget(target);
        context.setArgs(args);

        return context;
    }
}
