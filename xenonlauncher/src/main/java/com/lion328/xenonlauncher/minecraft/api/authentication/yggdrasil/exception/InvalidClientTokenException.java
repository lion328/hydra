package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil.exception;

public class InvalidClientTokenException extends InvalidImplementationException
{

    public InvalidClientTokenException()
    {
        super();
    }

    public InvalidClientTokenException(String message)
    {
        super(message);
    }

    public InvalidClientTokenException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public InvalidClientTokenException(Throwable cause)
    {
        super(cause);
    }

    protected InvalidClientTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
