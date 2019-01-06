package pers.fancy.tools.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public abstract class AbstractApplicationContextAware implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AbstractApplicationContextAware.applicationContext = applicationContext;
        SpringLoggerHelper.info("{}'s applicationContext been autowired.", this.getClass().getName());
    }
}
