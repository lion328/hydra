package net.mc_mafia.launcher;

import com.lion328.xenonlauncher.util.OS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Settings
{

    public static final Logger LOGGER = LogManager.getLogger("MCMafia-Launcher");
    public static final boolean IGNORE_LAUNCHER_UPDATER;
    public static final String LAUNCHER_VERSION = "0.1.4";
    public static final String GAME_VERSION_NAME = "MCMafia";
    public static final File GAME_DIRECTORY = new File(OS.getApplicationDataDirectory(), "mc-mafia");
    //public static final URL WEBSITE_URL = constantURL("http://mc-mafia.net");
    public static final URL BASE_URL = constantURL("http://mc-mafia.net/launcher/");
    public static final URL REGISTER_URL = constantURL("http://mc-mafia.net/?page=register");
    public static final URL NEWS_IMAGE_URL = baseURL("news.png");
    public static final URL AUTHENTICATION_URL = baseURL("auth.php");
    public static final URL COMPRESSED_FILES_LIST_URL = baseURL("filelist.txt.gz");
    public static final URL FILES_URL = baseURL("files/");
    public static final URL WHITELIST_FILES_URL = baseURL("whitelist.txt");
    public static final URL REMOTE_LAUNCHER_URL = baseURL("launcher.exe");
    public static final URL REMOTE_LAUNCHER_VERSION_URL = baseURL("launcher_version");
    private static File workingJar;

    static
    {
        IGNORE_LAUNCHER_UPDATER = Boolean.parseBoolean(System.getProperty("net.mc_mafia.launcher.ignoreLauncherUpdater"));
    }

    public static URL constantURL(String s)
    {
        try
        {
            return new URL(s);
        }
        catch (MalformedURLException e)
        {
            LOGGER.catching(e);
        }
        return null;
    }

    public static URL baseURL(String s)
    {
        try
        {
            return new URL(BASE_URL.toString() + s);
        }
        catch (MalformedURLException e)
        {
            LOGGER.catching(e);
        }
        return null;
    }

    public static File getWorkingJar()
    {
        if (workingJar == null)
        {
            try
            {
                workingJar = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            }
            catch (URISyntaxException e)
            {
                LOGGER.catching(e);
            }
        }

        return workingJar;
    }

    public static void setWorkingJar(File file)
    {
        workingJar = file;
    }
}
