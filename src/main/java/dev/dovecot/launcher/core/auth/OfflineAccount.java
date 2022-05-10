package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

public class OfflineAccount extends AbstractAccount
{
    public OfflineAccount(final String name)
    {
        super(name, "\"\"", "\"\"");
    }

    @Override
    public String getType()
    {
        return "offline";
    }

    @Override
    public AbstractAccount fromJson(final JSONObject json)
    {
        return null;
    }

    @Override
    public JSONObject toJson()
    {
        return null;
    }
}
