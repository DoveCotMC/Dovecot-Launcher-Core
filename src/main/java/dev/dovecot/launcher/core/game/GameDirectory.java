package dev.dovecot.launcher.core.game;

import java.io.File;
import java.net.URI;
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

    public List<GameVersion> loadVersions()
    {
        final List<GameVersion> versions = new ArrayList<>();
        for (final File file : Objects.requireNonNull(this.listFiles()))
        {
            System.out.println(file.getName());
        }
        return versions;
    }
}
