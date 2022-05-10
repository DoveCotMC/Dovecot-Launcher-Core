package dev.dovecot.launcher.core;

import dev.dovecot.launcher.core.auth.AbstractAccount;
import dev.dovecot.launcher.core.auth.AuthlibInjectorAccount;
import dev.dovecot.launcher.core.auth.OfflineAccount;
import dev.dovecot.launcher.core.game.GameDirectory;
import dev.dovecot.launcher.core.game.GameTask;
import dev.dovecot.launcher.core.game.GameVersion;
import dev.dovecot.launcher.core.log.LogManager;
import dev.dovecot.launcher.core.log.Logger;
import dev.dovecot.launcher.core.utils.FileUtils;
import dev.dovecot.launcher.core.web.UrlLoader;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DovecotCore
{
    public static final String SKIN_WEBSITE = "https://124.222.116.158/";
    public static final String SKIN_WEBSITE_YGGDRASIL = SKIN_WEBSITE + "api/yggdrasil/";
    public static final File SAVE_DIR = new File(System.getenv("APPDATA") + "\\.dovecot");
    public static void main(final String[] args) throws Exception
    {
        SAVE_DIR.mkdirs();
        final File logFile = new File(new File("logs").getAbsoluteFile(), new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(System.currentTimeMillis())) + ".txt");
        FileUtils.createFile(logFile);
        final Logger DEFAULT_LOGGER = new Logger("Main", new PrintStream(logFile));
        LogManager.setDefault(DEFAULT_LOGGER);

        final GameDirectory gameDir = new GameDirectory("Primary", ".minecraft");
        gameDir.mkdirs();
        final GameVersion version = gameDir.loadVersions().get(0);
        final GameTask task = GameTask.generateNewTask(version, "java", GameTask.generateJVMArguments(version), new OfflineAccount("Dev"));
        final Process process = task.run();
        System.out.println(new String(process.getInputStream().readAllBytes()));
        System.out.println(new String(process.getErrorStream().readAllBytes()));
    }
}
