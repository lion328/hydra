package com.lion328.xenonlauncher.proxy;

import java.net.Socket;

public interface DataHandler
{

    boolean process(Socket client, Socket server) throws Exception;
}
