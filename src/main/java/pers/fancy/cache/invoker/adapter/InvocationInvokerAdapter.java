package pers.fancy.cache.invoker.adapter;

import pers.fancy.cache.invoker.Invoker;
import org.apache.commons.proxy.Invocation;


/**
 * @author fancy
 */
public class InvocationInvokerAdapter implements Invoker {

    private Object target;

    private Invocation invocation;

    public InvocationInvokerAdapter(Object target, Invocation invocation) {
        this.target = target;
        this.invocation = invocation;
    }

    @Override
    public Object[] getArgs() {
        return invocation.getArguments();
    }

    @Override
    public Object proceed() throws Throwable {
        return invocation.proceed();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        return invocation.getMethod().invoke(target, args);
    }
}
