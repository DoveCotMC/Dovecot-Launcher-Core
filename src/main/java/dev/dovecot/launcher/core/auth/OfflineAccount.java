package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

public class OfflineAccount extends AbstractAccount
{
    public OfflineAccount(final String name)
    {
        super(name, "\"\"", "\"\"");
    }

    @Override
    public JSONObject toJson()
    {
        return null;
    }

    @Override
    public String getType()
    {
        return "offline";
    }
}
