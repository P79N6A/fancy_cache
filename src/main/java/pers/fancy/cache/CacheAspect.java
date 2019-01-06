package pers.fancy.cache;

import pers.fancy.cache.core.CacheConfig;
import pers.fancy.cache.core.CacheCore;
import pers.fancy.cache.core.CacheModule;
import pers.fancy.cache.invoker.adapter.JoinPointInvokerAdapter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * 缓存启用入口
 * @author fancy
 */
@Aspect
public class CacheAspect {

    private CacheCore core;

    public CacheAspect(Map<String, ICache> caches) {
        this(CacheConfig.newConfig(caches));
    }

    public CacheAspect(CacheConfig config) {
        core = CacheModule.coreInstance(config);
    }

    @Around("@annotation(pers.fancy.cache.CachedGet)")
    public Object read(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        CachedGet cachedGet = method.getAnnotation(CachedGet.class);
        return core.read(cachedGet, method, new JoinPointInvokerAdapter(pjp));
    }

    @Around("@annotation(pers.fancy.cache.Cached)")
    public Object readWrite(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        Cached cached = method.getAnnotation(Cached.class);

        return core.readWrite(cached, method, new JoinPointInvokerAdapter(pjp));
    }

    @After("@annotation(pers.fancy.cache.Invalid)")
    public void remove(JoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        Invalid invalid = method.getAnnotation(Invalid.class);
        core.remove(invalid, method, pjp.getArgs());
    }

    private Method getMethod(JoinPoint pjp) throws NoSuchMethodException {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            method = pjp.getTarget().getClass().getDeclaredMethod(ms.getName(), method.getParameterTypes());
        }
        return method;
    }
}
