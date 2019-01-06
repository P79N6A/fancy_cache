package pers.fancy.cache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志记录工具
 * @author fancy
 */
public class CacheLogger {

    private static Logger logger = LoggerFactory.getLogger("pers.fancy.cache");

    public static void debug(String format, Object... arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(format, arguments);
        }
    }

    public static void info(String format, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    public static void warn(String format, Object... arguments) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, arguments);
        }
    }

    public static void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }
}
