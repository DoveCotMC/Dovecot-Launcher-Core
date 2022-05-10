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

    public GameDirectory(final String name, final String pathname) throws IOException
    {
        super(pathname);
        this.name = name;
        this.initialize();
    }

    public GameDirectory(final String name, final File parent, final String child) throws IOException
    {
        super(parent, child);
        this.name = name;
        this.initialize();
    }

    public GameDirectory(final String name, final URI uri) throws IOException
    {
        super(uri);
        this.name = name;
        this.initialize();
    }

    protected void initialize() throws IOException
    {
        new File(this, "assets").mkdirs();
        new File(this, "config").mkdirs();
        new File(this, "libraries").mkdirs();
        new File(this, "versions").mkdirs();
        new File(this, "resourcepacks").mkdirs();
        new File(this, "saves").mkdirs();
        new File(this, "screenshots").mkdirs();
        final File profiles = new File(this, "launcher_profiles.json");
        if (!profiles.exists())
        {
            FileUtils.writeFile(profiles, new JSONObject().toString().getBytes(StandardCharsets.UTF_8));
        }
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
