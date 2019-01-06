package pers.fancy.cache.service;


import pers.fancy.cache.domain.User;

import java.util.List;
import java.util.Map;


public interface UserService {

    /**
     * multi
     ***/
    Map<Integer, User> returnMap(String app, List<Integer> ids, Object noKey);

    void invalidMap(String apps, List<Integer> ids);

    List<User> getUsers(List<Integer> ids, String name, Object non);

    void invalidList(List<User> users);

    /***
     * single
     ****/
    User singleKey(int id, String name, Object non);

    void singleRemove(int id, String name, Object non);

    void updateUser(User user, String name, Object non);

    boolean spelCompose(User user);

    /**
     * ops
     */
    void noParam();

    void noCacheKey(Object o);

    void wrongMultiParam(Object o);

    Map<Integer, Object> wrongIdentifier(List<Integer> ids);

    List<User> wrongCollectionReturn(List<Integer> ids);

    List<User> correctIdentifier(List<Integer> ids);
}
