package dev.dovecot.launcher.core.game;

import org.json.JSONObject;

import java.io.File;

public class GameVersion
{
    protected final String name;
    protected final JSONObject json;
    protected final GameDirectory gameDir;

    public GameVersion(final String name, final JSONObject json, final GameDirectory gameDir)
    {
        this.name = name;
        this.json = json;
        this.gameDir = gameDir;
    }
}
