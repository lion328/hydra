package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class AssetInformation extends DownloadInformation
{

    @SerializedName("totalSize")
    private int totalSizeInBytes;
    @SerializedName("id")
    private String id;
    @SerializedName("known")
    private boolean knownSizeAndHash = false;

    public AssetInformation(int totalSizeInBytes, String id, URL url, String sha1Hash, int sizeInBytes)
    {
        super(url, sha1Hash, sizeInBytes);

        knownSizeAndHash = true;
    }

    public int getTotalSizeInBytes()
    {
        return totalSizeInBytes;
    }

    public String getID()
    {
        return id;
    }

    public boolean isKnownSizeAndHash()
    {
        return knownSizeAndHash;
    }
}
