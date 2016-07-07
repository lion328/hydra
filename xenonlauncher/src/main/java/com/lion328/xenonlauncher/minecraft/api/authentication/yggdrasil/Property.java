package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil;

import com.google.gson.annotations.SerializedName;

public class Property
{

    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public Property()
    {

    }

    public Property(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}
