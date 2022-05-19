package dev.dovecot.launcher.core.game;

import dev.dovecot.launcher.core.auth.AbstractAccount;
import dev.dovecot.launcher.core.auth.AuthlibInjectorAccount;
import dev.dovecot.launcher.core.utils.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTask
{
    protected final String[] arguments;
    protected final GameVersion version;

    protected GameTask(final String[] arguments, final GameVersion version)
    {
        this.arguments = arguments;
        this.version = version;
    }

    public Process run() throws IOException
    {
        final ProcessBuilder builder = new ProcessBuilder();
        builder.command(this.arguments);
        builder.directory(version.getGameDir());
        return builder.start();
    }

    public static String[] generateJVMArguments(final GameVersion version)
    {
        if (version.getJson().has("arguments"))
        {
            final List<String> arguments = new ArrayList<>();
            for (final Object value : version.getJson().getJSONObject("arguments").getJSONArray("jvm"))
            {
                if (value instanceof String)
                {
                    if (((String) value).contains("-Djava.library.path") || ((String) value).contains("-Dminecraft.launcher.brand") || ((String) value).contains("-Dminecraft.launcher.version") || ((String) value).contains("-cp") || ((String) value).contains("${classpath}"))
                    {
                        continue;
                    }
                    String finalValue = String.valueOf(value);
                    finalValue = finalValue.replace("${library_directory}", new File(version.getGameDir(), "libraries").getAbsolutePath());
                    finalValue = finalValue.replace("${classpath_separator}", File.pathSeparator);
                    finalValue = finalValue.replace("${version_name}", version.getName());
                    finalValue = finalValue.replace("\\", "/");
                    arguments.add(finalValue);
                }
            }
            return arguments.toArray(new String[]{});
        }
        else
        {
            return new String[]{};
        }
    }

    public static GameTask generateNewTask(final GameVersion version, final String java, final String[] jvmArguments, final AbstractAccount account) throws IOException
    {
        String[] arguments = ArrayUtils.addAll(new String[]{java}, jvmArguments);
        if (account instanceof AuthlibInjectorAccount)
        {
            final File authlibInjectorFile = new File(version.getGameDir(), "authlib-injector.jar");
            if (authlibInjectorFile.exists())
            {
                FileUtils.createFile(authlibInjectorFile);
                FileUtils.writeFile(authlibInjectorFile, GameTask.class.getClassLoader().getResourceAsStream("authlib-injector.jar").readAllBytes());
            }
            arguments = ArrayUtils.addAll(arguments, String.format("-javaagent:%s=%s", authlibInjectorFile.getAbsoluteFile(), ((AuthlibInjectorAccount) account).getYggdrasilUrl()));
        }
        arguments = ArrayUtils.addAll(arguments, "-Djava.library.path=" + new File(version.getGameDir(), "versions\\" + version.getName() + "\\natives").getAbsolutePath());
        arguments = ArrayUtils.addAll(arguments, "-Dlog4j2.formatMsgNoLookups=true", "-Djava.rmi.server.useCodebaseOnly=true", "-Dcom.sun.jndi.rmi.object.trustURLCodebase=false", "-Dcom.sun.jndi.cosnaming.object.trustURLCodebase=false");
        final StringBuilder classpath = new StringBuilder();
        for (final Object o : version.getJson().getJSONArray("libraries"))
        {
            if (o instanceof JSONObject)
            {
                if (((JSONObject) o).has("downloads"))
                {
                    final File file = new File(new File(version.getGameDir(), "libraries"), ((JSONObject) o).getJSONObject("downloads").getJSONObject("artifact").getString("path"));
                    if (file.exists())
                    {
                        classpath.append(file.getAbsolutePath()).append(File.pathSeparator);
                    }
                }
                else
                {
                    final String packageName = ((JSONObject) o).getString("name").split(":")[0];
                    final String libraryName = ((JSONObject) o).getString("name").split(":")[1];
                    final String versionName = ((JSONObject) o).getString("name").split(":")[2];
                    final File libraryFile = new File(version.getGameDir(), "libraries\\" + packageName.replace(".", "\\") + "\\" + libraryName + "\\" + versionName + "\\" + libraryName + "-" + versionName + ".jar");
                    if (libraryFile.exists())
                    {
                        classpath.append(libraryFile.getAbsolutePath()).append(File.pathSeparator);
                    }
                }
            }
        }
        classpath.append(new File(version.getGameDir(), "versions\\" + version.getName() + "\\" + version.getName() + ".jar").getAbsolutePath());
        arguments = ArrayUtils.addAll(arguments, "-cp", classpath.toString());
        arguments = ArrayUtils.addAll(arguments, version.getJson().getString("mainClass"));
        if (version.getJson().has("arguments"))
        {
            final List<String> gameArguments = new ArrayList<>();
            for (final Object value : version.getJson().getJSONObject("arguments").getJSONArray("game"))
            {
                if (value instanceof String)
                {
                    String replacedValue = value.toString();
                    replacedValue = replacedValue.replace("${auth_player_name}", account.getName());
                    replacedValue = replacedValue.replace("${version_name}", "Dovecot");
                    replacedValue = replacedValue.replace("${game_directory}", version.getGameDir().getAbsolutePath());
                    replacedValue = replacedValue.replace("${assets_root}", new File(version.getGameDir(), "assets").getAbsolutePath());
                    replacedValue = replacedValue.replace("${assets_index_name}", version.getJson().getJSONObject("assetIndex").getString("id"));
                    replacedValue = replacedValue.replace("${auth_uuid}", account.getUuid());
                    replacedValue = replacedValue.replace("${auth_access_token}", account.getAccessToken());
                    replacedValue = replacedValue.replace("${user_type}", account.getType());
                    replacedValue = replacedValue.replace("${version_type}", "DovecotMC");
                    gameArguments.add(replacedValue);
                }
            }
            arguments = ArrayUtils.addAll(arguments, gameArguments.toArray(new String[]{}));
        }
        else
        {
        }
        return new GameTask(arguments, version);
    }
}
