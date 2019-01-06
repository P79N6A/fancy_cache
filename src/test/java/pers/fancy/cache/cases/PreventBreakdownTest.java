package pers.fancy.cache.cases;

import pers.fancy.cache.cases.base.TestBase;
import pers.fancy.cache.domain.User;
import pers.fancy.cache.service.impl.PreventBreakdownServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 缓存击穿
 * 对于一些设置了过期时间的key，如果这些key可能会在某些时间点被超高并发地访问，是一种非常“热点”的数据。这个时候，需要考虑一个问题：缓存被“击穿”的问题，这个和缓存雪崩的区别在于这里针对某一key缓存，前者则是很多key。
 *
 * 缓存在某个时间点过期的时候，恰好在这个时间点对这个Key有大量的并发请求过来，这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把后端DB压垮。
 *
 * 解决方案
 * 1.使用互斥锁(mutex key)
 * 业界比较常用的做法，是使用mutex。简单地来说，就是在缓存失效的时候（判断拿出来的值为空），不是立即去load db，而是先使用缓存工具的某些带成功操作返回值的操作（比如Redis的SETNX或者Memcache的ADD）去set一个mutex key，
 * 当操作返回成功时，再进行load db的操作并回设缓存；否则，就重试整个get缓存的方法。
 */
public class PreventBreakdownTest extends TestBase {


    @Autowired
    private PreventBreakdownServiceImpl service;

    @Test
    public void test() {
        List<Integer> ids = IntStream.range(0, 3).boxed().collect(Collectors.toList());

        Map<Integer, User> map = service.getMap(ids, "-feiqing");
        System.out.println("first map.size() = " + map.size());

        ids.add(4);
        map = service.getMap(ids, "-feiqing");
        System.out.println("second map.size() = " + map.size());
    }

    @Test
    public void test2() {
        Set<Integer> sets = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            sets.add(i);
        }

        System.out.println(service.getUsers(sets));
        sets.add(10);
        System.out.println(service.getUsers(sets));
    }

}