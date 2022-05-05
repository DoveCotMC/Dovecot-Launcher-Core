package dev.dovecot.launcher.core.web;

import dev.dovecot.launcher.core.log.LogManager;
import dev.dovecot.launcher.core.task.IDownloadTask;
import dev.dovecot.launcher.core.utils.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DownloadTask implements IDownloadTask
{
    protected final String urlString;
    protected final File outputFile;
    protected final int connectTimeOut;
    protected final int readTimeOut;
    protected final int poolSize;

    protected long total;
    protected long current;

    public DownloadTask(final String url, final File output)
    {
        this.urlString = url;
        this.outputFile = output;
        this.connectTimeOut = 10000;
        this.readTimeOut = 60000;
        this.poolSize = 1;
    }

    public DownloadTask(final String url, final File output, final int poolSize)
    {
        this.urlString = url;
        this.outputFile = output;
        this.connectTimeOut = 10000;
        this.readTimeOut = 60000;
        this.poolSize = poolSize;
    }

    public DownloadTask(final String url, final File output, final int connectTimeOut, final int readTimeOut)
    {
        this.urlString = url;
        this.outputFile = output;
        this.connectTimeOut = connectTimeOut;
        this.readTimeOut = readTimeOut;
        this.poolSize = 1;
    }

    public DownloadTask(final String url, final File output, final int connectTimeOut, final int readTimeOut, final int poolSize)
    {
        this.urlString = url;
        this.outputFile = output;
        this.connectTimeOut = connectTimeOut;
        this.readTimeOut = readTimeOut;
        this.poolSize = poolSize;
    }

    @Override
    public void download() throws IOException
    {
        LogManager.getDefault().info(String.format("Start downloading File from \"%s\" to \"%s\" with %d threads.", this.urlString, this.outputFile.getAbsolutePath(), this.poolSize));
        final long startTime = System.currentTimeMillis();
        FileUtils.createFile(this.outputFile);
        this.total = new URL(this.urlString).openConnection().getContentLength();
        final Thread parentThread = Thread.currentThread();
        if (this.poolSize <= 1)
        {
            final Thread thread = new Thread(() ->
            {
                try
                {
                    final HttpURLConnection connection = (HttpURLConnection) new URL(this.urlString).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(this.connectTimeOut);
                    connection.setReadTimeout(this.readTimeOut);
                    connection.setDoInput(true);
                    final InputStream inputStream = connection.getInputStream();
                    final FileOutputStream outputStream = new FileOutputStream(this.outputFile);
                    outputStream.write(inputStream.readAllBytes());
                    this.current += this.total;
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
                catch (Exception e)
                {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(parentThread, e);
                }
            });
            thread.run();
        }
        else
        {
            final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.poolSize);
            for (int i = 0; i < this.poolSize; ++i)
            {
                final int start = (int) (i * ((double) this.total / (double) this.poolSize));
                final int end = (int) ((i + 1) * ((double) this.total / (double) this.poolSize));
                final Thread thread = new Thread(() ->
                {
                    try
                    {
                        final HttpURLConnection connection = (HttpURLConnection) new URL(this.urlString).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(this.connectTimeOut);
                        connection.setReadTimeout(this.readTimeOut);
                        connection.setDoInput(true);
                        connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
                        final InputStream inputStream = connection.getInputStream();
                        final RandomAccessFile outputStream = new RandomAccessFile(this.outputFile, "rw");
                        outputStream.seek(start);
                        outputStream.write(inputStream.readAllBytes());
                        outputStream.close();
                        inputStream.close();
                        this.current += end - start;
                    }
                    catch (Exception e)
                    {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(parentThread, e);
                    }
                });
                pool.execute(thread);
            }
            while (pool.getCompletedTaskCount() < this.poolSize);
            pool.shutdown();
        }
        LogManager.getDefault().info("Download Complete, Time used: " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    @Override
    public void cancel()
    {
        this.outputFile.delete();
    }

    @Override
    public long getTotal()
    {
        return this.total;
    }

    @Override
    public long getCurrent()
    {
        return this.current;
    }
}
