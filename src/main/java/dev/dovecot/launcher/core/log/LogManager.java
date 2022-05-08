package dev.dovecot.launcher.core.log;

import java.util.ArrayList;
import java.util.List;

public final class LogManager
{
    public static final List<Logger> LOGGERS = new ArrayList<>();
    private static Logger defaultLogger;

    public static List<Logger> getLoggers()
    {
        return LOGGERS;
    }

    public static void setDefault(final Logger logger)
    {
        defaultLogger = logger;
    }

    public static Logger getDefault()
    {
        return defaultLogger;
    }
}
