package dev.dovecot.launcher.core.web;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UrlLoader
{
    public static String loadUrl(final String key) throws IOException
    {
        return new JSONObject(new String(Objects.requireNonNull(UrlLoader.class.getClassLoader().getResourceAsStream("urls/default.json")).readAllBytes(), StandardCharsets.UTF_8)).getJSONObject("urls").getString(key);
    }

    public static String replaceUrl(final String url) throws IOException
    {
        return url;
    }
}
