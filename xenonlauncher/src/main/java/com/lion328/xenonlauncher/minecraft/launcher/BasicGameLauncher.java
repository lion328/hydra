package com.lion328.xenonlauncher.minecraft.launcher;

public abstract class BasicGameLauncher implements GameLauncher
{

    @Override
    public void addGameArgument(String key, String value)
    {
        addGameArgument("--" + key);
        if (value != null)
        {
            addGameArgument(value);
        }
    }

    @Override
    public void setMaxMemorySize(int mb)
    {
        addJVMArgument("-Xmx" + mb + "M");
    }
}
