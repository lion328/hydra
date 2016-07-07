package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.message;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.UserProfile;

public class RefreshMessage
{

    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("clientToken")
    private String clientToken;
    @SerializedName("selectedProfile")
    private UserProfile selectedProfile;

    public RefreshMessage()
    {

    }

    public RefreshMessage(String accessToken, String clientToken)
    {
        this(accessToken, clientToken, null);
    }

    public RefreshMessage(String accessToken, String clientToken, UserProfile selectedProfile)
    {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.selectedProfile = selectedProfile;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public String getClientToken()
    {
        return clientToken;
    }

    public UserProfile getSelectedProfile()
    {
        return selectedProfile;
    }
}
