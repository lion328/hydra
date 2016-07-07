package com.lion328.xenonlauncher.settings;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class LauncherSettings
{

    @SerializedName("id")
    private String id;
    @SerializedName("authenticationServer")
    private URL authenticationServer;
    @SerializedName("versionsServer")
    private URL versionsServer;
    @SerializedName("librariesServer")
    private URL librariesServer;
    @SerializedName("assetsServer")
    private URL assetsServer;
}
