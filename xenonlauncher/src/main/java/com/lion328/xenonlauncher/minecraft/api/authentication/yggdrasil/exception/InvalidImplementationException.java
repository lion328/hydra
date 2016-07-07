package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.exception;

public class InvalidImplementationException extends YggdrasilAPIException
{

    public InvalidImplementationException()
    {
        super();
    }

    public InvalidImplementationException(String message)
    {
        super(message);
    }

    public InvalidImplementationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidImplementationException(Throwable cause)
    {
        super(cause);
    }

    protected InvalidImplementationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
