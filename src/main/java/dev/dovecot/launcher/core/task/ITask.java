package dev.dovecot.launcher.core.task;

public interface ITask
{
    void cancel();

    long getTotal();

    long getCurrent();

    default double getProgress()
    {
        return (double) getCurrent() / (double) getTotal();
    }
}
