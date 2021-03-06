package dev.dovecot.launcher.core.utils;

import java.io.*;
import java.util.Objects;

public final class FileUtils
{
    public static void createFile(final File file) throws IOException
    {
        if (file.exists())
        {
            file.delete();
//            throw new IOException("Could not delete existed File!");
        }
        if (!Objects.isNull(file.getParentFile()))
        {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
    }

    public static byte[] readFile(final File file) throws IOException
    {
        final InputStream stream = new FileInputStream(file);
        final byte[] bytes = stream.readAllBytes();
        stream.close();
        return bytes;
    }

    public static void writeFile(final File file, final byte[] content) throws IOException
    {
        createFile(file);
        final OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(content);
        outputStream.flush();
        outputStream.close();
    }
}
