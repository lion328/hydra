package com.lion328.xenonlauncher.minecraft.assets.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Assets
{

    @SerializedName("objects")
    private Map<String, AssetsObject> objects;

    public Assets()
    {

    }

    public Assets(Map<String, AssetsObject> objects)
    {
        this.objects = objects;
    }

    public Map<String, AssetsObject> getObjects()
    {
        return objects;
    }
}
