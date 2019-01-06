package pers.fancy.cache.cases;

import pers.fancy.cache.cases.base.TestBase;
import pers.fancy.cache.domain.User;
import pers.fancy.cache.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class SingleTest extends TestBase {

    @Autowired
    private UserService userService;

    @Test
    public void testSingleKey() throws InterruptedException {
        int id = 1;
        System.out.println("#########---");
        userService.singleKey(id, "ff-no-key", "no-key");

        System.out.println("---");
        userService.singleKey(id, "ff-no-key", "ls");

        System.out.println("---");
        userService.singleRemove(id, "woca", "ls");

        System.out.println("---");
        userService.singleKey(id, "ff-no-key", "ls");
        System.out.println("#########---");

    }

    //@Test
    public void testRemoveSingleKey() {
        int id = 1;
        String name = "fq";
        System.out.println("#########---");
        User user = userService.singleKey(id, "ff-no-key", "no-key");

        System.out.println("---");
        userService.singleRemove(id, name, "not`non");

        System.out.println("---");
        user = userService.singleKey(id, "ff-no-key", "ls");

        System.out.println("---");
        user = userService.singleKey(id, "ff-no-key", "ls");
        System.out.println("#########---");
    }

    @Test
    public void testSpEL() {
        User user = new User(1, "fancy", new Date(), 1, "chengdu");
        boolean b = userService.spelCompose(user);
        System.out.println(b);
    }
}
