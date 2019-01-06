package pers.fancy.cache.service.impl;

import pers.fancy.cache.CacheKey;
import pers.fancy.cache.Cached;
import pers.fancy.cache.domain.User;
import pers.fancy.cache.enums.Expiration;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class InnerMapService {

    @Cached(expire = Expiration.TEN_MIN)
    public Map<Integer, User> unmodifiableMap(@CacheKey(value = "'id:' + #arg0[#i]") List<Integer> ids) {
        if (ids.size() == 1) {
            int id = ids.get(0);
            return Collections.singletonMap(id, new User(id, "name" + id, new Date(), id, ""));
        } else {
            Map<Integer, User> map = new HashMap<>();
            for (Integer id : ids) {
                map.put(id, new User(id, "name" + id, new Date(), id, ""));
            }

            return Collections.unmodifiableMap(map);
        }
    }

    @Cached(expire = Expiration.TEN_MIN)
    public Map<Integer, User> synchronizedMap(@CacheKey(value = "'id:' + #arg0[#i]") List<Integer> ids) {
        Map<Integer, User> map = new HashMap<>();
        for (Integer id : ids) {
            map.put(id, new User(id, "name" + id, new Date(), id, ""));
        }

        return Collections.synchronizedMap(map);
    }

    @Cached(expire = Expiration.TEN_MIN)
    public Map<Integer, User> checkedMap(@CacheKey(value = "'id:' + #arg0[#i]") List<Integer> ids) {
        TreeMap<Integer, User> map = new TreeMap<>();
        for (Integer id : ids) {
            map.put(id, new User(id, "name" + id, new Date(), id, ""));
        }

        return Collections.checkedNavigableMap(map, Integer.class, User.class);
    }

    @Cached(prefix = "map-", expire = Expiration.TEN_MIN)
    public Map<Integer, User> immutableMap(@CacheKey(value = "'id:' + #arg0[#i]") List<Integer> ids) {
        TreeMap<Integer, User> map = new TreeMap<>();
        for (Integer id : ids) {
            map.put(id, new User(id, "name" + id, new Date(), id, ""));
        }

        return Collections.unmodifiableMap(map);
    }
}
