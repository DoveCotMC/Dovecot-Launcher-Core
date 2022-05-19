package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

import java.io.IOException;

public class MojangYggdrasilAccount extends AbstractYggdrasilAccount
{
    protected MojangYggdrasilAccount(final String name, final String uuid, final String accessToken, final String clientToken)
    {
        super(name, uuid, accessToken, clientToken);
    }

    public static AbstractAccount fromJson(JSONObject json)
    {
        return null;
    }

    @Override
    public void invalidate() throws IOException
    {
    }

    @Override
    public boolean isTokenAvailable() throws IOException
    {
        return false;
    }

    @Override
    public AbstractYggdrasilAccount refresh() throws IOException
    {
        return this;
    }

    @Override
    public JSONObject toJson()
    {
        return null;
    }
}
