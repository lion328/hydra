package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.util.OS;

import java.util.List;

public class GameLibrary
{

    @SerializedName("name")
    private DependencyName name;
    @SerializedName("rules")
    private List<LibraryRule> rules;
    @SerializedName("natives")
    private LibraryNatives natives;
    @SerializedName("extract")
    private ExtractRule extractRule;
    @SerializedName("downloads")
    private LibraryDownloadInfomation downloadInfo;

    public GameLibrary()
    {

    }

    public GameLibrary(DependencyName name, List<LibraryRule> rules, LibraryNatives natives, ExtractRule extractRule, LibraryDownloadInfomation downloadInfo)
    {
        this.name = name;
        this.rules = rules;
        this.natives = natives;
        this.extractRule = extractRule;
        this.downloadInfo = downloadInfo;
    }

    public DependencyName getDependencyName()
    {
        return name;
    }

    public List<LibraryRule> getRules()
    {
        return rules;
    }

    public LibraryNatives getNatives()
    {
        return natives;
    }

    public ExtractRule getExtractRule()
    {
        return extractRule;
    }

    public LibraryDownloadInfomation getDownloadInfo()
    {
        return downloadInfo;
    }

    public boolean isNativesLibrary()
    {
        return natives != null;
    }

    public boolean isJavaLibrary()
    {
        return !isNativesLibrary() || (downloadInfo != null && downloadInfo.getArtifactInfo() != null);
    }

    public boolean isAllowed(OS os, String version)
    {
        if (rules == null)
        {
            return true;
        }

        for (LibraryRule rule : rules)
        {
            if (!rule.isAllowed(os, version))
            {
                return false;
            }
        }
        return true;
    }

    public boolean isAllowed()
    {
        return isAllowed(OS.getCurrentOS(), OS.getCurrentVersion());
    }
}
