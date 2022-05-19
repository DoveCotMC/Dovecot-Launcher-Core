package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

import java.io.IOException;

public abstract class AbstractYggdrasilAccount extends AbstractAccount
{
    public static final String VALIDATE_SUFFIX = "validate";
    public static final String REFRESH_SUFFIX = "refresh";
    public static final String INVALIDATE_SUFFIX = "invalidate";

    protected AbstractYggdrasilAccount(final String name, final String uuid, final String accessToken, final String clientToken)
    {
        super(name, uuid, accessToken, clientToken);
    }

    public abstract void invalidate() throws IOException;

    public abstract boolean isTokenAvailable() throws IOException;

    public abstract AbstractYggdrasilAccount refresh() throws IOException;

    @Override
    public String getType()
    {
        return "yggdrasil";
    }
}
