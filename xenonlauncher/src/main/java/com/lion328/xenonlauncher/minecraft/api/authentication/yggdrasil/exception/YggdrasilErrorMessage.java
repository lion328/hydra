package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.exception;

import com.google.gson.annotations.SerializedName;

public class YggdrasilErrorMessage
{

    @SerializedName("error")
    private String shortMessage;
    @SerializedName("errorMessage")
    private String longMessage;
    @SerializedName("cause")
    private String cause;

    public YggdrasilErrorMessage()
    {

    }

    public YggdrasilErrorMessage(String shortMessage, String longMessage, String cause)
    {
        this.shortMessage = shortMessage;
        this.longMessage = longMessage;
        this.cause = cause;
    }

    public YggdrasilAPIException toException()
    {
        String message = shortMessage + ": " + longMessage;

        if (cause != null)
        {
            message += " (" + cause + ")";
        }

        return new YggdrasilAPIException(message);
    }

    public String getShortMessage()
    {
        return shortMessage;
    }

    public String getLongMessage()
    {
        return longMessage;
    }

    public String getCause()
    {
        return cause;
    }
}
