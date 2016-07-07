package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class PathDownloadInformation extends DownloadInformation
{

    @SerializedName("path")
    private String path;

    public PathDownloadInformation()
    {

    }

    public PathDownloadInformation(URL url, String sha1Hash, int sizeInBytes, String path)
    {
        super(url, sha1Hash, sizeInBytes);
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
}
