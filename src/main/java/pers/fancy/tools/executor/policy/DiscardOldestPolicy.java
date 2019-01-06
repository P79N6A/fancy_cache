package pers.fancy.tools.executor.policy;

import pers.fancy.tools.executor.AsyncRunnable;
import pers.fancy.tools.executor.ExecutorLoggerInner;

import java.util.Objects;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


public class DiscardOldestPolicy extends ThreadPoolExecutor.DiscardOldestPolicy implements ExecutorLoggerInner {

    public static final RejectedExecutionHandler instance = new DiscardOldestPolicy(DEFAULT_GROUP);

    private String group;

    public DiscardOldestPolicy(String group) {
        this.group = group;
    }

    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            // 将队首元素扔掉
            Runnable discardRunnable = executor.getQueue().poll();
            if (discardRunnable instanceof AsyncRunnable) {
                AsyncRunnable asyncRunnable = (AsyncRunnable)discardRunnable;

                String message = generatePolicyLoggerContent(group, this, executor.getQueue(), asyncRunnable.taskInfo(),
                    Objects.hashCode(asyncRunnable));

                monitorLogger.warn(message);
                executorLogger.warn(message);
            }

            // 执行新添加元素
            executor.execute(runnable);
        }
    }
}
