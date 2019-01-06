package pers.fancy.tools.executor.policy;

import pers.fancy.tools.executor.AsyncRunnable;
import pers.fancy.tools.executor.ExecutorLoggerInner;

import java.util.Objects;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


public class CallerRunsPolicy extends ThreadPoolExecutor.CallerRunsPolicy implements ExecutorLoggerInner {

    public static final RejectedExecutionHandler instance = new CallerRunsPolicy(DEFAULT_GROUP);

    private String group;

    public CallerRunsPolicy(String group) {
        this.group = group;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {

        if (runnable instanceof AsyncRunnable) {
            AsyncRunnable asyncRunnable = (AsyncRunnable) runnable;
            String message = generatePolicyLoggerContent(group, this, executor.getQueue(), asyncRunnable.taskInfo(),
                    Objects.hashCode(asyncRunnable));

            monitorLogger.warn(message);
            executorLogger.warn(message);
        }

        super.rejectedExecution(runnable, executor);
    }
}
