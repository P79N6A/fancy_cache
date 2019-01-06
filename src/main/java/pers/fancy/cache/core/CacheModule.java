package pers.fancy.cache.core;

import pers.fancy.cache.ICache;
import pers.fancy.cache.ShootingMXBean;
import pers.fancy.cache.reader.AbstractCacheReader;
import pers.fancy.cache.reader.MultiCacheReader;
import pers.fancy.cache.reader.SingleCacheReader;
import pers.fancy.tools.utils.Collections3;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 基于Guice的IOC实现
 * @author fancy
 */
public class CacheModule extends AbstractModule {

    private static final AtomicBoolean init = new AtomicBoolean(false);

    private static Injector injector;

    private CacheConfig config;

    private CacheModule(CacheConfig config) {
        this.config = config;
    }

    /**
     * 所有bean的装配工作都放到这儿
     */
    @Override
    protected void configure() {
        Preconditions.checkArgument(config != null, "config param can not be null.");
        Preconditions.checkArgument(Collections3.isNotEmpty(config.getCaches()), "caches param can not be empty.");

        bind(CacheConfig.class).toInstance(config);

        // bind caches
        MapBinder<String, ICache> mapBinder = MapBinder.newMapBinder(binder(), String.class, ICache.class);
        config.getCaches().forEach((name, cache) -> mapBinder.addBinding(name).toInstance(cache));

        // bind shootingMXBean
        Optional.ofNullable(config.getShootingMXBean())
                .ifPresent(mxBean -> bind(ShootingMXBean.class).toInstance(mxBean));

        bind(AbstractCacheReader.class).annotatedWith(Names.named("singleCacheReader")).to(SingleCacheReader.class);
        bind(AbstractCacheReader.class).annotatedWith(Names.named("multiCacheReader")).to(MultiCacheReader.class);
    }

    public synchronized static CacheCore coreInstance(CacheConfig config) {
        if (init.compareAndSet(false, true)) {
            injector = Guice.createInjector(new CacheModule(config));
        }

        return injector.getInstance(CacheCore.class);
    }
}
