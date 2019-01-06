package pers.fancy.cache;

import pers.fancy.cache.domain.User;
import pers.fancy.cache.utils.SpelCalculator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class ObjectTestCase {

    @Test
    public void test() {
        List<User> users = Arrays.asList(new User(1, "fq1"), new User(2, "fq2"));
        String value = (String) SpelCalculator.calcSpelValueWithContext("#users[#index].name + ' '",
                new String[]{"users", "user", "index"}, new Object[]{users,null, 1},
                "kong");
        System.out.println(value);
    }
}
