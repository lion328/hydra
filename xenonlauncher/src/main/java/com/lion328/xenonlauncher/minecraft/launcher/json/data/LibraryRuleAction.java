package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

public enum LibraryRuleAction
{

    @SerializedName("allow")ALLOW("allow"),
    @SerializedName("disallow")DISALLOW("disallow");

    private transient String s;

    LibraryRuleAction(String s)
    {
        this.s = s;
    }

    public String toString()
    {
        return s;
    }
}