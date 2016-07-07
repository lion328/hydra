package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProperties
{

    @SerializedName("id")
    private String id;
    @SerializedName("properties")
    private List<Property> properties;

    public UserProperties()
    {

    }

    public UserProperties(String id, List<Property> properties)
    {
        this.id = id;
        this.properties = properties;
    }

    public String getId()
    {
        return id;
    }

    public List<Property> getProperties()
    {
        return properties;
    }
}
