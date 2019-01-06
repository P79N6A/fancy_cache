package pers.fancy.cache.shooting;

import pers.fancy.cache.ShootingMXBean;
import pers.fancy.cache.support.shooting.DerbyShootingMXBeanImpl;
import pers.fancy.cache.support.shooting.H2ShootingMXBeanImpl;
import org.junit.Test;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * ManagementFactory是一个为我们提供各种获取JVM信息的工厂类，使用ManagementFactory可以获取大量的运行时JVM信息，
 * 比如JVM堆的使用情况，
 * 以及GC情况，线程信息等，通过这些数据项我们可以了解正在运行的JVM的情况，以便我们可以做出相应的调整。
 */
public class MxBeanTest {

    @Test
    public void testDerby() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        ShootingMXBean mxBean = new DerbyShootingMXBeanImpl();

        ManagementFactory.getPlatformMBeanServer().registerMBean(mxBean, new ObjectName("pers.fancy.cache:name=hit"));
        mxBean.hitIncr("nihao", 1);
        mxBean.reqIncr("nihao", 2);

//        Thread.sleep(1000000);
        //mxBean.reset("nihao");

        //mxBean.reqIncr("testDerby", 88);
        //mxBean.resetAll();
    }

    //@Test
    public void testH2() throws InterruptedException, MalformedObjectNameException, NotCompliantMBeanException,
            InstanceAlreadyExistsException, MBeanRegistrationException {
        ShootingMXBean mxBean = new H2ShootingMXBeanImpl();

        ManagementFactory.getPlatformMBeanServer().registerMBean(mxBean, new ObjectName("pers.fancy.cache:name=shooting"));
        mxBean.hitIncr("nihao", 1);
        mxBean.reqIncr("nihao", 2);

//        Thread.sleep(1000000);
    }
}
