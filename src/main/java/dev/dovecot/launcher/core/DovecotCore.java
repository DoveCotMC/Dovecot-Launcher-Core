package dev.dovecot.launcher.core;

import dev.dovecot.launcher.core.game.GameDirectory;
import dev.dovecot.launcher.core.log.LogManager;
import dev.dovecot.launcher.core.log.Logger;
import dev.dovecot.launcher.core.utils.FileUtils;

import java.io.File;
import java.io.PrintStream;
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

        final GameDirectory gameDir = new GameDirectory("Primary", ".minecraft");
        gameDir.mkdirs();
    }
}
