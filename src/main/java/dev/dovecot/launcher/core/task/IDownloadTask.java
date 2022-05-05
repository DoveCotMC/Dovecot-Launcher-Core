package dev.dovecot.launcher.core.task;

import java.io.IOException;

public interface IDownloadTask extends ITask
{
    void download() throws IOException;
}
