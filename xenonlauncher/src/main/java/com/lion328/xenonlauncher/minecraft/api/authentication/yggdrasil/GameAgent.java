package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil;

import com.google.gson.annotations.SerializedName;

public class GameAgent
{

    @SerializedName("name")
    private String name;
    @SerializedName("version")
    private int version;

    public GameAgent()
    {

    }

    public GameAgent(String name, int version)
    {
        this.name = name;
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public int getVersion()
    {
        return version;
    }
}
