package dev.dovecot.launcher.core.task;

import java.io.IOException;

public interface IInstallTask extends ITask
{
    void install() throws IOException;
}
