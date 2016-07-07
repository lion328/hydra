package com.lion328.xenonlauncher.proxy.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Packet
{

    void read(InputStream in) throws IOException;

    void write(OutputStream out) throws IOException;
}
