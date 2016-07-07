package com.lion328.xenonlauncher.proxy.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GreetingResponsePacket implements Packet
{

    private int version;
    private AuthenticationMethod selectedMethod;

    public GreetingResponsePacket()
    {

    }

    public GreetingResponsePacket(int version, AuthenticationMethod method)
    {
        this.version = version;
        selectedMethod = method;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public AuthenticationMethod getSelectedMethod()
    {
        return selectedMethod;
    }

    public void setSelectedMethod(AuthenticationMethod selectedMethod)
    {
        this.selectedMethod = selectedMethod;
    }

    @Override
    public void read(InputStream in) throws IOException
    {
        version = in.read();
        selectedMethod = AuthenticationMethod.getByByte(in.read());
    }

    @Override
    public void write(OutputStream out) throws IOException
    {
        out.write(version & 0xFF);
        out.write(selectedMethod.getByte() & 0xFF);
    }
}
