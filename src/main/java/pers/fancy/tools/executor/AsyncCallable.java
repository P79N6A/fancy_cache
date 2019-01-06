package pers.fancy.tools.executor;

/**
 * 类似{@code java.util.concurrent.Callable}结构, 在{@code Callable}基础上添加
 * -  {@code taskInfo()}
 * 等方法, 相比于原生的{@code Callable}有以下优势:
 * 1.  在task执行前设置rpc context信息, 以及将traceId塞入MDC;
 * 2.  监控task执行耗时(rt);
 * 3.  监控task执行次数(success、failure), 以及据此计算得出的tps(详见{@code ExecutorMonitor};
 * 4.  监控task执行出错时打印详细log;
 * 5.  当task进入RunnableQueue后/触发{@code java.util.concurrent.RejectedExecutionHandler}时打印更详细的的信息.
 * 6.  持续完善中...
 * <p>
 * (使用了{@code ExecutorManager}后, 即使'submit()'使用的是原生Callable, 也会被封装成一个AsyncCallable)
 *
 */
@FunctionalInterface
public interface AsyncCallable<V> extends java.util.concurrent.Callable<V>, ExecutorLoggerInner {

    /**
     * implements like {@code Callable.call()}
     *
     * @return sub thread invoke result
     * @throws Exception sub thread runtime throws Exception
     */
    V execute(AsyncContext context) throws Exception;

    /**
     * detail info of the task need to invoke.
     *
     * @return default task class name.
     */
    default String taskInfo() {
        return this.getClass().getName();
    }

    /**
     * default implements Callable<V>, you should not change this implementation !!!
     *
     * @return null
     * @throws Exception
     */
    @Override
    default V call() throws Exception {
        return execute(null);
    }
}
