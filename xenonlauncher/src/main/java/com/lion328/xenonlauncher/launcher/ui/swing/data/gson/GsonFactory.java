package com.lion328.xenonlauncher.launcher.ui.swing.data.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lion328.xenonlauncher.launcher.ui.swing.data.type.ComponentTypeAdapter;

import java.awt.Component;

public class GsonFactory
{

    private static Gson defaultGson;

    public static Gson create()
    {
        if (defaultGson == null)
        {
            defaultGson = new GsonBuilder()
                    .registerTypeAdapter(Component.class, new ComponentTypeAdapter())
                    .create();
        }

        return defaultGson;
    }
}
