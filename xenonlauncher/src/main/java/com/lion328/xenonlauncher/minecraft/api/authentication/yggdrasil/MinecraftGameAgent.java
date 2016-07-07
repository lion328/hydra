package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil;

public class MinecraftGameAgent extends GameAgent
{

    public static final int DEFAULT_VERSION = 1;

    public MinecraftGameAgent()
    {
        this(DEFAULT_VERSION);
    }

    public MinecraftGameAgent(int version)
    {
        super("minecraft", version);
    }
}
