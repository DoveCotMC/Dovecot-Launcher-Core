package dev.dovecot.launcher.core.log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    public static final String PATTERN = "[%s] [Logger-%s/Thread-%s] [%s]: %s";

    protected final String name;
    protected final PrintStream stream;

    public Logger(final String name, final PrintStream printStream)
    {
        this.name = name;
        this.stream = printStream;
    }

    // Credits: https://zhuanlan.zhihu.com/p/208768786
    public void info(final String content)
    {
        final String output = String.format(PATTERN, new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())), this.name, Thread.currentThread().getName(), "INFO", content);
        this.stream.println(output);
        System.out.println("\u001b[0m" + output + "\u001b[0m");
    }

    public void warn(final String content)
    {
        final String output = String.format(PATTERN, new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())), this.name, Thread.currentThread().getName(), "ERROR", content);
        this.stream.println(output);
        System.out.println("\u001b[0;38;5;11m" + output + "\u001b[0m");
    }

    public void err(final String content)
    {
        final String output = String.format(PATTERN, new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())), this.name, Thread.currentThread().getName(), "ERROR", content);
        this.stream.println(output);
        System.out.println("\u001b[0;38;5;9m" + output + "\u001b[0m");
    }
}
