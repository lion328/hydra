package com.lion328.xenonlauncher.proxy.socks5;

public enum AuthenticationMethod
{

    NO_AUTHENTICATION_REQUIRED(0x00),
    GSSAPI(0x01),
    USERNAME_PASSWORD(0x02),
    NO_ACCEPTABLE_METHODS(0xFF),
    UNKNOWN(-1);

    private int b;

    AuthenticationMethod(int b)
    {
        this.b = b;
    }

    public static AuthenticationMethod getByByte(int b)
    {
        for (AuthenticationMethod method : values())
        {
            if (method.b == (b & 0xFF))
            {
                return method;
            }
        }
        return UNKNOWN;
    }

    public int getByte()
    {
        return b & 0xFF;
    }
}
