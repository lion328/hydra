package com.lion328.xenonlauncher.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LauncherConstant
{

    public static final Logger LOGGER;
    public static final String VERSION;
    public static final String ID;

    static
    {
        LOGGER = LogManager.getLogger("XenonLauncher");
        VERSION = "0.1";

        InputStream in = LauncherConstant.class.getResourceAsStream("/com/lion328/xenonlauncher/settings/launcherid");
        String id = null;

        try
        {
            id = new BufferedReader(new InputStreamReader(in)).readLine().trim();
        }
        catch (IOException e)
        {
            LOGGER.catching(e);
        }

        ID = id;
    }
}
