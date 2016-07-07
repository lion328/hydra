package com.lion328.xenonlauncher.proxy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil
{

    public static void pipeStream(InputStream in, OutputStream out)
    {
        int b;
        try
        {
            while ((b = in.read()) != -1)
            {
                out.write(b);
            }
        }
        catch (IOException e)
        {

        }
    }

    public static void pipeStreamThread(final InputStream in, final OutputStream out)
    {
        new Thread()
        {

            public void run()
            {
                pipeStream(in, out);
            }
        }.start();
    }
}
