package dev.dovecot.launcher.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class FileUtils
{
    public static void createFile(final File file) throws IOException
    {
//        if (file.exists())
//        {
//            file.delete();
////            throw new IOException("Could not delete existed File!");
//        }
        if (!Objects.isNull(file.getParentFile()))
        {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
    }
}
