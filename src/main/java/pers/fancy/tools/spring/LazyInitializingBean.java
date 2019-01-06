package pers.fancy.tools.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public interface LazyInitializingBean extends ApplicationListener<ContextRefreshedEvent> {

    @Override
    default void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            afterApplicationInitiated(event.getApplicationContext());
        }
    }

    void afterApplicationInitiated(ApplicationContext applicationContext);
}
