package pers.fancy.cache.utils;

import java.io.Serializable;


public class PreventObjects {

    public static Object getPreventObject() {
        return PreventObj.INSTANCE;
    }

    public static boolean isPrevent(Object object) {
        return object == PreventObj.INSTANCE || object instanceof PreventObj;
    }

    private static final class PreventObj implements Serializable {

        private static final long serialVersionUID = -1102811488039755703L;

        private static final PreventObj INSTANCE = new PreventObj();
    }
}
