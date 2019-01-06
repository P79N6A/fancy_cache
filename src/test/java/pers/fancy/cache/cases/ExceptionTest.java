package pers.fancy.cache.cases;

import pers.fancy.cache.cases.base.TestBase;
import pers.fancy.cache.domain.User;
import pers.fancy.cache.exception.CacheException;
import pers.fancy.cache.service.UserService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;


public class ExceptionTest extends TestBase {

    @Autowired
    private UserService service;

    @Test
    public void test1() {
        service.noParam();
    }

    @Test
    public void test2() {
        service.noCacheKey(new Object());
    }

    @Test(expected = CacheException.class)
    public void test3() {
        service.wrongMultiParam(new Object());
    }

    @Test
    public void test4() {
        service.wrongIdentifier(Lists.newArrayList(1, 2));
    }

    @Test(expected = CacheException.class)
    public void test41() {
        service.wrongCollectionReturn(Lists.newArrayList(1, 2));
    }

    @Test(expected = NullPointerException.class)
    public void test50() {
        List<User> users = service.correctIdentifier(null);
    }

    @Test
    public void test51() {
        service.correctIdentifier(Collections.<Integer>emptyList());
        service.correctIdentifier(Collections.<Integer>emptyList());
    }

    @Test
    public void test5() {
        service.correctIdentifier(Lists.newArrayList(1, 2));
        List<User> users = service.correctIdentifier(Lists.newArrayList(1, 2, 3, 4, 5, 6));
        System.out.println(users);
    }
}
