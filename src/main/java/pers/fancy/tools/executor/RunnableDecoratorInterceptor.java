package pers.fancy.tools.executor;

import pers.fancy.tools.helpers.ExceptionableSupplier;
import com.google.common.collect.Sets;
import lombok.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import static pers.fancy.tools.utils.JboxUtils.runWithNewMdcContext;


class RunnableDecoratorInterceptor implements InvocationHandler {

    private static final Set<String> NEED_PROXY_METHODS = Sets.newConcurrentHashSet(Arrays.asList(
            "execute",
            "submit",
            "schedule",
            "invokeAll",/*todo*/
            "scheduleAtFixedRate",
            "scheduleWithFixedDelay"
    ));

    private String group;

    private ExecutorService target;

    RunnableDecoratorInterceptor(String group, ExecutorService target) {
        this.group = group;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (NEED_PROXY_METHODS.contains(methodName)) {
            Object firstArg = args[0];
            ExecutorManager.FlightRecorder recorder = ExecutorManager.recorders.computeIfAbsent(group, (k) -> new ExecutorManager.FlightRecorder());
            AsyncContext context = AsyncContext.newContext(group);
            if (firstArg instanceof AsyncRunnable) {
                args[0] = new AsyncRunnableDecorator(context, recorder, (AsyncRunnable) firstArg);
            } else if (firstArg instanceof AsyncCallable) {
                args[0] = new AsyncCallableDecorator(context, recorder, (AsyncCallable) firstArg);
            } else if (firstArg instanceof Runnable) {
                args[0] = new RunnableDecorator(context, recorder, (Runnable) firstArg);
            } else if (firstArg instanceof Callable) {
                args[0] = new CallableDecorator(context, recorder, (Callable) firstArg);
            }
        }

        return method.invoke(target, args);
    }
}

class RunnableDecorator implements AsyncRunnable {

    private AsyncContext context;

    private ExecutorManager.FlightRecorder recorder;

    private Runnable runnable;

    RunnableDecorator(@NonNull AsyncContext context,
                      @NonNull ExecutorManager.FlightRecorder recorder,
                      @NonNull Runnable runnable) {
        this.context = context;
        this.recorder = recorder;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runWithNewMdcContext((Supplier<Object>) () -> {
            try {
                long start = System.currentTimeMillis();

                runnable.run();

                recorder.getTotalRt().addAndGet((System.currentTimeMillis() - start));
                recorder.getSuccess().incrementAndGet();
            } catch (Throwable e) {
                recorder.getFailure().incrementAndGet();
                monitorLogger.error("task: '{}' exec error, thread: [{}].", taskInfo(), Thread.currentThread().getName(), e);
                throw e;
            }

            return null;
        }, context.getMdcContext());
    }

    @Override
    public void execute(AsyncContext context) {
    }

    @Override
    public String taskInfo() {
        return runnable.getClass().getName();
    }
}

class CallableDecorator implements AsyncCallable {

    private AsyncContext context;

    private ExecutorManager.FlightRecorder recorder;

    private Callable callable;

    CallableDecorator(@NonNull AsyncContext context,
                      @NonNull ExecutorManager.FlightRecorder recorder,
                      @NonNull Callable callable) {
        this.context = context;
        this.recorder = recorder;
        this.callable = callable;
    }

    @Override
    public Object call() throws Exception {
        return runWithNewMdcContext((ExceptionableSupplier<Object>) () -> {
            try {
                long start = System.currentTimeMillis();

                Object result = callable.call();

                recorder.getTotalRt().addAndGet((System.currentTimeMillis() - start));
                recorder.getSuccess().incrementAndGet();

                return result;
            } catch (Throwable e) {
                recorder.getFailure().incrementAndGet();
                monitorLogger.error("task: '{}' exec error, thread: [{}].", taskInfo(), Thread.currentThread().getName(), e);
                throw e;
            }
        }, context.getMdcContext());
    }

    @Override
    public String taskInfo() {
        return callable.getClass().getName();
    }

    @Override
    public Object execute(AsyncContext context) {
        return null;
    }
}

class AsyncRunnableDecorator implements AsyncRunnable {

    private AsyncContext context;

    private ExecutorManager.FlightRecorder recorder;

    private AsyncRunnable asyncRunnable;

    AsyncRunnableDecorator(@NonNull AsyncContext context,
                           @NonNull ExecutorManager.FlightRecorder recorder,
                           @NonNull AsyncRunnable asyncRunnable) {
        this.context = context;
        this.recorder = recorder;
        this.asyncRunnable = asyncRunnable;
    }

    @Override
    public void run() {
        runWithNewMdcContext((Supplier<Object>) () -> {
            try {
                long start = System.currentTimeMillis();

                asyncRunnable.execute(context);

                recorder.getTotalRt().addAndGet((System.currentTimeMillis() - start));
                recorder.getSuccess().incrementAndGet();
            } catch (Throwable e) {
                recorder.getFailure().incrementAndGet();
                monitorLogger.error("task: '{}' exec error, thread: [{}].", taskInfo(), Thread.currentThread().getName(), e);
                throw e;
            }

            return null;
        }, context.getMdcContext());
    }

    @Override
    public String taskInfo() {
        return asyncRunnable.taskInfo();
    }

    @Override
    public void execute(AsyncContext context) {
    }
}

/**
 * 包装成为AsyncXX, 方便ExecutorMonitor调用.
 */
class AsyncCallableDecorator implements AsyncCallable {

    private AsyncContext context;

    private ExecutorManager.FlightRecorder recorder;

    private AsyncCallable asyncCallable;


    AsyncCallableDecorator(@NonNull AsyncContext context,
                           @NonNull ExecutorManager.FlightRecorder recorder,
                           @NonNull AsyncCallable asyncCallable) {
        this.context = context;
        this.recorder = recorder;
        this.asyncCallable = asyncCallable;
    }

    @Override
    public Object call() throws Exception {
        return runWithNewMdcContext((ExceptionableSupplier<Object>) () -> {
            try {
                long start = System.currentTimeMillis();

                Object result = asyncCallable.execute(context);

                recorder.getTotalRt().addAndGet((System.currentTimeMillis() - start));
                recorder.getSuccess().incrementAndGet();

                return result;
            } catch (Throwable e) {
                recorder.getFailure().incrementAndGet();
                monitorLogger.error("task: '{}' exec error, thread: [{}].", taskInfo(), Thread.currentThread().getName(), e);
                throw e;
            }
        }, context.getMdcContext());
    }

    @Override
    public String taskInfo() {
        return asyncCallable.taskInfo();
    }

    @Override
    public Object execute(AsyncContext context) {
        return null;
    }

}