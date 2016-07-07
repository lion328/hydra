package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtractRule
{

    @SerializedName("exclude")
    private List<String> exclude;

    public ExtractRule()
    {

    }

    public ExtractRule(List<String> excludeList)
    {
        exclude = excludeList;
    }

    public List<String> getExcludeList()
    {
        return exclude;
    }
}
