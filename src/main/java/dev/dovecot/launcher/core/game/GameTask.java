package dev.dovecot.launcher.core.game;

import dev.dovecot.launcher.core.auth.AbstractAccount;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameTask
{
    protected final String[] arguments;

    protected GameTask(final String[] arguments)
    {
        this.arguments = arguments;
    }

    public Process run() throws IOException
    {
        return Runtime.getRuntime().exec(this.arguments);
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
                        System.out.println(file.getAbsolutePath());
                    }
                }
                else
                {
                    final String packageName = ((JSONObject) o).getString("name").split(":")[0];
                    final String libraryName = ((JSONObject) o).getString("name").split(":")[1];
                    final String versionName = ((JSONObject) o).getString("name").split(":")[2];
                    System.out.println(packageName);
                    System.out.println(libraryName);
                    System.out.println(versionName);
                }
            }
        }
        arguments = ArrayUtils.addAll(arguments, "-cp", classpath.toString());
        return new GameTask(arguments);
    }
}
