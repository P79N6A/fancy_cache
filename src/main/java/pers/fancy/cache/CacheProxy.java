package pers.fancy.cache;

import pers.fancy.cache.core.CacheConfig;
import pers.fancy.cache.core.CacheCore;
import pers.fancy.cache.core.CacheModule;
import pers.fancy.cache.invoker.adapter.InvocationInvokerAdapter;
import org.apache.commons.proxy.Interceptor;
import org.apache.commons.proxy.Invocation;
import org.apache.commons.proxy.ProxyFactory;
import org.apache.commons.proxy.factory.cglib.CglibProxyFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Method;
import java.util.Map;


/**
 * https://www.jianshu.com/p/05c909c9beb0
 *
 * 在 Spring 中，BeanFactory是 IoC 容器的核心接口。它的职责包括：
 * 实例化、定位、配置应用程序中的对象及建立这些对象间的依赖。
 *
 * BeanFactory 提供的高级配置机制，使得管理任何性质的对象成为可能。
 * ApplicationContext 是 BeanFactory 的扩展，功能得到了进一步增强，比如更易与 Spring AOP 集成、消息资源处理(国际化处理)、事件传递及各种不同应用层的 context 实现(如针对 web 应用的WebApplicationContext)。
 *
 *
 * Spring 中为我们提供了两种类型的 bean，一种就是普通的 bean，我们通过 getBean(id) 方法获得是该 bean 的实际类型，另外还有一种 bean 是 FactoryBean，也就是工厂 bean，我们通过 getBean(id) 获得是该工厂所产生的 Bean 的实例，而不是该 FactoryBean 的实例。
 *
 *
 * 不过这原理说出来也好简单，所有FactoryBean 实现FactoryBean接口的getObject()函数。
 * Spring容器getBean(id)时见到bean的定义是普通class时，就会构造该class的实例来获得bean，
 * 而如果发现是FacotryBean接口的实例时，就通过调用它的getObject()函数来获得bean，
 * 仅此而以.......可见，很重要的思想，可以用很简单的设计来实现
 *
 * 以Bean结尾，表示它是一个Bean，不同于普通Bean的是：它是实现了FactoryBean<T>接口的Bean，
 * 根据该Bean的ID从BeanFactory中获取的实际上是FactoryBean的getObject()返回的对象，而不是FactoryBean本身，
 * 如果要获取FactoryBean对象，请在id前面加一个&符号来获取。
 *
 * @author Administrator
 */
public class CacheProxy<T> implements FactoryBean<T> {

    private Object target;

    private Object proxy;

    private Class<T> type;

    private CacheConfig.Switch cglib;

    private CacheCore cacheXCore;

    public CacheProxy(Object target, Map<String, ICache> caches) {
        this(target, (Class<T>) target.getClass().getInterfaces()[0], caches, CacheConfig.Switch.OFF);
    }

    public CacheProxy(Object target, Class<T> type, Map<String, ICache> caches, CacheConfig.Switch cglib) {
        this.target = target;
        this.type = type;
        this.cglib = cglib;
        this.proxy = newProxy();
        this.cacheXCore = CacheModule.coreInstance(CacheConfig.newConfig(caches));
    }

    private Object newProxy() {
        ProxyFactory factory;
        if (cglib == CacheConfig.Switch.ON || !this.type.isInterface()) {
            factory = new CglibProxyFactory();
        } else {
            factory = new ProxyFactory();
        }

        return factory.createInterceptorProxy(target, interceptor, new Class[]{type});
    }

    private Interceptor interceptor = new Interceptor() {

        @Override
        public Object intercept(Invocation invocation) throws Throwable {

            Method method = invocation.getMethod();
            Cached cached;
            if ((cached = method.getAnnotation(Cached.class)) != null) {
                return cacheXCore.readWrite(cached, method, new InvocationInvokerAdapter(target, invocation));
            }

            CachedGet cachedGet;
            if ((cachedGet = method.getAnnotation(CachedGet.class)) != null) {
                return cacheXCore.read(cachedGet, method, new InvocationInvokerAdapter(target, invocation));
            }

            Invalid invalid;
            if ((invalid = method.getAnnotation(Invalid.class)) != null) {
                cacheXCore.remove(invalid, method, invocation.getArguments());
                return null;
            }

            return invocation.proceed();
        }
    };

    @Override
    public T getObject() {
        return (T) proxy;
    }

    @Override
    public Class<T> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}