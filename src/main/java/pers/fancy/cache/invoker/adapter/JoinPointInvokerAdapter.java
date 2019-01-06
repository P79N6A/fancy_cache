package pers.fancy.cache.invoker.adapter;

import pers.fancy.cache.invoker.Invoker;
import org.aspectj.lang.ProceedingJoinPoint;


/**
 * @author fancy
 */
public class JoinPointInvokerAdapter implements Invoker {

    private ProceedingJoinPoint proceedingJoinPoint;

    public JoinPointInvokerAdapter(ProceedingJoinPoint proceedingJoinPoint) {
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    @Override
    public Object[] getArgs() {
        return proceedingJoinPoint.getArgs();
    }

    @Override
    public Object proceed() throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        return proceedingJoinPoint.proceed(args);
    }
}
