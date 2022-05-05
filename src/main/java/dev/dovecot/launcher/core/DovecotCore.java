package dev.dovecot.launcher.core;

import dev.dovecot.launcher.core.internet.DownloadTask;
import dev.dovecot.launcher.core.log.LogManager;
import dev.dovecot.launcher.core.log.Logger;
import dev.dovecot.launcher.core.task.IDownloadTask;
import dev.dovecot.launcher.core.utils.FileUtils;

import java.io.File;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DovecotCore
{
    public static void main(final String[] args) throws Exception
    {
        final File logFile = new File(new File("logs").getAbsoluteFile(), new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(System.currentTimeMillis())) + ".txt");
        FileUtils.createFile(logFile);
        final Logger DEFAULT_LOGGER = new Logger("Main", new PrintStream(logFile));
        LogManager.setDefault(DEFAULT_LOGGER);
    }
}
