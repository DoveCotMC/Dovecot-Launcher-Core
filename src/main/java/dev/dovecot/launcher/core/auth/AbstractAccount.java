package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

public abstract class AbstractAccount
{
    protected final String name;
    protected final String uuid;
    protected final String accessToken;
    protected final String clientToken;

    protected AbstractAccount(final String name, final String uuid, final String accessToken, final String clientToken)
    {
        this.name = name;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.clientToken = clientToken;
    }

    public final String getName()
    {
        return this.name;
    }

    public final String getAccessToken()
    {
        return this.accessToken;
    }

    public final String getUuid()
    {
        return this.uuid;
    }

    public abstract String getType();

    public abstract JSONObject toJson();
}
