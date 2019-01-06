package pers.fancy.cache.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class ArgNameGenerator {

    private static final String X_ARGS_PREFIX = "args";

    private static String[] X_ARGS = {
            X_ARGS_PREFIX + 0,
            X_ARGS_PREFIX + 1,
            X_ARGS_PREFIX + 2,
            X_ARGS_PREFIX + 3,
            X_ARGS_PREFIX + 4,
            X_ARGS_PREFIX + 5,
            X_ARGS_PREFIX + 6,
            X_ARGS_PREFIX + 7,
            X_ARGS_PREFIX + 8,
            X_ARGS_PREFIX + 9,
            X_ARGS_PREFIX + 10,
            X_ARGS_PREFIX + 11,
            X_ARGS_PREFIX + 12,
            X_ARGS_PREFIX + 13,
            X_ARGS_PREFIX + 14,
            X_ARGS_PREFIX + 15,
            X_ARGS_PREFIX + 16,
            X_ARGS_PREFIX + 17,
            X_ARGS_PREFIX + 18,
            X_ARGS_PREFIX + 19
    };

    private static boolean isFirst = true;

    private static final ConcurrentMap<Method, String[]> methodParameterNames = new ConcurrentHashMap<>();

    public static String[] getArgNames(Method method) {
        return methodParameterNames.computeIfAbsent(method, ArgNameGenerator::doGetArgNamesWithJava8);
    }

    /**由于编译参数–parameters的影响, 开启了该参数, 获取到的参数名为真是的方法参数Name; 没有开启: 则是获取到argN这种.
    为了方便用户, 我们统一生成xArgN这种方式来填充, 同时也兼容原先的这种生成方式¬*/
    public static String[] getXArgNames(int valueSize) {
        if (valueSize == 0) {
            return new String[0];
        }

        String[] xArgs = new String[valueSize];
        for (int i = 0; i < valueSize; ++i) {
            xArgs[i] = i < X_ARGS.length ? X_ARGS[i] : X_ARGS_PREFIX + i;
        }

        return xArgs;
    }

    /**Java1.8之后提供了获取参数名方法, 但需要编译时添加`–parameters`参数支持, 如`javac –parameters`, 不然参数名为'arg0'*/
    private static String[] doGetArgNamesWithJava8(Method method) {
        Parameter[] parameters = method.getParameters();
        String[] argNames = Arrays.stream(parameters).map(Parameter::getName).toArray(String[]::new);
        if (isFirst && argNames.length != 0 && argNames[0].equals("arg0")) {
            CacheLogger.warn("compile not set '–parameters', used default method parameter names");
            isFirst = false;
        }

        return argNames;
    }

}
