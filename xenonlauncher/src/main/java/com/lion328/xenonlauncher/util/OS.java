package com.lion328.xenonlauncher.util;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public enum OS
{

    @SerializedName("win")WINDOWS("win"),
    @SerializedName("osx")OSX("osx"),
    @SerializedName("linux")LINUX("linux");

    private static OS currentOS = null;
    private static File appdata = null;
    private final String s;

    OS(String s)
    {
        this.s = s;
    }

    public static OS fromString(String s)
    {
        for (OS os : values())
        {
            if (os.toString().equals(s))
            {
                return os;
            }
        }
        return null;
    }

    public static OS getCurrentOS()
    {
        if (currentOS == null)
        {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win"))
            {
                currentOS = OS.WINDOWS;
            }
            else if (osName.contains("mac"))
            {
                currentOS = OS.OSX;
            }
            else
            {
                currentOS = OS.LINUX;
            }
        }
        return currentOS;
    }

    public static String getCurrentVersion()
    {
        return System.getProperty("os.version");
    }

    public static String getCurrentArchitecture()
    {
        return System.getProperty("sun.arch.data.model");
    }

    public static File getApplicationDataDirectory()
    {
        if (appdata == null)
        {
            File home = new File(System.getProperty("user.home", "."));

            switch (getCurrentOS())
            {
                default:
                case LINUX:
                    appdata = home;
                    break;
                case WINDOWS:
                    String winAppdata = System.getenv("APPDATA");

                    if (winAppdata == null)
                    {
                        appdata = home;
                    }
                    else
                    {
                        appdata = new File(winAppdata);
                    }

                    break;
                case OSX:
                    appdata = new File(home, "Library/Application Support/");
                    break;
            }
        }
        return appdata;
    }

    public String toString()
    {
        return s;
    }
}