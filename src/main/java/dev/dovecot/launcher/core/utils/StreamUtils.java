package dev.dovecot.launcher.core.utils;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtils
{
    private static byte[] readInputStream(final InputStream stream) throws IOException
    {
        final byte[] bytes = stream.readAllBytes();
        stream.close();
        return bytes;
    }
}
