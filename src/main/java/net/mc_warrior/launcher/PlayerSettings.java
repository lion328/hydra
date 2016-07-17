package net.mc_warrior.launcher;

import com.google.gson.annotations.SerializedName;

public class PlayerSettings
{

    public static final int VERSION = 1;

    @SerializedName("version")
    private int version;
    @SerializedName("playerName")
    private String playerName;
    @SerializedName("maximumMemory")
    private int maximumMemory;

    public PlayerSettings(String playerName, int maximumMemory)
    {
        this(VERSION, playerName, maximumMemory);
    }

    public PlayerSettings(int version, String playerName, int maximumMemory)
    {
        this.version = version;
        this.playerName = playerName;
        this.maximumMemory = maximumMemory;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public int getMaximumMemory()
    {
        return maximumMemory;
    }

    public void setMaximumMemory(int maximumMemory)
    {
        this.maximumMemory = maximumMemory;
    }
}
