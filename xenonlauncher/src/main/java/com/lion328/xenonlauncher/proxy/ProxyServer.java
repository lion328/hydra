package com.lion328.xenonlauncher.proxy;

import java.io.IOException;
import java.net.ServerSocket;

public interface ProxyServer
{

    void start(ServerSocket server) throws IOException;

    void stop();

    void addDataHandler(int level, DataHandler handler);
}
