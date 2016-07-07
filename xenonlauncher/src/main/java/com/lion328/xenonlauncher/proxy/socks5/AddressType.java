package com.lion328.xenonlauncher.proxy.socks5;

public enum AddressType
{

    IPV4(0x01),
    DOMAINNAME(0x03),
    IPV6(0x04);

    private int b;

    AddressType(int b)
    {
        this.b = b;
    }

    public static AddressType getByByte(int b)
    {
        for (AddressType type : values())
        {
            if (type.b == (b & 0xFF))
            {
                return type;
            }
        }
        return null;
    }

    public int getByte()
    {
        return b & 0xFF;
    }
}

