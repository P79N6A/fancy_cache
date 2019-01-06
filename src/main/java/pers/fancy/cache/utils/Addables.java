package pers.fancy.cache.utils;

import pers.fancy.cache.exception.CacheException;
import pers.fancy.tools.utils.Collections3;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Addables {

    public interface Addable<T> {

        Addable init(Class<T> type, int initSize);

        Addable addAll(List<Object> list);

        T getResult();
    }

    private static class ArrayAddable implements Addable<Object[]> {

        private Object[] instance;

        @Override
        public Addable init(Class<Object[]> type, int initSize) {
            this.instance = new Object[initSize];
            return this;
        }

        @Override
        public Addable addAll(List<Object> list) {
            for (int i = 0; i < list.size(); ++i) {
                this.instance[i] = list.get(i);
            }

            return this;
        }

        @Override
        public Object[] getResult() {
            return this.instance;
        }
    }

    private static class CollectionAddable implements Addable<Collection> {

        private Collection instance;

        @Override
        public Addable init(Class<Collection> type, int initSize) {
            try {
                this.instance = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CacheException("could not invoke collection: " + type.getName() + "'s no param (default) constructor!", e);
            }

            return this;
        }

        @Override
        public Addable addAll(List<Object> list) {
            this.instance.addAll(list);
            return this;
        }

        @Override
        public Collection getResult() {
            return this.instance;
        }
    }

    private static class MapAddable implements Addable<Map> {

        private Map instance;

        @Override
        public Addable init(Class<Map> type, int initSize) {
            try {
                this.instance = type.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CacheException("could not invoke Map: " + type.getName() + "'s no param (default) constructor!", e);
            }

            return this;
        }

        @Override
        public Addable addAll(List<Object> list) {
            if (Collections3.isEmpty(list)) {
                return this;
            }

            list.stream().map(obj -> (Map.Entry) obj).forEach(entry -> instance.put(entry.getKey(), entry.getValue()));

            return this;
        }

        @Override
        public Map getResult() {
            return instance;
        }
    }

    public static Addable newAddable(Class<?> type, int size) {
        if (Map.class.isAssignableFrom(type)) {
            return new MapAddable().init((Class<Map>) type, size);
        } else if (Collection.class.isAssignableFrom(type)) {
            return new CollectionAddable().init((Class<Collection>) type, size);
        } else {
            return new ArrayAddable().init((Class<Object[]>) type, size);
        }
    }

    public static Collection newCollection(Class<?> type, Collection initCollection) {
        try {
            Collection collection = (Collection) type.newInstance();
            if (Collections3.isNotEmpty(initCollection)) {
                collection.addAll(initCollection);
            }

            return collection;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CacheException("could not invoke collection: " + type.getName() + "'s no param (default) constructor!", e);
        }
    }

    public static Map newMap(Class<?> type, Map initMap) {
        try {
            Map map = (Map) type.newInstance();
            if (Collections3.isNotEmpty(initMap)) {
                map.putAll(initMap);
            }
            return map;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CacheException("could not invoke map: " + type.getName() + "'s no param (default) constructor!", e);
        }
    }
}
