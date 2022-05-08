package dev.dovecot.launcher.core.game;

import org.json.JSONObject;

import java.io.File;

public class GameVersion
{
    protected final String name;
    protected final GameDirectory gameDir;
    protected final JSONObject json;

    public GameVersion(final String name, final GameDirectory gameDir, final JSONObject json)
    {
        this.name = name;
        this.gameDir = gameDir;
        this.json = json;
    }

    public boolean hasJar()
    {
        return new File(this.gameDir, "versions\\" + this.name + "\\" + this.name + ".jar").exists();
    }

    public String getName()
    {
        return this.name;
    }

    public GameDirectory getGameDir()
    {
        return this.gameDir;
    }

    public JSONObject getJson()
    {
        return this.json;
    }

    @Override
    public String toString()
    {
        final StringBuffer sb = new StringBuffer("GameVersion{");
        sb.append("name='").append(name).append('\'');
        sb.append(", id=").append(json.getString("id"));
        sb.append(", gameDir=").append(gameDir.getAbsolutePath());
        sb.append('}');
        return sb.toString();
    }
}
