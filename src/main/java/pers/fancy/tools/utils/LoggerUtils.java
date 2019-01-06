package pers.fancy.tools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggerUtils {

    private static final Logger logger = LoggerFactory.getLogger("com.github.tools");

    static void info(String format, Object... arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(format, arguments);
        }
    }

    static void error(String format, Object... arguments) {
        if (logger.isErrorEnabled()) {
            logger.error(format, arguments);
        }
    }
}
