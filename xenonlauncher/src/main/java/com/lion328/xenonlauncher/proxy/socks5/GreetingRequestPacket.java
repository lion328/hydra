package com.lion328.xenonlauncher.proxy.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GreetingRequestPacket implements Packet
{

    private int version;
    private AuthenticationMethod[] availableAuthenticationMethods;

    public GreetingRequestPacket()
    {

    }

    public GreetingRequestPacket(int version, AuthenticationMethod[] availableAuthenticationMethods) throws IOException
    {
        this.version = version;
        this.availableAuthenticationMethods = availableAuthenticationMethods.clone();
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public AuthenticationMethod[] getAvailableAuthenticationMethods()
    {
        return availableAuthenticationMethods;
    }

    public void setAvailableAuthenticationMethods(AuthenticationMethod[] availableAuthenticationMethods)
    {
        this.availableAuthenticationMethods = availableAuthenticationMethods;
    }

    @Override
    public void read(InputStream in) throws IOException
    {
        version = in.read() & 0xFF;
        int methodAvailable = in.read();
        availableAuthenticationMethods = new AuthenticationMethod[methodAvailable];
        for (int i = 0; i < methodAvailable; i++)
        {
            availableAuthenticationMethods[i] = AuthenticationMethod.getByByte((byte) (in.read() & 0xFF));
        }
    }

    @Override
    public void write(OutputStream out) throws IOException
    {
        out.write(version & 0xFF);
        out.write(availableAuthenticationMethods.length & 0xFF);
        for (AuthenticationMethod method : availableAuthenticationMethods)
        {
            out.write(method.getByte());
        }
    }
}
