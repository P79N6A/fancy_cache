package pers.fancy.cache.cases;

import pers.fancy.cache.Utils;
import pers.fancy.cache.cases.base.TestBase;
import pers.fancy.cache.exception.CacheException;
import pers.fancy.cache.service.impl.InnerMapService;
import com.google.common.collect.Lists;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;


public class InnerMapTest extends TestBase {

    @Resource
    private InnerMapService service;

    @Test(expected = CacheException.class)
    public void unmodifiableMap() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);

        service.unmodifiableMap(list);
        service.unmodifiableMap(list);
        list.add(Utils.nextRadom());
        service.unmodifiableMap(list);
    }

    @Test(expected = CacheException.class)
    public void synchronizedMap() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);

        service.synchronizedMap(list);
        service.synchronizedMap(list);
        list.add(Utils.nextRadom());
        service.synchronizedMap(list);
    }

    @Test(expected = CacheException.class)
    public void checkedMap() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);

        service.checkedMap(list);
        service.checkedMap(list);
        list.add(Utils.nextRadom());
        service.checkedMap(list);
    }


    @Test(expected = CacheException.class)
    public void immutableMap() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);

        service.immutableMap(list);
        service.immutableMap(list);
        list.add(Utils.nextRadom());
        service.immutableMap(list);
    }
}
