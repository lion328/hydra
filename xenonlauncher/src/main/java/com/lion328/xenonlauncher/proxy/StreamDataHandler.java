package com.lion328.xenonlauncher.proxy;

import com.lion328.xenonlauncher.proxy.util.StreamUtil;

import java.io.IOException;
import java.net.Socket;

public class StreamDataHandler implements DataHandler
{

    @Override
    public boolean process(Socket client, Socket server) throws IOException
    {
        StreamUtil.pipeStreamThread(client.getInputStream(), server.getOutputStream());
        StreamUtil.pipeStream(server.getInputStream(), client.getOutputStream());
        return false;
    }
}
