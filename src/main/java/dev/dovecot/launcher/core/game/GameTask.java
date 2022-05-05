package dev.dovecot.launcher.core.game;

import java.io.IOException;

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

    public static GameTask generateNewTask() throws IOException
    {
        return new GameTask(new String[]{"java"});
    }
}
