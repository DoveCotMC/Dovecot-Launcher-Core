package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

public abstract class AbstractYggdrasilAccount extends AbstractAccount
{
    protected AbstractYggdrasilAccount(final String name, final String uuid, final String accessToken)
    {
        super(name, uuid, accessToken);
    }

    @Override
    public String getType()
    {
        return "yggdrasil";
    }
}
