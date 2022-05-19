package dev.dovecot.launcher.core.auth;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AuthlibInjectorAccount extends AbstractYggdrasilAccount
{
    protected final String serverName;
    protected final String yggdrasilUrl;
    protected final String serverUrl;
    protected final String registerUrl;

    protected AuthlibInjectorAccount(final String serverName, final String yggdrasilUrl, final String serverUrl, final String registerUrl, final String name, final String uuid, final String accessToken, final String clientToken)
    {
        super(name, uuid, accessToken, clientToken);
        this.serverName = serverName;
        this.yggdrasilUrl = yggdrasilUrl;
        this.serverUrl = serverUrl;
        this.registerUrl = registerUrl;
    }

    public static AbstractAccount fromJson(final JSONObject json)
    {
        return new AuthlibInjectorAccount(json.getString("server_name"), json.getString("yggdrasil_url"), json.getString("server_url"), json.getString("register_url"), json.getString("username"), json.getString("uuid"), json.getString("access_token"), json.getString("client_token"));
    }

    @Override
    public JSONObject toJson()
    {
        return new JSONObject().put("type", "authlib_injector").put("username", this.name).put("uuid", this.uuid).put("server_url", this.serverUrl).put("server_name", this.serverName).put("register_url", this.registerUrl).put("yggdrasil_url", this.yggdrasilUrl).put("access_token", this.accessToken).put("client_token", this.clientToken);
    }

    public String getYggdrasilUrl()
    {
        return this.yggdrasilUrl;
    }

    @Override
    public void invalidate() throws IOException
    {
        final HttpURLConnection connection = (HttpURLConnection) new URL(this.yggdrasilUrl.endsWith("/") ? this.yggdrasilUrl + "authserver/" + AbstractYggdrasilAccount.INVALIDATE_SUFFIX : this.yggdrasilUrl + "/authserver/" + AbstractYggdrasilAccount.INVALIDATE_SUFFIX).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.getOutputStream().write(new JSONObject().put("accessToken", this.accessToken).put("clientToken", this.clientToken).toString().getBytes(StandardCharsets.UTF_8));
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300)
        {
            return;
        }
        throw new IOException(connection.getResponseMessage());
    }

    @Override
    public boolean isTokenAvailable() throws IOException
    {
        final HttpURLConnection connection = (HttpURLConnection) new URL(this.yggdrasilUrl.endsWith("/") ? this.yggdrasilUrl + "authserver/" + AbstractYggdrasilAccount.VALIDATE_SUFFIX : this.yggdrasilUrl + "/authserver/" + AbstractYggdrasilAccount.VALIDATE_SUFFIX).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.getOutputStream().write(new JSONObject().put("accessToken", this.accessToken).put("clientToken", this.clientToken).toString().getBytes(StandardCharsets.UTF_8));
        if (connection.getResponseCode() < 300)
        {
            return true;
        }
        else if (connection.getResponseCode() == 403)
        {
            return false;
        }
        throw new IOException(connection.getResponseMessage());
    }

    @Override
    public AbstractYggdrasilAccount refresh() throws IOException
    {
        final HttpURLConnection connection = (HttpURLConnection) new URL(this.yggdrasilUrl.endsWith("/") ? this.yggdrasilUrl + "authserver/" + AbstractYggdrasilAccount.REFRESH_SUFFIX : this.yggdrasilUrl + "/authserver/" + AbstractYggdrasilAccount.REFRESH_SUFFIX).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.getOutputStream().write(new JSONObject().put("accessToken", this.accessToken).put("clientToken", this.clientToken).toString().getBytes(StandardCharsets.UTF_8));
        if (connection.getResponseCode() < 300 && connection.getResponseCode() >= 200)
        {
            final JSONObject json = new JSONObject(new String(connection.getInputStream().readAllBytes()));
            return new AuthlibInjectorAccount(this.serverName, this.yggdrasilUrl, this.serverUrl, this.registerUrl, this.name, this.uuid, json.getString("accessToken"), json.getString("clientToken"));
        }
        throw new IOException(connection.getResponseMessage());
    }

    public static AuthlibInjectorAccount[] authenticate(final String url, final String username, final String password) throws IOException
    {
        return authenticate(url, username, password, 10000, 60000);
    }

    public static AuthlibInjectorAccount[] authenticate(final String yggdrasilUrl, final String username, final String password, final long connectTimeOut, final long readTimeOut) throws IOException
    {
        final HttpURLConnection infoConnection = (HttpURLConnection) new URL(yggdrasilUrl).openConnection();
        infoConnection.setRequestMethod("GET");
        infoConnection.setDoInput(true);
        final JSONObject result = new JSONObject(new String(infoConnection.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        final String serverName = result.getJSONObject("meta").getString("serverName");
        final String serverUrl = result.getJSONObject("meta").getJSONObject("links").getString("homepage");
        final String registerUrl = result.getJSONObject("meta").getJSONObject("links").getString("register");
//        final String publicKey = result.getString("signaturePublickey").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        final HttpURLConnection authConnection = (HttpURLConnection) new URL(yggdrasilUrl.endsWith("/") ? yggdrasilUrl + "authserver/authenticate" : yggdrasilUrl + "/authserver/authenticate").openConnection();
        authConnection.setRequestMethod("POST");
        authConnection.setRequestProperty("Content-Type", "application/json");
        authConnection.setDoInput(true);
        authConnection.setDoOutput(true);
        authConnection.getOutputStream().write(new JSONObject().put("agent", new JSONObject().put("name", "minecraft").put("version", 1)).put("username", username).put("password", password).toString().getBytes(StandardCharsets.UTF_8));
        final JSONObject accountJson = new JSONObject(new String(authConnection.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        final ArrayList<AuthlibInjectorAccount> accounts = new ArrayList<>();
        for (final Object profile : accountJson.getJSONArray("availableProfiles"))
        {
            if (profile instanceof JSONObject)
            {
                accounts.add(new AuthlibInjectorAccount(serverName, yggdrasilUrl, serverUrl, registerUrl, ((JSONObject) profile).getString("name"), ((JSONObject) profile).getString("id"), accountJson.getString("accessToken"), accountJson.getString("clientToken")));
            }
        }
        return accounts.toArray(new AuthlibInjectorAccount[]{});
    }
}
