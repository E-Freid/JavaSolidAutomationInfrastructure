package org.automation.elifreid.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReporter {
    private static final Logger logger = LoggerFactory.getLogger(TestReporter.class);

    public static void log(String msg)
    {
        logger.info(msg);
    }

    public static void log(String msg, Object... args)
    {
        logger.info(msg, args);
    }
}
