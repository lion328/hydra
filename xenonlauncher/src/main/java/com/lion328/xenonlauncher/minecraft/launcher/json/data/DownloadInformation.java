package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class DownloadInformation
{

    @SerializedName("url")
    private URL url;
    @SerializedName("sha1")
    private String sha1Hash;
    @SerializedName("size")
    private int sizeInBytes;

    public DownloadInformation()
    {

    }

    public DownloadInformation(URL url, String sha1Hash, int sizeInBytes)
    {
        this.url = url;
        this.sha1Hash = sha1Hash;
        this.sizeInBytes = sizeInBytes;
    }

    public URL getURL()
    {
        return url;
    }

    public String getSHA1Hash()
    {
        return sha1Hash;
    }

    public int getSizeInBytes()
    {
        return sizeInBytes;
    }
}
