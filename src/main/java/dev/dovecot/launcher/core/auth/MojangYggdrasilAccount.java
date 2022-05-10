package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

public class MojangYggdrasilAccount extends AbstractYggdrasilAccount
{
    protected MojangYggdrasilAccount(final String name, final String uuid, final String accessToken)
    {
        super(name, uuid, accessToken);
    }

    @Override
    public AbstractAccount fromJson(JSONObject json)
    {
        return null;
    }

    @Override
    public JSONObject toJson()
    {
        return null;
    }
}
