package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.util.OS;

public class LibraryRule
{

    @SerializedName("action")
    private LibraryRuleAction action;
    @SerializedName("os")
    private SystemIdentifier identifier;

    public LibraryRule()
    {
        this.action = LibraryRuleAction.ALLOW;
    }

    public LibraryRule(LibraryRuleAction action, SystemIdentifier identifier)
    {
        this.action = action;
        this.identifier = identifier;
    }

    public LibraryRuleAction getAction()
    {
        return action;
    }

    public SystemIdentifier getIdentifier()
    {
        return identifier;
    }

    public boolean isAllowed(OS os, String version)
    {
        return (action == LibraryRuleAction.ALLOW) == (identifier == null || identifier.isMatch(os, version));
    }

    public static class SystemIdentifier
    {

        @SerializedName("name")
        private OS os;
        @SerializedName("version")
        private String versionPattern;

        public SystemIdentifier()
        {

        }

        public SystemIdentifier(OS os)
        {
            this(os, "(.*?)");
        }

        public SystemIdentifier(OS os, String versionPattern)
        {
            this.os = os;
            this.versionPattern = versionPattern;
        }

        public OS getOS()
        {
            return os;
        }

        public boolean isMatch(OS os, String version)
        {
            return this.os == os && (versionPattern == null || version.matches(versionPattern));
        }
    }
}
