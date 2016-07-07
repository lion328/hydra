package com.lion328.xenonlauncher.i18n;

import com.lion328.xenonlauncher.settings.LauncherConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class I18n
{

    private static Map<String, Properties> langTables;

    private static String currentLang;
    private static Properties currentLangTable;

    static
    {
        langTables = new HashMap<>();

        try
        {
            loadLanguage("th-TH");
            setCurrentLanguage("th-TH");
        }
        catch (IOException e)
        {
            LauncherConstant.LOGGER.catching(e);
        }
    }

    public static void loadLanguage(String name) throws IOException
    {
        String path = "/com/lion328/xenonlauncher/languages/" + name + ".properties";
        InputStream in = I18n.class.getResourceAsStream(path);

        Properties properties = new Properties();
        properties.load(in);

        langTables.put(name, properties);
    }

    public static boolean setCurrentLanguage(String name)
    {
        if (!langTables.containsKey(name))
        {
            try
            {
                loadLanguage(name);
            }
            catch (IOException e)
            {
                return false;
            }
        }

        currentLang = name;
        currentLangTable = langTables.get(name);

        return true;
    }

    public static String getCurrentLanguage()
    {
        return currentLang;
    }

    public static String get(String key)
    {
        String s = null;

        if (currentLangTable != null)
        {
            s = currentLangTable.getProperty(key);
        }

        if (s == null)
        {
            return key;
        }

        return s;
    }
}
