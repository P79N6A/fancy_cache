package pers.fancy.tools.utils;


public class Objects2 {

    public static <T> T nullToDefault(T nullable, T defaultValue) {
        return nullable == null ? defaultValue : nullable;
    }
}
