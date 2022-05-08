package dev.dovecot.launcher.core.game;

import dev.dovecot.launcher.core.utils.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameDirectory extends File
{
    protected final String name;

    public GameDirectory(final String name, final String pathname)
    {
        super(pathname);
        this.name = name;
    }

    public GameDirectory(final String name, final File parent, final String child)
    {
        super(parent, child);
        this.name = name;
    }

    public GameDirectory(final String name, final URI uri)
    {
        super(uri);
        this.name = name;
    }

    public List<GameVersion> loadVersions() throws IOException
    {
        final List<GameVersion> versions = new ArrayList<>();
        final File versionsDir = new File(this, "versions");
        if (versionsDir.exists())
        {
            for (final File file : Objects.requireNonNull(versionsDir.listFiles()))
            {
                final File jsonFile = new File(versionsDir, file.getName() + "\\" + file.getName() + ".json");
                if (jsonFile.exists())
                {
                    versions.add(new GameVersion(file.getName(), this, new JSONObject(new String(FileUtils.readFile(jsonFile), StandardCharsets.UTF_8))));
                }
            }
        }
        return versions;
    }
}
