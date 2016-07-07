package com.lion328.xenonlauncher.updater;

import com.lion328.xenonlauncher.downloader.DownloaderCallback;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;
import com.lion328.xenonlauncher.settings.LauncherConstant;
import com.lion328.xenonlauncher.util.URLUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class LauncherUpdater
{

    public static final URL DEFAULT_CHECK_URL = URLUtil.constantURL("https://minecraft.lion328.com/xenonlauncher/api/version");
    public static final URL DEFAULT_DOWNLOAD_URL = URLUtil.constantURL("https://minecraft.lion328.com/xenonlauncher/files/launcher.jar");
    public static final URL DEFAULT_DOWNLOAD_WINDOWS_URL = DEFAULT_DOWNLOAD_URL;

    private final URL checkURL;
    private final URL downloadURL;
    private final File launcherFile;

    public LauncherUpdater(URL checkURL, URL downloadURL, File launcherFile)
    {
        this.checkURL = checkURL;
        this.downloadURL = downloadURL;
        this.launcherFile = launcherFile;
    }

    public static String getLauncherVersion(File launcherFile) throws IOException
    {
        URLClassLoader classLoader = new URLClassLoader(new URL[] {launcherFile.toURI().toURL()});

        try
        {
            Class<?> mainClass = classLoader.loadClass("com.lion328.xenonlauncher.main.Main");
            Method method = mainClass.getMethod("getLauncherVersion");

            return (String) method.invoke(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e); // invalid launcher executable case
        }
    }

    public void update(DownloaderCallback callback) throws IOException
    {
        if (LauncherConstant.VERSION.equals(getRemoteLauncherVersion()))
        {
            return;
        }

        URLFileDownloader downloader = new URLFileDownloader(downloadURL, launcherFile);

        if (callback != null)
        {
            downloader.registerCallback(callback);
        }

        downloader.download();

        patchLauncher();
    }

    public String getRemoteLauncherVersion() throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) checkURL.openConnection();

        connection.setRequestMethod("GET");

        return new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().trim();
    }

    private void patchLauncher() throws IOException
    {
        ByteArrayOutputStream outTmp = new ByteArrayOutputStream(8192);
        JarOutputStream out = new JarOutputStream(outTmp);
        JarInputStream in = new JarInputStream(new FileInputStream(launcherFile));

        JarEntry entry;
        byte[] buffer = new byte[8192];
        int read;

        while ((entry = in.getNextJarEntry()) != null)
        {
            entry = new JarEntry(entry.getName());
            out.putNextEntry(entry);

            if (!entry.isDirectory())
            {
                while ((read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, read);
                }
            }
            else if (entry.getName().equals("com/lion328/xenonlauncher/settings/launcherid"))
            {
                out.write(LauncherConstant.ID.getBytes(StandardCharsets.UTF_8));
            }

            out.closeEntry();
        }

        in.close();

        out = new JarOutputStream(new FileOutputStream(launcherFile));
        out.write(outTmp.toByteArray());

        out.close();
    }
}
