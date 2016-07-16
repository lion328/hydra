package net.mc_warrior.launcher;

import com.lion328.xenonlauncher.settings.LauncherConstant;
import com.lion328.xenonlauncher.util.OS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Settings
{

    public static final Logger LOGGER = LogManager.getLogger("MCWarrior-Launcher");

    private static File workingJar;

    public static final String LAUNCHER_VERSION = "0.1.1";
    public static final String GAME_VERSION_NAME = "MCWarrior";
    public static final File GAME_DIRECTORY = new File(OS.getApplicationDataDirectory(), "mc-warrior");

    public static final URL WEBSITE_URL = constantURL("http://mc-warrior.net");
    public static final URL REGISTER_URL = constantURL("http://mc-warrior.net/?page=register");
    public static final URL NEWS_IMAGE_URL = constantURL("http://mc-warrior.net/launcher/news.png");
    public static final URL AUTHENTICATION_URL = constantURL("http://mc-warrior.net/launcher/auth.php");
    public static final URL FILES_LIST_URL = constantURL("http://mc-warrior.net/launcher/filelist.php");
    public static final URL FILES_URL = constantURL("http://mc-warrior.net/launcher/files/");
    public static final URL WHITELIST_FILES_URL = constantURL("http://mc-warrior.net/launcher/whitelist.txt");
    public static final URL REMOTE_LAUNCHER_URL = constantURL("http://mc-warrior.net/launcher/launcher.exe");
    public static final URL REMOTE_LAUNCHER_VERSION_URL = constantURL("http://mc-warrior.net/launcher/launcher_version");

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

    public static File getWorkingJar()
    {
        if (workingJar == null)
        {
            try
            {
                new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
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
