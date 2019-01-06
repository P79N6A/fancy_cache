package pers.fancy.tools.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class SpringLoggerHelper {

    private static final Logger logger = LoggerFactory.getLogger("com.github.tools.spring");

    static void info(String format, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(format, args);
        }
    }

    static void warn(String format, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(format, args);
        }
    }

    static void error(String format, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(format, args);
        }
    }
}
