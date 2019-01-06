package pers.fancy.cache.domain;

import java.util.*;


/**
 *
 * @author fancy
 */
public class CacheReadResult {

    private Map<String, Object> hitKeyMap;

    private Set<String> missKeySet;

    /** 有参构造存在，必须声明无参构造*/
    public CacheReadResult() {}

    public CacheReadResult(Map<String, Object> hitKeyMap, Set<String> missKeySet) {
        this.hitKeyMap = hitKeyMap;
        this.missKeySet = missKeySet;
    }

    public Map<String, Object> getHitKeyMap() {
        return hitKeyMap == null ? Collections.emptyMap() : hitKeyMap;
    }

    public Set<String> getMissKeySet() {
        return missKeySet == null ? Collections.emptySet() : missKeySet;
    }

}
