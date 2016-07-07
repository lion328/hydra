package com.lion328.xenonlauncher.minecraft.launcher.json.data.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.type.DependencyNameTypeAdapter;

public class GsonFactory
{

    private static Gson defaultGson;

    public static Gson create()
    {
        if (defaultGson == null)
        {
            defaultGson = new GsonBuilder()
                    .registerTypeAdapter(DependencyName.class, new DependencyNameTypeAdapter())
                    .create();
        }

        return defaultGson;
    }
}
