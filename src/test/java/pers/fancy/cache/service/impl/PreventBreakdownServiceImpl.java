package pers.fancy.cache.service.impl;

import pers.fancy.cache.CacheKey;
import pers.fancy.cache.Cached;
import pers.fancy.cache.domain.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PreventBreakdownServiceImpl {

    @Cached
    public Map<Integer, User> getMap(@CacheKey("'id:' + #arg0[#i]") List<Integer> ids, @CacheKey String name) {
        Map<Integer, User> map = new HashMap<>(ids.size());
        // 故意不返回第一个
        for (int i = 1; i < ids.size(); ++i) {
            map.put(ids.get(i), new User(ids.get(i), name + ids.get(i)));
        }

        return map;
    }

    @Cached
    public List<User> getUsers(@CacheKey(value = "'id:' + #arg0[#i]", field = "id") Set<Integer> ids) {
        List<User> u = new ArrayList<>();
        for (int i : ids) {
            u.add(new User(i, "name" + i));
        }

        return u;
    }

}
