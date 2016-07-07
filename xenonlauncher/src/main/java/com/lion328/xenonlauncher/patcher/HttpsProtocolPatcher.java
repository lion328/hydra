package com.lion328.xenonlauncher.patcher;

public class HttpsProtocolPatcher extends StringReplaceFilePatcher
{

    private final String protocol;

    public HttpsProtocolPatcher(String protocol)
    {
        super("https://", protocol + "://");

        this.protocol = protocol;
    }

    public String getProtocol()
    {
        return protocol;
    }
}
