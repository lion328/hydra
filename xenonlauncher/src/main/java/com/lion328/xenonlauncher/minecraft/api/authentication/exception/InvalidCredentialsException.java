package com.lion328.xenonlauncher.minecraft.api.authentication.exception;

public class InvalidCredentialsException extends MinecraftAuthenticatorException
{

    public InvalidCredentialsException()
    {
    }

    public InvalidCredentialsException(String message)
    {
        super(message);
    }

    public InvalidCredentialsException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidCredentialsException(Throwable cause)
    {
        super(cause);
    }

    public InvalidCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
